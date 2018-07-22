package company.utils;

public class ExtensionGenerator {
    public static String GenerateExtension(String input) {
        return input.substring(input.lastIndexOf(".") - 1);
    }
}
