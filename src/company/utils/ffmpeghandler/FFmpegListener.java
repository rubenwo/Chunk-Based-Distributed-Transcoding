package company.utils.ffmpeghandler;

public interface FFmpegListener {
    void onEncoderStart(String filename);

    void onProgressUpdate(double progress);

    void onJobDone(CommandType type);
}
