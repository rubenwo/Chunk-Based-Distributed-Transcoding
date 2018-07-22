package company.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionHandler implements Runnable {
    private ArrayList<String> onlineEncoders;
    private Socket socket;
    private ObjectInputStream fromClient;
    private ObjectOutputStream toClient;

    private ClientStatusListener clientStatusListener;

    private String clientID;
    private String clientIP;

    public ConnectionHandler(Socket socket, ArrayList<String> onlineEncoders, ClientStatusListener clientStatusListener) {
        this.socket = socket;
        this.onlineEncoders = onlineEncoders;
        this.clientStatusListener = clientStatusListener;
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
                toClient.writeObject(onlineEncoders);
                toClient.flush();
                break;
            case "Encoder":
                System.out.println("An Encoder just came online!");
                clientID = fromClient.readUTF();
                clientIP = fromClient.readUTF();
                System.out.println("Client IP: " + clientIP);
                break;
        }
    }

    @Override
    public void run() {

    }

    public void updateOnlineClientList(ArrayList<String> onlineEncoders) {
        this.onlineEncoders = onlineEncoders;
    }

    public String getClientID() {
        return clientID;
    }
}
