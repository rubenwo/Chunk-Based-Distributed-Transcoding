package company.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionHandler implements Runnable {
    private ArrayList<String> onlineEncoders;
    private Socket socket;
    private ObjectInputStream fromClient;
    private ObjectOutputStream toClient;

    private ClientStatusListener clientStatusListener;
    private EncoderProgressListener progressListener;

    private String clientID;
    private String clientIP;

    private boolean running = true;

    public ConnectionHandler(Socket socket, ArrayList<String> onlineEncoders, ClientStatusListener clientStatusListener, EncoderProgressListener progressListener) {
        this.socket = socket;
        this.onlineEncoders = onlineEncoders;
        this.clientStatusListener = clientStatusListener;
        this.progressListener = progressListener;
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        fromClient = new ObjectInputStream(socket.getInputStream());
        toClient = new ObjectOutputStream(socket.getOutputStream());
        toClient.flush();

        String clientType = fromClient.readUTF();
        switch (clientType) {
            case "Master":
                System.out.println("A Master client just came online!");
                clientID = fromClient.readUTF();
                clientStatusListener.onMasterOnline(this);
                toClient.writeObject(onlineEncoders);
                toClient.flush();
                break;
            case "Encoder":
                System.out.println("An Encoder just came online!");
                clientID = fromClient.readUTF();
                clientIP = fromClient.readUTF();
                clientStatusListener.onEncoderOnline(this);
                System.out.println("Client IP: " + clientIP);
                break;
        }
    }

    @Override
    public void run() {
        System.out.println("Receiving updates from client...");
        while (running) {
            try {
                byte dataType = fromClient.readByte();
                switch (dataType) {
                    case 0:
                        // Input files
                        break;
                    case 1:
                        // Commands for encoders
                        break;
                    case 2:
                        double progress = fromClient.readDouble();
                        progressListener.onProgressUpdate(this.clientID, progress);
                        break;
                    case 3:
                        // Encoder is ready to receive a file.
                        break;
                    case 4:
                        // Encoder is done encoding
                        // Initialize a file receiver on Server-Side
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateOnlineClientList(ArrayList<String> onlineEncoders) {
        this.onlineEncoders = onlineEncoders;
        try {
            toClient.writeByte(0);
            toClient.flush();
            toClient.writeObject(onlineEncoders);
            toClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateProgressMap(HashMap<String, Double> encoderProgress) {
        try {
            toClient.writeByte(1);
            toClient.flush();
            toClient.writeObject(encoderProgress);
            toClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        running = false;
    }

    public String getClientID() {
        return clientID;
    }
}
