package company.masterclient.GUI;

import company.masterclient.MasterClient;

import javax.swing.*;
import java.awt.*;

public class PreMasterFrame {
    public PreMasterFrame() {
        JFrame frame = new JFrame("Create a Master node");
        frame.setPreferredSize(new Dimension(350, 115));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel contentPane = new JPanel(new BorderLayout());
        JPanel inputForms = new JPanel();
        inputForms.setLayout(new BoxLayout(inputForms, BoxLayout.Y_AXIS));

        JLabel ipaddr = new JLabel("Enter the Server IP-Address below:");
        JTextField getIpaddr = new JTextField();

        getIpaddr.addActionListener(e -> {
            new MasterClient(getIpaddr.getText());
            frame.dispose();
        });

        inputForms.add(ipaddr);
        inputForms.add(getIpaddr);

        JButton startNode = new JButton("Start Master Node");
        startNode.addActionListener(e -> {
            new MasterClient(getIpaddr.getText());
            frame.dispose();
        });

        contentPane.add(inputForms, BorderLayout.CENTER);
        contentPane.add(startNode, BorderLayout.SOUTH);

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
