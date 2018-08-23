package com.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TempDirCreator {
    public static String initializeTemporaryDirectory(String uniqueID, OperatingSystem operatingSystem) throws IOException {
        String workingDirectory;

        Path tempDir = Files.createTempDirectory(uniqueID);
        workingDirectory = tempDir.toString() + "/";
        Files.createDirectory(Paths.get(workingDirectory + "chunks"));
        Files.createDirectory(Paths.get(workingDirectory + "transcoded"));

        new TempDirCreator(workingDirectory, operatingSystem);

        return workingDirectory;
    }

    private TempDirCreator(String tempDir, OperatingSystem operatingSystem) throws IOException {
        String tempEncoderDir = tempDir + "ffmpeg";
        long start = System.currentTimeMillis();

        InputStream inputStream = getClass().getResourceAsStream(OperatingSystem.getEncoderPath(operatingSystem));
        Path tempEncoderPath = Paths.get(tempEncoderDir);
        Files.copy(inputStream, tempEncoderPath);
        inputStream.close();
        new File(tempEncoderDir).setExecutable(true);

        long end = System.currentTimeMillis();
        System.out.println("Extracted ffmpeg in: " + (end - start) + " milliseconds.");
    }

}
