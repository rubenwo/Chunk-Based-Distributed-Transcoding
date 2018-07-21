package company.masterclient;

import java.util.HashMap;

public class MasterClient {
    public static HashMap<String, Double> encoderProgress = new HashMap<>();

    public MasterClient() {
    }

    public HashMap<String, Double> getEncoderProgress() {
        return encoderProgress;
    }
}
