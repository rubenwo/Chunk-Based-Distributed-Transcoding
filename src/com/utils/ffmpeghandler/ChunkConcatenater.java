package com.utils.ffmpeghandler;

public class ChunkConcatenater {
    public static void ConcatChunks(String encoderPath, String filePath, String output, FFmpegListener ffmpegListener) {
        String concatCommand = " -f concat -safe 0";
        String command = "-c copy";
        new Thread(new FFmpegHandler(encoderPath, concatCommand, filePath, command, output, ffmpegListener, CommandType.CONCATENATE)).start();
    }
}
