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
    private boolean readCTML = false;
    private boolean lastCharWasEnter = false;
    private boolean isEnd = false;


    Lexer(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int getLineCounter() {
        return lineCounter;
    }

    public int getCharCounter() {
        return charCounter;
    }

    private char getNextChar() throws Exception {
        char nextChar = 0;

        try {
            nextChar = (char) inputStream.read();

            if (nextChar == '\n') {
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
        Token token;

        if (!readCTML)
            token = htmlContent();
        else
            token = ctmlContent();

        return token;
    }

    private Token htmlContent() throws Exception {
        final StringBuilder stringBuilder = new StringBuilder();

        if (tokenChar == 0 && inputStream.available() == 0) //empty file case
            return new Token("", TokenType.END);
        else if (tokenChar == 0 )
            tokenChar = getNextChar();
        else if (inputStream.available() == 0 && !isEnd) {
            isEnd = true;
            stringBuilder.append(tokenChar);
            return new Token(stringBuilder.toString(), TokenType.HTML_CONTENT);
        } else if (isEnd) {
            return new Token("", TokenType.END);
        }

        stringBuilder.append(tokenChar);

        if (tokenChar == '<' ) {
            if((tokenChar = getNextChar()) == '?') {
                readCTML = true;
                stringBuilder.append(tokenChar);
                tokenChar = getNextChar();
                return new Token(stringBuilder.toString(), TokenType.CTML_START);
            } else {
                return new Token(stringBuilder.toString(), TokenType.HTML_CONTENT);
            }
        }

        tokenChar = getNextChar();
        return new Token(stringBuilder.toString(), TokenType.HTML_CONTENT);
    }

    private Token ctmlContent() throws Exception {
        final StringBuilder stringBuilder = new StringBuilder();

        if (tokenChar == 0 || Character.isSpaceChar(tokenChar) || tokenChar == '\r') {
            tokenChar = getNextChar();

            while (continueReading()) {
                tokenChar = getNextChar();
            }
        } else if (isEnd) {
            return new Token("", TokenType.END);
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
        while((tokenChar = getNextChar()) != '\"' && inputStream.available() != 0) {
            stringBuilder.append(tokenChar);

            if (tokenChar == '\\')
                stringBuilder.append(tokenChar = getNextChar());
        }

        stringBuilder.append(tokenChar);
        tokenChar = getNextChar();

        return new Token(stringBuilder.toString(), TokenType.STRING_CONTENT);
    }

    private Token buildOperator(StringBuilder stringBuilder) throws Exception {
        if(isOperatorWithDoubleChar(tokenChar, tokenChar = getNextChar())) {
            stringBuilder.append(tokenChar);
            tokenChar = getNextChar();
        }

        TokenType tokenType = PredefinedTokens.OPERATORS.get(stringBuilder.toString());

        if (inputStream.available() == 0) {
            isEnd = true;
            return new Token(stringBuilder.toString(), tokenType);
        }
        else if (tokenType == TokenType.CTML_END) {
            readCTML = false;
            return new Token(stringBuilder.toString(), TokenType.CTML_END);
        } else if (tokenType == TokenType.COMMENT) {
            while(tokenChar != '\n') {
                tokenChar = getNextChar();
            }
            return nextToken();
        } else if (tokenType == TokenType.NEXT_LINE)
            return nextToken();
        else if (tokenType != null)

            return new Token(stringBuilder.toString(), tokenType);

        return new Token(stringBuilder.toString(), TokenType.UNDEFINED);

    }

    private Token buildIdOrKeyword(StringBuilder stringBuilder) throws Exception {
        while (Character.isLetter(tokenChar = getNextChar()) || Character.isDigit(tokenChar) || tokenChar == '_') {
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
                (firstChar == '!' && secondChar == '=') ||
                (firstChar == '?' && secondChar == '>') ||
                (firstChar == '/' && secondChar == '/')
                );
    }
}
