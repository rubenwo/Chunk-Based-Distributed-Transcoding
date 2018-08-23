package com.encoderclient.GUI;

import com.encoderclient.EncoderClient;

import javax.swing.*;
import java.awt.*;

public class PreEncoderFrame {
    public PreEncoderFrame() {
        JFrame frame = new JFrame("Create an Encoder node");
        frame.setPreferredSize(new Dimension(350, 115));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel contentPane = new JPanel(new BorderLayout());
        JPanel inputForms = new JPanel();
        inputForms.setLayout(new BoxLayout(inputForms, BoxLayout.Y_AXIS));

        JLabel ipaddr = new JLabel("Please enter the Server IP-Address below:");
        JTextField getIpaddr = new JTextField();

        getIpaddr.addActionListener(e -> {
            new EncoderClient(getIpaddr.getText(), false);
            frame.dispose();
        });

        inputForms.add(ipaddr);
        inputForms.add(getIpaddr);

        JButton startNode = new JButton("Start Encoder Node");
        startNode.addActionListener(e -> {
            new EncoderClient(getIpaddr.getText(), false);
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
