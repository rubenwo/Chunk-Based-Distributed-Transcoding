package company.masterclient.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class FFmpegOptionPanel extends JPanel {
    private HashMap<String, String> ffmpegCommandMap = new HashMap<>();
    private int crf_Min = 0;
    private int crf_Max = 30;
    private int threads_Min = 1;
    private int threads_Max = Runtime.getRuntime().availableProcessors() + 1;
    private String[][] videoPresets = {{"Select a Video Preset", "ultrafast", "superfast", "veryfast", "faster", "fast", "medium", "slow", "slower", "veryslow", "placebo"},
            {"Select a Video Preset", "hq", "hp", "bd", "ll", "llhq", "llhp", "default"}};
    private String[][] audioBitrate = {{"Select Audio Bitrate", "96", "128", "160", "192", "256", "320"}, {"Select Audo Bitrate"}};
    private String[] videoEncoderLibs = {"Select a Video Encoder", "libx264", "libx265", "nvenc_hevc", "nvenc_h264"};
    private String[] audioEncoderLibs = {"Select an Audio Encoder", "mp3", "aac", "ac3", "eac3"};
    private String[] audioChannels = {"Select # of Audio channels (Default = input)", "2.1", "5.1", "7.1"};
    private String[] outputFilesFormats = {"Select a output File Format", ".mkv", ".mp4", ".mov", ".mpg"};
    private String[] inputs;

    public FFmpegOptionPanel(MasterFrame masterFrame) {
        this.setLayout(new BorderLayout());
        this.add(dropDownPanel(), BorderLayout.CENTER);
        JTextField commandLine = new JTextField();
        JButton startEncoding = new JButton("Start Encoding");
        startEncoding.addActionListener(e -> {
            inputs = masterFrame.getInputs();
            if (inputs != null) {
                String[] commands;
                if (!commandLine.getText().isEmpty()) {
                    commands = getFfmpegCommandsWithCLI(commandLine.getText());
                } else {
                    commands = getFFmpegCommands();
                }
            } else {
                System.out.println("No input selected!");
            }
        });
        this.add(commandLine, BorderLayout.SOUTH);
        this.add(startEncoding, BorderLayout.WEST);
        buildDefaultCommandMap();
    }

    private JPanel dropDownPanel() {
        JPanel dropDownPanel = new JPanel();

        JComboBox<String> videoEncoderLibsBox = new JComboBox<>(videoEncoderLibs);
        JComboBox<String> videoPresetsBox = new JComboBox<>(videoPresets[0]);

        JComboBox<String> crfBox = new JComboBox<>();
        crfBox.addItem("Select CRF");
        for (int crf = crf_Min; crf < crf_Max; crf++)
            crfBox.addItem(Integer.toString(crf));

        JComboBox<String> audioEncoderLibsBox = new JComboBox<>(audioEncoderLibs);
        JComboBox<String> audioChannelsBox = new JComboBox<>(audioChannels);
        JComboBox<String> audioBitrateBox = new JComboBox<>(audioBitrate[0]);

        JComboBox<String> encoderThreadsBox = new JComboBox<>();
        encoderThreadsBox.addItem("Select # of Threads (Default = ALL)");
        for (int thread = threads_Min; thread < threads_Max; thread++)
            encoderThreadsBox.addItem(Integer.toString(thread));

        JComboBox<String> outputFormatBox = new JComboBox<>(outputFilesFormats);

        videoEncoderLibsBox.addActionListener(e -> {
            System.out.println(videoEncoderLibsBox.getSelectedItem());
            ffmpegCommandMap.replace("videoEncoder", (String) videoEncoderLibsBox.getSelectedItem());
            if (videoEncoderLibsBox.getSelectedItem().equals("libx264") || videoEncoderLibsBox.getSelectedItem().equals("libx265")) {
                videoPresetsBox.removeAllItems();
                for (int i = 0; i < videoPresets[0].length; i++)
                    videoPresetsBox.addItem(videoPresets[0][i]);

            } else if (videoEncoderLibsBox.getSelectedItem().equals("nvenc_hevc") || videoEncoderLibsBox.getSelectedItem().equals("nvenc_h264")) {
                videoPresetsBox.removeAllItems();
                for (int i = 0; i < videoPresets[1].length; i++)
                    videoPresetsBox.addItem(videoPresets[1][i]);
            }
        });
        videoPresetsBox.addActionListener(e -> {
            System.out.println(videoPresetsBox.getSelectedItem());
            ffmpegCommandMap.replace("videoPreset", (String) videoPresetsBox.getSelectedItem());
        });
        crfBox.addActionListener(e -> {
            System.out.println(crfBox.getSelectedItem());
            ffmpegCommandMap.replace("crf", (String) crfBox.getSelectedItem());
        });
        audioEncoderLibsBox.addActionListener(e -> {
            System.out.println(audioEncoderLibsBox.getSelectedItem());
            ffmpegCommandMap.replace("audioEncoder", (String) audioEncoderLibsBox.getSelectedItem());
            if (audioEncoderLibsBox.getSelectedItem().equals("mp3")) {
                audioBitrateBox.removeAllItems();
                for (int i = 0; i < audioBitrate[0].length; i++)
                    audioBitrateBox.addItem(audioBitrate[0][i]);
            } else if (audioEncoderLibsBox.getSelectedItem().equals("ac3") || audioEncoderLibsBox.getSelectedItem().equals("eac3")) {
                audioBitrateBox.removeAllItems();
                for (int i = 0; i < audioBitrate[1].length; i++)
                    audioBitrateBox.addItem(audioBitrate[1][i]);
            }
        });
        audioChannelsBox.addActionListener(e -> {
            System.out.println(audioChannelsBox.getSelectedItem());
        });
        audioBitrateBox.addActionListener(e -> {
            System.out.println(audioBitrateBox.getSelectedItem());
            ffmpegCommandMap.replace("audioBitrate", (String) audioBitrateBox.getSelectedItem());
        });
        encoderThreadsBox.addActionListener(e -> {
            System.out.println(encoderThreadsBox.getSelectedItem());
        });

        outputFormatBox.addActionListener(e -> {
            System.out.println(outputFormatBox.getSelectedItem());
            ffmpegCommandMap.replace("outputFormat", (String) outputFormatBox.getSelectedItem());
        });

        dropDownPanel.add(videoEncoderLibsBox);
        dropDownPanel.add(videoPresetsBox);
        dropDownPanel.add(crfBox);
        dropDownPanel.add(audioEncoderLibsBox);
        dropDownPanel.add(audioChannelsBox);
        dropDownPanel.add(audioBitrateBox);
        dropDownPanel.add(encoderThreadsBox);
        dropDownPanel.add(outputFormatBox);
        return dropDownPanel;
    }

    private void buildDefaultCommandMap() {
        ffmpegCommandMap.put("videoEncoder", "libx264");
        ffmpegCommandMap.put("videoPreset", "medium");
        ffmpegCommandMap.put("crf", "20");
        ffmpegCommandMap.put("audioEncoder", "eac3");
        ffmpegCommandMap.put("audioBitrate", "640");
        ffmpegCommandMap.put("outputFormat", ".mp4");
    }

    private String[] getFFmpegCommands() {
        String[] commands = new String[inputs.length];
        for (int i = 0; i < inputs.length; i++)
            commands[i] = inputs[i] + "," + "-c:v " + ffmpegCommandMap.get("videoEncoder") + " -preset:v " + ffmpegCommandMap.get("videoPreset") + " -crf " + ffmpegCommandMap.get("crf") + " -c:a " + ffmpegCommandMap.get("audioEncoder") + " -q:a " + ffmpegCommandMap.get("audioBitrate");
        return commands;
    }

    private String[] getFfmpegCommandsWithCLI(String flags) {
        String[] commands = new String[inputs.length];
        for (int i = 0; i < inputs.length; i++)
            commands[i] = inputs[i] + flags;
        return commands;
    }
}
