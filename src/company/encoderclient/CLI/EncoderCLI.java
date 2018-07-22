package company.encoderclient.CLI;

import company.encoderclient.EncoderClient;

import java.util.Scanner;

public class EncoderCLI {
    private EncoderClient encoderClient;

    private EncoderCLI() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the Server IP-Address:");
        String serverIP = input.nextLine();

        encoderClient = new EncoderClient(serverIP, true);
    }

    public static void main(String[] args) {
        new EncoderCLI();
    }
}
