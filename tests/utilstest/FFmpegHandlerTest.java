package utilstest;

import company.utils.OperatingSystem;
import company.utils.TempDirCreator;
import company.utils.ffmpeghandler.CommandType;
import company.utils.ffmpeghandler.FFmpegHandler;
import company.utils.ffmpeghandler.FFmpegListener;
import company.utils.filehandler.FilesHandler;

import java.io.IOException;
import java.util.UUID;

public class FFmpegHandlerTest implements FFmpegListener {

    public FFmpegHandlerTest() {
        OperatingSystem operatingSystem = OperatingSystem.detectOperatingSystem();
        String uniqueId = UUID.randomUUID().toString();
        String tempDir = null;

        try {
            tempDir = TempDirCreator.initializeTemporaryDirectory(uniqueId, operatingSystem);
        } catch (IOException e) {
            e.printStackTrace();
        }


        assert (tempDir != null);

        try {
            FilesHandler.LocalCopy("./res/test_files/big_buck_bunny.mp4", tempDir + "big_buck_bunny.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String input = tempDir + "big_buck_bunny.mp4";
        String command = "-c:v libx264 -preset:v ultrafast -c:a copy";
        String output = tempDir + "transcoded.mkv";
        new Thread(new FFmpegHandler(tempDir + "ffmpeg", input, command, output, this, CommandType.TRANSCODE)).start();
    }

    @Override
    public void onEncoderStart(String filename) {
        System.out.println("Started transcoding: " + filename);
    }

    @Override
    public void onProgressUpdate(double progress) {
        System.out.println("Progress: " + progress + "%");
    }

    @Override
    public void onJobDone(CommandType type) {
        System.out.println("Finished transcoding!");
    }

    public static void main(String[] args) {
        new FFmpegHandlerTest();
    }
}
