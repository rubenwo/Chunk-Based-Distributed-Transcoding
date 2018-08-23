package company.encoderclient.GUI;

import javax.swing.*;
import java.awt.*;

public class EncoderFrame {
    private JTextArea currentJob;
    private JProgressBar progressBar;

    public EncoderFrame(String ip, String clientId) {
        JFrame frame = new JFrame("Encoder Frame");
        frame.setPreferredSize(new Dimension(350, 115));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        JPanel contentPane = new JPanel(new BorderLayout());

        JTextArea ipAddress = new JTextArea(clientId + "\nIP-Address: " + ip);
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
    }

    public void updateCurrentJob(double progress) {
        progressBar.setValue((int) progress);
        progressBar.setString("" + progress + "%");
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
