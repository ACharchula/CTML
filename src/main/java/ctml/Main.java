package ctml;

import ctml.helpers.FileHelper;
import ctml.interpreter.Interpreter;

import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new Exception("Required arguments has been not provided!");
        }

        System.out.println("[INFO] File input URL: " + args[0]);
        System.out.println("[INFO] File output URL: " + args[1]);

        final InputStream inputStream = FileHelper.getInputStreamFromHtmlFile(args[0]);
        Interpreter.run(inputStream, args[1]);
    }
}
