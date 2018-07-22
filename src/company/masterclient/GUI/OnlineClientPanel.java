package company.masterclient.GUI;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class OnlineClientPanel extends JPanel {
    private ArrayList<String> onlineClients;
    private JList onlineClientJList;

    private DetailedProgressFrame detailedProgressFrame;


    public OnlineClientPanel(ArrayList<String> onlineClients) {
        this.onlineClients = onlineClients;
        buildClientList();
    }

    private void buildClientList() {
        onlineClientJList = new JList(onlineClients.toArray());

        onlineClientJList.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println(onlineClientJList.getSelectedValue());
                if (detailedProgressFrame != null)
                    detailedProgressFrame.dispose();
                detailedProgressFrame = new DetailedProgressFrame((String) onlineClientJList.getSelectedValue());
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        onlineClientJList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(onlineClientJList.getSelectedValue());
                if (detailedProgressFrame != null)
                    detailedProgressFrame.dispose();
                detailedProgressFrame = new DetailedProgressFrame((String) onlineClientJList.getSelectedValue());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.add(onlineClientJList);
    }

    public void updateClientList(ArrayList<String> onlineClients) {
        this.onlineClients = onlineClients;
        onlineClientJList.setListData(this.onlineClients.toArray());
    }
}
