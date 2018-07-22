package guitests;

import company.masterclient.GUI.MasterFrame;

import java.util.ArrayList;
import java.util.UUID;

public class MasterGUITest {
    public static void main(String[] args) {
        ArrayList<String> onlineClients = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            onlineClients.add(UUID.randomUUID().toString());
        new MasterFrame(onlineClients);
    }
}
