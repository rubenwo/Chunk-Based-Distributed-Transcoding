package company.encoderclient;

import company.Constants;
import company.encoderclient.GUI.EncoderFrame;
import company.utils.ExtensionGenerator;
import company.utils.OperatingSystem;
import company.utils.TempDirCreator;
import company.utils.ffmpeghandler.CommandType;
import company.utils.ffmpeghandler.FFmpegHandler;
import company.utils.ffmpeghandler.FFmpegListener;
import company.utils.filehandler.FileReceiver;
import company.utils.filehandler.FileReceiverListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

public class EncoderClient implements FFmpegListener, FileReceiverListener {
    private OperatingSystem operatingSystem;
    private String tempDir;

    private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;

    private EncoderFrame encoderFrame;

    private String ID = UUID.randomUUID().toString();
    private String IP;

    private boolean running = true;
    private boolean asCommandLine;

    private String currentCommand = null;
    private String currentInput = null;

    public EncoderClient(String serverIp, boolean asCommandLine) {
        this.asCommandLine = asCommandLine;
        operatingSystem = OperatingSystem.detectOperatingSystem();

        try {
            tempDir = TempDirCreator.initializeTemporaryDirectory(ID, operatingSystem);

            init(serverIp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new UpdaterService()).start();
        if (!asCommandLine)
            encoderFrame = new EncoderFrame(IP, ID);
    }

    private void init(String serverIp) throws IOException {
        socket = new Socket(serverIp, Constants.NETWORK_PORT);

        this.IP = InetAddress.getLocalHost().getHostAddress();

        toServer = new ObjectOutputStream(socket.getOutputStream());
        toServer.flush();
        fromServer = new ObjectInputStream(socket.getInputStream());

        toServer.writeUTF("Encoder");
        toServer.flush();
        toServer.writeUTF(this.ID);
        toServer.flush();
        toServer.writeUTF(this.IP);
        toServer.flush();
    }

    private void startFileReceiver() {
        new Thread(new FileReceiver(this, currentInput, tempDir)).start();
    }

    private void startEncoderThread() {
        assert ((currentCommand != null) && (currentInput != null));
        new Thread(new FFmpegHandler(tempDir + "ffmpeg",
                currentInput,
                currentCommand,
                tempDir + "transcoded/" + ExtensionGenerator.GenerateExtension(currentInput),
                this,
                CommandType.TRANSCODE));
    }

    @Override
    public void onEncoderStart(String filename) {
        if (!asCommandLine)
            encoderFrame.setCurrentJobFileName(filename);
        else {
            System.out.println("Encoding: " + filename);
        }
    }

    @Override
    public void onProgressUpdate(double progress) {
        if (!asCommandLine)
            encoderFrame.updateCurrentJob(progress);
        else {
            System.out.println("Progress: " + progress + "%");
        }
    }

    @Override
    public void onJobDone(CommandType type) {
        if (!type.equals(CommandType.TRANSCODE))
            System.err.println("Something went wrong with ffmpeg!");
    }

    @Override
    public void onFileReceiverOnline() {
        try {
            toServer.writeByte(3);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileReceived() {
        startEncoderThread();
    }

    class UpdaterService implements Runnable {
        @Override
        public void run() {
            System.out.println("Started Update Service for EncoderClient: " + ID);
            while (running) {
                try {
                    byte dataTyoe = fromServer.readByte();
                    switch (dataTyoe) {
                        case 0:
                            currentCommand = fromServer.readUTF();
                            break;
                        case 1:
                            currentInput = fromServer.readUTF();
                            startFileReceiver();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
