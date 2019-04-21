package ctml.interpreter.lexer;

import ctml.structures.data.Token;
import ctml.structures.data.TokenType;

import static ctml.interpreter.lexer.Lexer.*;

public class HtmlReader implements Reader {

    private char tokenChar;

    @Override
    public void setNextState() {
        setLexerState(new CtmlReader());
    }

    @Override
    public void printState() {
        System.out.println("Reader state - HtmlReader");
    }

    @Override
    public Token read() throws Exception {
        final StringBuilder stringBuilder = new StringBuilder();

        if (tokenChar == 0 && getInputStream().available() == 0) //empty file case
            return new Token("", TokenType.END, getLineCounter(), getCharCounter());
        else if (tokenChar == 0 )
            tokenChar = getNextChar();
        else if (getInputStream().available() == 0 && !getIsEnd()) {
            setIsEnd();
            stringBuilder.append(tokenChar);
            return new Token(stringBuilder.toString(), TokenType.HTML_CONTENT, getLineCounter(), getCharCounter());
        } else if (getIsEnd()) {
            return new Token("", TokenType.END, getLineCounter(), getCharCounter());
        }

        stringBuilder.append(tokenChar);

        int line = getLineCounter();
        int character = getCharCounter();

        if (tokenChar == '<' ) {
            if((tokenChar = getNextChar()) == '?') {
                setNextState();
                stringBuilder.append(tokenChar);
                tokenChar = getNextChar();
                return new Token(stringBuilder.toString(), TokenType.CTML_START, getLineCounter(), getCharCounter());
            } else {
                return new Token(stringBuilder.toString(), TokenType.HTML_CONTENT, line, character);
            }
        }

        tokenChar = getNextChar();
        return new Token(stringBuilder.toString(), TokenType.HTML_CONTENT, line, character);
    }


}
