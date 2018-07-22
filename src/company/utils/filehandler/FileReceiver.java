package company.utils.filehandler;

public class FileReceiver implements Runnable {
    private FileReceiverListener receiverListener;
    private String fileName;
    private String tempDir;

    public FileReceiver(FileReceiverListener receiverListener, String fileName, String tempDir) {
        this.receiverListener = receiverListener;
    }

    @Override
    public void run() {

    }
}
