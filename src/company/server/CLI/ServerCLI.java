package company.server.CLI;

import company.Constants;

public class ServerCLI {
    public ServerCLI(String ipAddress) {
        System.out.println("Server " + Constants.VERSION);
        System.out.println("Server IP-Address: " + ipAddress + "\nServer Port: " + Constants.NETWORK_PORT);
    }
}
