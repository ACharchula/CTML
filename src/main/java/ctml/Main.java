package ctml;

import ctml.helpers.FileHelper;
import ctml.helpers.Logger;
import ctml.interpreter.Interpreter;

import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            Logger.error("Needed arguments has been not provided!");
            return;
        }

        Logger.info("File input URL: " + args[0]);
        Logger.info("File output URL: " + args[1]);

        final InputStream inputStream = FileHelper.getInputStreamFromHtmlFile(args[0]);
        Interpreter.run(inputStream, args[1]);
    }
}
