package company.utils.filehandler;

import company.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FilesHandler {
    public static void LocalCopy(String src, String dest) throws IOException {
        long start = System.currentTimeMillis();

        FileChannel from = (FileChannel.open(Paths.get(src), StandardOpenOption.READ));
        FileChannel to = (FileChannel.open(Paths.get(dest), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE));
        transfer(from, to, 0l, from.size());

        long end = System.currentTimeMillis();
        System.out.println("Transferred file in: " + (end - start) + " milliseconds.");
    }

    private static void transfer(FileChannel from, FileChannel to, long position, long size) throws IOException {
        while (position < size)
            position += from.transferTo(position, Constants.TRANSFER_MAX_SIZE, to);
    }

    public static void DoTempDirCleanUp(String tempdir) throws IOException {
        File temp = new File(tempdir);
        File[] old_files = temp.listFiles();

        assert (old_files != null);
        for (File old_file : old_files) {
            if (!old_file.getName().contains("ffmpeg"))
                Files.delete(Paths.get(old_file.getAbsolutePath()));
        }
    }

    public static void DoSingleFileCleanUp(String filePath) throws IOException {
        Files.delete(Paths.get(filePath));
    }
}
