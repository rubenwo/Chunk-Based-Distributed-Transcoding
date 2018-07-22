package company.utils.filehandler;

public class FileReceiver {
    private static FileReceiver instance = null;

    public static FileReceiver getInstance() {
        if (instance == null)
            instance = new FileReceiver();
        return instance;
    }

    public FileReceiver() {

    }
}
