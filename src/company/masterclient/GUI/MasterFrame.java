package company.masterclient.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class MasterFrame {
    private String[] inputs;
    private OnlineClientPanel onlineClientPanel;

    public MasterFrame(ArrayList<String> onlineClients, CommandListener commandListener, SlaveStatusListener slaveStatusListener) {
        JFrame frame = new JFrame("File-Based Distributed Transcoding Master");
        frame.setPreferredSize(new Dimension(1280, 720));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());

        onlineClientPanel = new OnlineClientPanel(onlineClients, slaveStatusListener);
        onlineClientPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        JPanel ffmpegOptionPanel = new FFmpegOptionPanel(this, commandListener);
        ffmpegOptionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        contentPane.add(onlineClientPanel, BorderLayout.EAST);
        contentPane.add(ffmpegOptionPanel, BorderLayout.CENTER);

        frame.setJMenuBar(buildMenuBar());
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");

        JMenuItem openDirectory = new JMenuItem("Open Directory");
        openDirectory.addActionListener(e -> {
            System.out.println("Clicked open.");
            openDirectory(openDirectory);
            for (String str : inputs)
                System.out.println(str);
        });
        openDirectory.setToolTipText("Choose the directory you wish to transcode.");
        JMenuItem savePreset = new JMenuItem("Save Preset");
        savePreset.addActionListener(e -> {
            System.out.println("Clicked save preset");
        });
        JMenuItem loadPreset = new JMenuItem("Load Preset");
        loadPreset.addActionListener(e -> {
            System.out.println("Clicked load preset");
        });

        file.add(openDirectory);
        file.add(savePreset);
        file.add(loadPreset);

        menuBar.add(file);
        return menuBar;
    }

    private void openDirectory(JMenuItem item) {
        JFileChooser fc = new JFileChooser("./res");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setDialogTitle("Open Directory");
        fc.setApproveButtonText("Open");
        if (fc.showOpenDialog(item) == JFileChooser.APPROVE_OPTION) {
            inputs = getInputs(fc.getCurrentDirectory().toString());
        }

    }

    private String[] getInputs(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        String[] inputs = new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile())
                inputs[i] = listOfFiles[i].getAbsolutePath();
        }
        return inputs;
    }

    public String[] getInputs() {
        return inputs;
    }

    public void updateClientList(ArrayList<String> onlineClients) {
        onlineClientPanel.updateClientList(onlineClients);
    }
}
