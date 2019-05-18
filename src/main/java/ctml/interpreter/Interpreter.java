package ctml.interpreter;

import ctml.interpreter.parser.Parser;
import ctml.interpreter.parser.Program;


import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;

public class Interpreter {

    public static PrintWriter writer;

    public static void run(final InputStream inputStream, String outputURL) throws Exception {
        new PrintWriter(outputURL).close(); //clear output file

        FileWriter fileWriter = new FileWriter(outputURL, true);
        writer = new PrintWriter(fileWriter);

        Parser parser = new Parser(inputStream);
        Program program = parser.parseProgram();
        program.execute();
        writer.close();
    }
}
