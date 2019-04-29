package ctml.interpreter.lexer;

import ctml.helpers.Logger;
import ctml.structures.token.Token;

import java.io.IOException;
import java.io.InputStream;

public class Lexer {

    private static Reader reader = new HtmlReader();
    private static InputStream inputStream;
    private static int lineCounter = 1;
    private static int charCounter = 1;
    private static boolean lastCharWasEnter = false;
    private static boolean isEnd = false;


    public Lexer(InputStream inputStream) {
        Lexer.inputStream = inputStream;
    }

    public static void setLexerState(Reader reader) {
        Lexer.reader = reader;
    }

    static InputStream getInputStream() {
        return inputStream;
    }

    static boolean getIsEnd() {
        return isEnd;
    }

    static void setIsEnd() {
        Lexer.isEnd = true;
    }

    static int getLineCounter() {
        return lineCounter;
    }

    static int getCharCounter() {
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
