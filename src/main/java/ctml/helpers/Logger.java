package ctml.helpers;

public class Logger {

    public static void info(String content) {
        System.out.println("[INFO] " + content);
    }

    public static Exception error(String content) {
        return new Exception("[ERROR] " + content);
    }

    public static void warn(String content) {
        System.out.println("[WARN] " + content);
    }

}
