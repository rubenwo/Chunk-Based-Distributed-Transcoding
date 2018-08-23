package com.utils.ffmpeghandler;

import com.Constants;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ChunkGenerater {
    public static void GenerateChunks(String encoderPath, String input, FFmpegListener ffmpegListener) {
        String command = "-map 0 -c copy -f segment -segment_time " + Constants.CHUNK_SIZE;
        String output = encoderPath.substring(0, input.lastIndexOf("/")) + "/chunks/chunk_%03d" + input.substring(input.lastIndexOf("."));
        new Thread(new FFmpegHandler(encoderPath, input, command, output, ffmpegListener, CommandType.GENERATE_CHUNKS)).start();
    }

    public static String GenerateConcatFile(String tempdir) throws IOException {
        File chunksFile = new File(tempdir + "/chunks");
        File concatFile = new File(chunksFile.getAbsolutePath() + "/chunks.txt");
        File[] listOfChunks = chunksFile.listFiles();
        assert (listOfChunks != null);

        PrintWriter printWriter = new PrintWriter(concatFile);
        for (File chunk : listOfChunks)
            printWriter.println("file \'" + chunk.getAbsolutePath().replace("\\", "/").replaceAll("chunks", "transcoded") + "\'");
        printWriter.close();

        return concatFile.getAbsolutePath();
    }
}
