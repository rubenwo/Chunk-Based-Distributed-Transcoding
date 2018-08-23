package guitests;

import com.encoderclient.GUI.EncoderFrame;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class EncoderGUITest {
    public static void main(String[] args) throws UnknownHostException {
        EncoderFrame encoderFrame = new EncoderFrame(InetAddress.getLocalHost().getHostAddress(), UUID.randomUUID().toString());

        encoderFrame.setCurrentJobFileName("test_chunk_000.mp4");
        double progress = 0;
        while (true) {
            try {
                Thread.sleep(10);
                encoderFrame.updateCurrentJob(progress);
                progress += 0.5;
                if (progress == 100.0)
                    break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        encoderFrame.resetFrame();
    }
}
