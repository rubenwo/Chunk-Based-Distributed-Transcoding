package company.server;

import company.Constants;
import company.utils.DistributingManager;
import company.utils.OperatingSystem;
import company.utils.TempDirCreator;
import company.utils.ffmpeghandler.ChunkGenerater;
import company.utils.ffmpeghandler.CommandType;
import company.utils.ffmpeghandler.FFmpegListener;
import company.utils.filehandler.FilesHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MultiThreadedServer implements Runnable, ClientStatusListener, EncoderProgressListener, InputsListener, FFmpegListener {
    private DistributingManager distributingManager;
    private OperatingSystem operatingSystem;
    private String tempDir;

    private HashMap<String, Double> encoderProgress = new HashMap<>();
    private ArrayList<String> onlineEncoders = new ArrayList<>();
    private ArrayList<ConnectionHandler> masters = new ArrayList<>();
    private ArrayList<ConnectionHandler> encoders = new ArrayList<>();

    private ServerSocket serverSocket;

    private ArrayList<String> inputs;
    private String command;
    private String concatFile;

    private boolean running = true;

    public MultiThreadedServer(String ipAddress) {
        init(ipAddress);
        operatingSystem = OperatingSystem.detectOperatingSystem();
        try {
            tempDir = TempDirCreator.initializeTemporaryDirectory("Multi_Threaded_Server_" + ipAddress, operatingSystem);
        } catch (IOException e) {
            e.printStackTrace();
        }
        distributingManager = new DistributingManager();
    }

    private void init(String ipAddress) {
        try {
            serverSocket = new ServerSocket(Constants.NETWORK_PORT, 0, InetAddress.getByName(ipAddress));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                System.out.println("Connection established!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(new ConnectionHandler(socket, onlineEncoders, this, this, this)).start();
        }
    }

    private void updateOnlineClientList() {
        for (ConnectionHandler masterHandler : masters)
            masterHandler.updateOnlineClientList(onlineEncoders);
    }

    private void updateEncoderProgress() {
        for (ConnectionHandler masterHandler : masters)
            masterHandler.updateProgressMap(encoderProgress);
    }

    private void cacheAndGenerateChunks() throws IOException {
        if (inputs.size() > 0) {
            String input = tempDir + inputs.get(0).substring(inputs.get(0).lastIndexOf("/"));
            FilesHandler.LocalCopy(inputs.get(0), input);
            ChunkGenerater.GenerateChunks(OperatingSystem.getEncoderPath(operatingSystem), input, this);
        } else {
            System.out.println("No more inputs to cache!");
        }
    }

    @Override
    public void onMasterOnline(ConnectionHandler masterHandler) {
        masters.add(masterHandler);
    }

    @Override
    public void onMasterOffline(ConnectionHandler masterHandler) {
        masters.removeIf(e -> e.equals(masterHandler));
    }

    @Override
    public void onEncoderOnline(ConnectionHandler encoderHandler) {
        encoders.add(encoderHandler);
        encoderProgress.put(encoderHandler.getClientID(), -1.0);
        onlineEncoders.add(encoderHandler.getClientID());
        updateOnlineClientList();
    }

    @Override
    public void onEncoderOffline(ConnectionHandler encoderHandler) {
        encoders.removeIf(e -> e.equals(encoderHandler));
        encoderProgress.remove(encoderHandler.getClientID());
        onlineEncoders.removeIf(e -> e.equals(encoderHandler.getClientID()));
        updateOnlineClientList();
    }

    @Override
    public void onProgressUpdate(String clientID, double progress) {
        encoderProgress.put(clientID, progress);
        updateEncoderProgress();
    }

    @Override
    public void onInputsAvailable(String[] inputs) {
        Collections.addAll(this.inputs, inputs);
        try {
            cacheAndGenerateChunks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCommandAvailable(String command) {
        this.command = command;
    }

    @Override
    public void onEncoderStart(String filename) {
        System.out.println("Started ffmpeg for: " + filename);
    }

    @Override
    public void onProgressUpdate(double progress) {
        System.out.println("Progress: " + progress + "%");
    }

    @Override
    public void onJobDone(CommandType type) {
        System.out.println("Done encoding!");
        if (type.equals(CommandType.GENERATE_CHUNKS))
            try {
                concatFile = ChunkGenerater.GenerateConcatFile(tempDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void shutdown() {
        running = false;
    }
}
