package company.server;

import company.server.GUI.ServerGUI;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {
    public Server() {
        String ipAddress = null;
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        assert (ipAddress != null);
        new ServerGUI(ipAddress);
        new Thread(new MultiThreadedServer(ipAddress)).start();
    }

    public static void main(String[] args) {
        new Server();
    }
}
