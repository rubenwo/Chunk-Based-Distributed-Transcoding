package com.masterclient.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class FFmpegOptionPanel extends JPanel {
    private HashMap<String, String> ffmpegCommandMap = new HashMap<>();

    private String[] videoEncoderLibs = {"Select a Video Encoder", "libx264", "libx265"};
    private String[] videoPresets = {"Select a Video Preset", "ultrafast", "superfast", "veryfast", "faster", "fast", "medium", "slow", "slower", "veryslow", "placebo"};
    private String[] videoTuner = {"Select a Tuning Preset", "film", "animation", "grain", "stillimage", "fastdecode", "zerolatency"};

    private String[] audioEncoderLibs = {"Select an Audio Encoder (Default = Copy)", "mp3", "aac", "ac3", "eac3", "opus", "vorbis"};
    private HashMap<String, String[]> audioBitrateMap = new HashMap<>();

    private String[] outputFilesFormats = {"Select a output File Format", ".mkv", ".mp4", ".mov", ".mpg"};


    private String[] inputs;

    public FFmpegOptionPanel(MasterFrame masterFrame) {
        generateDefaultSettings();
        generateAudioBitrates();

        this.setLayout(new BorderLayout());
        this.add(dropDownPanel(), BorderLayout.CENTER);

        JTextField commandLine = new JTextField();
        JButton startEncoding = new JButton("Start Encoding");
        startEncoding.addActionListener(e -> {
            inputs = masterFrame.getInputs();
            if (inputs != null) {
                String command;
                if (!commandLine.getText().isEmpty()) {
                    command = commandLine.getText();
                } else {
                    command = generateCommand();
                }
                int reply = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to transcode these files?",
                        "Are you sure?",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        0,
                        null
                );
                if (reply == JOptionPane.YES_OPTION) {
                    System.out.println("Accepted!");
                    System.out.println(command);
                } else if (reply == JOptionPane.NO_OPTION) {
                    System.out.println("Declined!");
                } else {
                    System.out.println("Cancelled!");
                }
            } else {
                System.out.println("No input selected!");
                JOptionPane.showMessageDialog(null,
                        "You don't have any inputs selected!\nPlease select the directory you wish to transcode.",
                        "No input selected!",
                        0,
                        null);
            }
        });
        this.add(commandLine, BorderLayout.SOUTH);
        this.add(startEncoding, BorderLayout.WEST);
    }

    private JPanel dropDownPanel() {
        JPanel dropDownPanel = new JPanel();

        JComboBox<String> videoEncoderLibsBox = new JComboBox<>(videoEncoderLibs);
        JComboBox<String> videoPresetsBox = new JComboBox<>(videoPresets);
        JComboBox<String> videoTunerBox = new JComboBox<>(videoTuner);

        JComboBox<String> crfBox = new JComboBox<>();
        crfBox.addItem("Select CRF (Default = Encoder Default");
        for (int crf = 0; crf < 30; crf++)
            crfBox.addItem(Integer.toString(crf));

        JComboBox<String> audioEncoderLibsBox = new JComboBox<>(audioEncoderLibs);
        JComboBox<String> audioBitrateBox = new JComboBox<>(audioBitrateMap.get("copy"));

        JComboBox<String> outputFormatBox = new JComboBox<>(outputFilesFormats);

        videoEncoderLibsBox.addActionListener(e -> {
            System.out.println(videoEncoderLibsBox.getSelectedItem());
            ffmpegCommandMap.replace("videoEncoder", (String) videoEncoderLibsBox.getSelectedItem());
        });
        videoPresetsBox.addActionListener(e -> {
            System.out.println(videoPresetsBox.getSelectedItem());
            ffmpegCommandMap.replace("videoPreset", (String) videoPresetsBox.getSelectedItem());
        });
        videoTunerBox.addActionListener(e -> {
            System.out.println(videoTunerBox.getSelectedItem());
            ffmpegCommandMap.replace("videoTuner", (String) videoTunerBox.getSelectedItem());
        });
        crfBox.addActionListener(e -> {
            System.out.println(crfBox.getSelectedItem());
            ffmpegCommandMap.replace("crf", (String) crfBox.getSelectedItem());
        });

        audioEncoderLibsBox.addActionListener(e -> {
            String audioEnc = audioEncoderLibsBox.getSelectedItem().toString();
            switch (audioEnc) {
                case "Select an Audio Encoder (Default = Copy)":
                    audioBitrateBox.removeAllItems();
                    audioBitrateBox.addItem("Copy");
                    break;
                case "mp3":
                    audioBitrateBox.removeAllItems();
                    audioBitrateBox.setModel(new DefaultComboBoxModel<>(audioBitrateMap.get("mp3")));
                    break;
                case "aac":
                    audioBitrateBox.removeAllItems();
                    audioBitrateBox.setModel(new DefaultComboBoxModel<>(audioBitrateMap.get("aac")));
                    break;
                case "ac3":
                    audioBitrateBox.removeAllItems();
                    audioBitrateBox.setModel(new DefaultComboBoxModel<>(audioBitrateMap.get("ac3")));
                    break;
                case "eac3":
                    audioBitrateBox.removeAllItems();
                    audioBitrateBox.setModel(new DefaultComboBoxModel<>(audioBitrateMap.get("eac3")));
                    break;
                case "opus":
                    audioBitrateBox.removeAllItems();
                    audioBitrateBox.setModel(new DefaultComboBoxModel<>(audioBitrateMap.get("opus")));
                    break;
                case "vorbis":
                    audioBitrateBox.removeAllItems();
                    audioBitrateBox.setModel(new DefaultComboBoxModel<>(audioBitrateMap.get("vorbis")));
                    break;
            }
        });

        audioBitrateBox.addActionListener(e -> {
            System.out.println(audioBitrateBox.getSelectedItem());
            ffmpegCommandMap.replace("audioBitrate", (String) audioBitrateBox.getSelectedItem());
        });

        outputFormatBox.addActionListener(e -> {
            System.out.println(outputFormatBox.getSelectedItem());
            ffmpegCommandMap.replace("outputFormat", (String) outputFormatBox.getSelectedItem());
        });

        dropDownPanel.add(videoEncoderLibsBox);
        dropDownPanel.add(videoPresetsBox);
        dropDownPanel.add(videoTunerBox);
        dropDownPanel.add(crfBox);
        dropDownPanel.add(audioEncoderLibsBox);
        dropDownPanel.add(audioBitrateBox);
        dropDownPanel.add(outputFormatBox);
        return dropDownPanel;
    }

    private void generateDefaultSettings() {
        ffmpegCommandMap.put("videoEncoder", "libx264");
        ffmpegCommandMap.put("videoPreset", "medium");
        ffmpegCommandMap.put("videoTuner", "film");
        ffmpegCommandMap.put("crf", "-1");
        ffmpegCommandMap.put("audioEncoder", "copy");
        ffmpegCommandMap.put("audioBitrate", "copy");
        ffmpegCommandMap.put("outputFormat", ".mp4");
    }

    private void generateAudioBitrates() {
        audioBitrateMap.put("copy", new String[]{"copy"});
        audioBitrateMap.put("mp3", new String[]{"Select Audio Bitrate", "96k", "128k", "160k", "192k", "256k", "320k"});
        audioBitrateMap.put("aac", new String[]{"Select Audio Bitrate", "96k", "128k", "160k", "192k", "256k", "320k"});
        audioBitrateMap.put("ac3", new String[]{"Select Audio Bitrate", "96k", "128k", "160k", "192k", "256k", "320k"});
        audioBitrateMap.put("eac3", new String[]{"Select Audio Bitrate", "96k", "128k", "160k", "192k", "256k", "320k"});
        audioBitrateMap.put("opus", new String[]{"Select Audio Bitrate", "96k", "128k", "160k", "192k", "256k", "320k"});
        audioBitrateMap.put("vorbis", new String[]{"Select Audio Bitrate", "96k", "128k", "160k", "192k", "256k", "320k"});
    }

    private String generateCommand() {
        if (!ffmpegCommandMap.get("crf").equals("-1") && !ffmpegCommandMap.get("audioEncoder").equals("copy"))
            return "-c:v " + ffmpegCommandMap.get("videoEncoder") +
                    " -preset:v " + ffmpegCommandMap.get("videoPreset") +
                    " -tune " + ffmpegCommandMap.get("videoTuner") +
                    " -crf " + ffmpegCommandMap.get("crf") +
                    " -c:a " + ffmpegCommandMap.get("audioEncoder") +
                    " -b:a " + ffmpegCommandMap.get("audioBitrate");
        else if (ffmpegCommandMap.get("audioEncoder").equals("copy") && ffmpegCommandMap.get("crf").equals("-1"))
            return "-c:v " + ffmpegCommandMap.get("videoEncoder") +
                    " -preset:v " + ffmpegCommandMap.get("videoPreset") +
                    " -tune " + ffmpegCommandMap.get("videoTuner") +
                    " -c:a " + ffmpegCommandMap.get("audioEncoder");
        else if (ffmpegCommandMap.get("audioEncoder").equals("copy"))
            return "-c:v " + ffmpegCommandMap.get("videoEncoder") +
                    " -preset:v " + ffmpegCommandMap.get("videoPreset") +
                    " -tune " + ffmpegCommandMap.get("videoTuner") +
                    " -crf " + ffmpegCommandMap.get("crf") +
                    " -c:a " + ffmpegCommandMap.get("audioEncoder");
        else {
            return "-c:v " + ffmpegCommandMap.get("videoEncoder") +
                    " -preset:v " + ffmpegCommandMap.get("videoPreset") +
                    " -tune " + ffmpegCommandMap.get("videoTuner") +
                    " -c:a " + ffmpegCommandMap.get("audioEncoder") +
                    " -b:a " + ffmpegCommandMap.get("audioBitrate");
        }
    }
}
