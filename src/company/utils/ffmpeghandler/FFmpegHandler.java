package utils.ffmpeghandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;

public class FFmpegHandler implements Runnable {
    private Process ffmpeg;
    private FFmpegListener listener;
    private ArrayList<String> commands = new ArrayList<>();

    public FFmpegHandler(String encoderPath, String input, String command, String output, FFmpegListener listener) {
        this.listener = listener;

        commands.add(encoderPath);
        commands.add("-i");
        commands.add(input);
        String[] commandSplit = command.trim().split("\\s++");
        Collections.addAll(commands, commandSplit);
        commands.add(output);

        //listener.onEncoderStart(input.substring(input.lastIndexOf("/")));

        System.out.println(encoderPath);
        System.out.println(input);

        try {
            ffmpeg = new ProcessBuilder(commands).redirectErrorStream(true).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(ffmpeg.getInputStream(), Charset.defaultCharset()));

        try {
            String line;
            int durationMillis = 0;

            while ((line = processOutputReader.readLine()) != null) {
                if (line.contains("Duration")) {
                    String[] durationString = line.split(",")[0].substring(12, 23).split(":");
                    durationMillis += Integer.valueOf(durationString[0]) * 3600000;
                    durationMillis += Integer.valueOf(durationString[1]) * 60000;
                    durationMillis += Integer.valueOf(durationString[2].substring(0, 2)) * 1000;
                    durationMillis += Integer.valueOf(durationString[2].substring(3, 5));
                }
                if (line.contains("frame") && line.contains("time")) {
                    String[] currentTime = line.split("=")[5].split(":");

                    int currentTimeMillis = 0;
                    currentTimeMillis += Integer.valueOf(currentTime[0]) * 3600000;
                    currentTimeMillis += Integer.valueOf(currentTime[1]) * 60000;
                    currentTimeMillis += Integer.valueOf(currentTime[2].substring(0, 2)) * 1000;
                    currentTimeMillis += Integer.valueOf(currentTime[2].substring(3, 5));

                    double percent = (double) currentTimeMillis / durationMillis * 100;
                    percent = Math.round(percent * 100.0) / 100.0;
                    listener.onProgressUpdate(percent);
                }
            }
            ffmpeg.waitFor();
            listener.onJobDone();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
