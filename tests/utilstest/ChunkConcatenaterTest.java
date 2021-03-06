package utilstest;

import com.utils.ffmpeghandler.ChunkConcatenater;
import com.utils.ffmpeghandler.CommandType;
import com.utils.ffmpeghandler.FFmpegListener;

import java.io.IOException;

public class ChunkConcatenaterTest implements FFmpegListener {
    public ChunkConcatenaterTest(String encoderPath, String chunkFile) throws IOException {
        ChunkConcatenater.ConcatChunks(encoderPath, chunkFile, "./res/test.mp4", this);
    }

    @Override
    public void onEncoderStart(String filename) {
        System.out.println("Started concat of: " + filename);
    }

    @Override
    public void onProgressUpdate(double progress) {
        System.out.println("Progress: " + progress + "%");
    }

    @Override
    public void onJobDone(CommandType type) {
        System.out.println("Done concatenating file!");
    }
}
