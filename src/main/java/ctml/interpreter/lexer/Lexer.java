package ctml.interpreter.lexer;

import ctml.structures.token.Token;

import java.io.IOException;
import java.io.InputStream;

public class Lexer {

    private Reader reader = new HtmlReader(this);
    private InputStream inputStream;
    private int lineCounter = 1;
    private int charCounter = 1;
    private boolean lastCharWasEnter = false;
    private boolean isEnd = false;


    public Lexer(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setLexerState(Reader reader) {
        this.reader = reader;
    }

    InputStream getInputStream() {
        return inputStream;
    }

    boolean getIsEnd() {
        return isEnd;
    }

    void setIsEnd() {
        this.isEnd = true;
    }

    int getLineCounter() {
        return lineCounter;
    }

    int getCharCounter() {
        return charCounter;
    }

    char getNextChar() throws Exception {
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
            throw new Exception("Cannot get next character in input at line " + lineCounter + " and position " + charCounter);
        }

        return nextChar;
    }

    public Token nextToken() throws Exception {
        return reader.read();
    }
}
