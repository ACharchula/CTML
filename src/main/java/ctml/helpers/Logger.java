package ctml.helpers;

public class Logger {

    public static void info(String content) {
        System.out.println("[INFO] " + content);
    }

    public static void error(String content) throws Exception {
        throw new Exception("[ERROR] " + content);
    }

    public static void warn(String content) {
        System.out.println("[WARN] " + content);
    }

}
