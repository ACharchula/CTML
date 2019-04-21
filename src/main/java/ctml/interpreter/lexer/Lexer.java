package ctml.interpreter.lexer;

import ctml.helpers.Logger;
import ctml.structures.data.Token;

import java.io.IOException;
import java.io.InputStream;

public class Lexer {

    public static Reader getReader() {
        return reader;
    }

    private static Reader reader = new HtmlReader();

    public static void setLexerState(Reader reader) {
        Lexer.reader = reader;
    }

    public static InputStream getInputStream() {
        return inputStream;
    }

    private static InputStream inputStream;

    private static int lineCounter = 1;
    private static int charCounter = 1;
    private static boolean lastCharWasEnter = false;

    public static boolean getIsEnd() {
        return isEnd;
    }

    public static void setIsEnd(boolean isEnd) {
        Lexer.isEnd = isEnd;
    }

    private static boolean isEnd = false;


    public Lexer(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public static int getLineCounter() {
        return lineCounter;
    }

    public static int getCharCounter() {
        return charCounter;
    }

    static char getNextChar() throws Exception {
        char nextChar = 0;

        try {
            nextChar = (char) inputStream.read();

            if(nextChar == '\n' && lastCharWasEnter) {
              charCounter = 1;
              lineCounter++;
            } else if (nextChar == '\n') {
                charCounter++;
                lastCharWasEnter = true;
            } else {
                if(lastCharWasEnter) {
                    lineCounter++;
                    charCounter = 1;
                    lastCharWasEnter = false;
                } else {
                    charCounter ++;
                }
            }
        } catch (IOException e) {
            Logger.error("Cannot get next character in input at line " + lineCounter + " and position " + charCounter);
        }

        return nextChar;
    }

    public Token nextToken() throws Exception {
        return reader.read();
    }
}
