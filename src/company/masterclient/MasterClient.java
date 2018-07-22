package company.masterclient;

import company.Constants;
import company.masterclient.GUI.MasterFrame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MasterClient {
    private ArrayList<String> onlineEncoders = new ArrayList<>();
    private HashMap<String, Double> encoderProgress = new HashMap<>();

    private MasterFrame masterFrame;

    private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;

    private String ID = UUID.randomUUID().toString();

    public MasterClient(String serverIP) {
        try {
            init(serverIP);
        } catch (IOException e) {
            e.printStackTrace();
        }

        masterFrame = new MasterFrame(onlineEncoders);
    }

    private void init(String serverIP) throws IOException {
        socket = new Socket(serverIP, Constants.NETWORK_PORT);

        toServer = new ObjectOutputStream(socket.getOutputStream());
        toServer.flush();
        fromServer = new ObjectInputStream(socket.getInputStream());

        toServer.writeUTF("Master");
        toServer.flush();
        toServer.writeUTF(this.ID);
        toServer.flush();

        try {
            onlineEncoders = (ArrayList<String>) fromServer.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Double> getEncoderProgress() {
        return encoderProgress;
    }
}
