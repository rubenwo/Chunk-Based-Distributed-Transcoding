package com.masterclient.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DetailedProgressFrame implements ActionListener {
    private JTextArea currentJob;
    private JProgressBar progressBar;
    private JFrame frame;

    private String encoderID;

    private Timer timer;

    public DetailedProgressFrame(String encoderID) {
        this.encoderID = encoderID;
        frame = new JFrame("Detailed Progress");
        frame.setPreferredSize(new Dimension(350, 110));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel contentPane = new JPanel(new BorderLayout());

        JTextArea ipAddress = new JTextArea("Encoder ID: " + encoderID);
        ipAddress.setEditable(false);

        currentJob = new JTextArea();
        currentJob.setEditable(false);
        currentJob.setText("Current Job: None\nProgress:");

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("N/A");

        contentPane.add(ipAddress, BorderLayout.NORTH);
        contentPane.add(currentJob, BorderLayout.CENTER);
        contentPane.add(progressBar, BorderLayout.SOUTH);

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        timer = new Timer(500, this);
        timer.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //updateCurrentJob(MasterClient.get(encoderID));
        System.out.println("Update...");
    }

    private void updateCurrentJob(double progress) {
        progressBar.setValue((int) progress);
        progressBar.setString("" + progress + "%");
    }

    public void dispose() {
        timer.stop();
        frame.dispose();
    }

    public void setCurrentJobFileName(String fileName) {
        currentJob.setText("Current Job: " + fileName + "\nProgress:");
    }

    public void resetFrame() {
        currentJob.setText("Current Job: None\nProgress:");
        progressBar.setValue(0);
        progressBar.setString("N/A");
    }
}
