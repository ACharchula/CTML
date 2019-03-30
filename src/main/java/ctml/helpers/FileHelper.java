package ctml.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileHelper {

    public static InputStream getInputStreamFromHtmlFile(final String filePath) throws Exception {
        File file = new File(filePath);
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Logger.error("Input file not loaded");
            return null;
        }
    }

}
