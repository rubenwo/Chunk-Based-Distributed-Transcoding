package guitests;

import company.server.GUI.ServerGUI;

import java.io.IOException;
import java.net.InetAddress;

public class ServerGUITest {
    public static void main(String[] args) throws IOException {
        new ServerGUI(InetAddress.getLocalHost().getHostAddress());
    }
}
