package com.masterclient;

import com.Constants;
import com.masterclient.GUI.MasterFrame;

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

    private boolean running = true;

    public MasterClient(String serverIP) {
        try {
            init(serverIP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new UpdaterService()).start();
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

    class UpdaterService implements Runnable {
        @Override
        public void run() {
            System.out.println("Running updater service.");
            while (running) {
                try {
                    byte dataType = fromServer.readByte();
                    switch (dataType) {
                        case 0:
                            try {
                                onlineEncoders = (ArrayList<String>) fromServer.readObject();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        case 1:
                            try {
                                encoderProgress = (HashMap<String, Double>) fromServer.readObject();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
