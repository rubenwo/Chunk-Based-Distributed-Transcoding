package utilstest;

import company.utils.OperatingSystem;

public class OperatingSystemTest {
    public static void main(String[] args) {
        OperatingSystem operatingSystem = OperatingSystem.detectOperatingSystem();
        System.out.println(operatingSystem);
        String encoderPath = OperatingSystem.getEncoderPath(operatingSystem);
        System.out.println(encoderPath);
    }
}
