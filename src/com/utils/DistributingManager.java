package com.utils;

import com.server.ConnectionHandler;
import com.server.Status;

import java.util.ArrayList;

public class DistributingManager {
    private String command;

    public void setCommand(String command) {
        this.command = command;
    }

    public void distribute(ArrayList<String> chunks, ArrayList<ConnectionHandler> encoders) {
        if (encoders.size() != 0) {
            if (chunks.size() != 0) {
                for (ConnectionHandler encoder : encoders) {
                    if (encoder.getStatus().equals(Status.IDLE)) {
                        encoder.setStatus(Status.IN_FILE_TRANSFER);
                        distribute(encoder, chunks.get(0), command);
                        chunks.remove(0);
                    }
                    if (chunks.size() == 0) break;
                }
            } else {
                System.out.println("There are no (more) inputs to be encoded!");
            }
        } else {
            System.err.println("There are no encoders online at the moment!");
        }
    }

    private void distribute(ConnectionHandler encoderHandler, String fileName, String command) {
        encoderHandler.sendCommandToEncoder(command);
        encoderHandler.sendFile(fileName);
        encoderHandler.setStatus(Status.ENCODING);
    }
}
