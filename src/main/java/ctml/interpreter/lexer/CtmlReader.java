package ctml.interpreter.lexer;

import ctml.structures.data.PredefinedTokens;
import ctml.structures.data.Token;
import ctml.structures.data.TokenType;

import java.io.IOException;

import static ctml.interpreter.lexer.Lexer.*;

public class CtmlReader implements Reader {

    private char tokenChar;

    @Override
    public void setNextState() {
        setLexerState(new HtmlReader());
    }

    @Override
    public void printState() {
        System.out.println("Reader state - CtmlReader");
    }

    @Override
    public Token read() throws Exception {
        final StringBuilder stringBuilder = new StringBuilder();

        if (tokenChar == 0 || Character.isSpaceChar(tokenChar) || tokenChar == '\r') {
            tokenChar = getNextChar();

            while (continueReading()) {
                tokenChar = getNextChar();
            }
        } else if (getIsEnd()) {
            return new Token("", TokenType.END, getLineCounter(), getCharCounter());
        }

        stringBuilder.append(tokenChar);

        if (Character.isDigit(tokenChar)) {
            return buildNumberToken(stringBuilder);
        } else if(tokenChar == '\"'){
            return buildString(stringBuilder);
        } else if (Character.isLetter(tokenChar)) {
            return buildIdOrKeyword(stringBuilder);
        } else {
            return buildOperator(stringBuilder);
        }
    }

    private Token buildString(StringBuilder stringBuilder) throws Exception {
        while((tokenChar = getNextChar()) != '\"' && getInputStream().available() != 0) {
            stringBuilder.append(tokenChar);

            if (tokenChar == '\\')
                stringBuilder.append(tokenChar = getNextChar());
        }

        stringBuilder.append(tokenChar);
        tokenChar = getNextChar();

        return new Token(stringBuilder.toString(), TokenType.STRING_CONTENT, getLineCounter(), getCharCounter());
    }

    private Token buildOperator(StringBuilder stringBuilder) throws Exception {
        if(isOperatorWithDoubleChar(tokenChar, tokenChar = getNextChar())) {
            stringBuilder.append(tokenChar);
            tokenChar = getNextChar();
        }

        TokenType tokenType = PredefinedTokens.OPERATORS.get(stringBuilder.toString());

        if (getInputStream().available() == 0) {
             setIsEnd(true);
            return new Token(stringBuilder.toString(), tokenType, getLineCounter(), getCharCounter());
        }
        else if (tokenType == TokenType.CTML_END) {
            setNextState();
            return new Token(stringBuilder.toString(), TokenType.CTML_END, getLineCounter(), getCharCounter());
        } else if (tokenType == TokenType.COMMENT) {
            while(tokenChar != '\n') {
                tokenChar = getNextChar();
            }
            return read();
        } else if (tokenType == TokenType.NEXT_LINE)
            return read();
        else if (tokenType != null)

            return new Token(stringBuilder.toString(), tokenType, getLineCounter(), getCharCounter());

        return new Token(stringBuilder.toString(), TokenType.UNDEFINED, getLineCounter(), getCharCounter());

    }

    private Token buildIdOrKeyword(StringBuilder stringBuilder) throws Exception {
        while (Character.isLetter(tokenChar = getNextChar()) || Character.isDigit(tokenChar) || tokenChar == '_') {
            stringBuilder.append(tokenChar);
        }

        if (PredefinedTokens.KEYWORDS.containsKey(stringBuilder.toString())) {
            return new Token(stringBuilder.toString(), PredefinedTokens.KEYWORDS.get(stringBuilder.toString()), getLineCounter(), getCharCounter());
        }

        return new Token(stringBuilder.toString(), TokenType.ID, getLineCounter(), getCharCounter());
    }

    private Token buildNumberToken(StringBuilder stringBuilder) throws Exception {

        while (Character.isDigit(tokenChar = getNextChar()))
            stringBuilder.append(tokenChar);

        return new Token(stringBuilder.toString(), TokenType.NUMBER, getLineCounter(), getCharCounter());
    }

    private boolean continueReading() throws IOException {
        return getInputStream().available() != 0 && Character.isSpaceChar(tokenChar);

    }

    private boolean isOperatorWithDoubleChar(char firstChar, char secondChar) {
        return (
                (firstChar == '=' && secondChar == '=') ||
                        (firstChar == '>' && secondChar == '=') ||
                        (firstChar == '<' && secondChar == '=') ||
                        (firstChar == '&' && secondChar == '&') ||
                        (firstChar == '|' && secondChar == '|') ||
                        (firstChar == '!' && secondChar == '=') ||
                        (firstChar == '?' && secondChar == '>') ||
                        (firstChar == '/' && secondChar == '/')
        );
    }
}
