package ctml.interpreter;

import ctml.helpers.Logger;
import ctml.structures.data.PredefinedTokens;
import ctml.structures.data.Token;
import ctml.structures.data.TokenType;

import java.io.IOException;
import java.io.InputStream;

class Lexer {

    private InputStream inputStream;
    private char tokenChar;
    private int lineCounter = 1;
    private int charCounter = 1;


    Lexer(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private char getNextChar() throws Exception {
        char nextChar = 0;

        try {
            nextChar = (char) inputStream.read();

            if (nextChar == '\n') {
                lineCounter++;
                charCounter = 1;
                return getNextChar();
            } else {
                charCounter++;
            }
        } catch (IOException e) {
            Logger.error("Cannot get next character in input at line " + lineCounter + " and position " + charCounter);
        }

        return nextChar;
    }

    public Token nextToken() throws Exception {
        final StringBuilder stringBuilder = new StringBuilder();
        Token token = null;

        if (tokenChar == 0 || Character.isSpaceChar(tokenChar) || tokenChar == '\r' ) {
            tokenChar = getNextChar();

            while (continueReading()) {
                tokenChar = getNextChar();
            }
        }

        if (Character.isSpaceChar(tokenChar))
            return new Token(stringBuilder.toString(), TokenType.UNDEFINED);

        stringBuilder.append(tokenChar);

        if(Character.isDigit(tokenChar)) {
            token = buildNumberToken(stringBuilder);
        } else if (Character.isLetter(tokenChar)) {
            token = buildIdOrKeyword(stringBuilder);
        } else {
            token = buildOperator(stringBuilder);
        }

        return token;


    }

    private Token buildOperator(StringBuilder stringBuilder) throws Exception {
        if(isOperatorWithDoubleChar(tokenChar, tokenChar = getNextChar())) {
            stringBuilder.append(tokenChar);
            tokenChar = getNextChar();
        }

        TokenType tokenType = PredefinedTokens.OPERATORS.get(stringBuilder.toString());

        if (inputStream.available() == 0)
            return new Token(stringBuilder.toString(), TokenType.END);
        else if (tokenType != null)
            return new Token(stringBuilder.toString(), tokenType);

        return new Token(stringBuilder.toString(), TokenType.UNDEFINED);

    }

    private Token buildIdOrKeyword(StringBuilder stringBuilder) throws Exception {
        while (Character.isLetter(tokenChar = getNextChar()) || Character.isDigit(tokenChar)) {
            stringBuilder.append(tokenChar);
        }

        if (PredefinedTokens.KEYWORDS.containsKey(stringBuilder.toString())) {
            return new Token(stringBuilder.toString(), PredefinedTokens.KEYWORDS.get(stringBuilder.toString()));
        }

        return new Token(stringBuilder.toString(), TokenType.ID);
    }

    private Token buildNumberToken(StringBuilder stringBuilder) throws Exception {

        while (Character.isDigit(tokenChar = getNextChar()))
            stringBuilder.append(tokenChar);

        return new Token(stringBuilder.toString(), TokenType.NUMBER);
    }

    private boolean continueReading() throws IOException {
        return inputStream.available() != 0 && Character.isSpaceChar(tokenChar);

    }

    private boolean isOperatorWithDoubleChar(char firstChar, char secondChar) {
        return (
                (firstChar == '=' && secondChar == '=') ||
                (firstChar == '>' && secondChar == '=') ||
                (firstChar == '<' && secondChar == '=') ||
                (firstChar == '&' && secondChar == '&') ||
                (firstChar == '|' && secondChar == '|') ||
                (firstChar == '!' && secondChar == '=')
                );
    }
}
