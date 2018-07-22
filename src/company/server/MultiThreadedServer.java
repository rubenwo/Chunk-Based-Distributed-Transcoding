package company.server;

import company.Constants;
import company.utils.DistributingManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class MultiThreadedServer implements Runnable, ClientStatusListener, EncoderProgressListener {
    private DistributingManager distributingManager;

    private HashMap<String, Double> encoderProgress = new HashMap<>();
    private ArrayList<String> onlineEncoders = new ArrayList<>();
    private ArrayList<ConnectionHandler> masters = new ArrayList<>();
    private ArrayList<ConnectionHandler> encoders = new ArrayList<>();

    private ServerSocket serverSocket;

    private boolean running = true;

    public MultiThreadedServer(String ipAddress) {
        init(ipAddress);
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
            new Thread(new ConnectionHandler(socket, onlineEncoders, this, this)).start();
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

    public void shutdown() {
        running = false;
    }
}