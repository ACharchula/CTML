package ctml.interpreter.lexer;

import ctml.helpers.Logger;
import ctml.structures.token.PredefinedTokens;
import ctml.structures.token.Token;
import ctml.structures.token.TokenType;

import java.io.IOException;

public class CtmlReader implements Reader {

    private char tokenChar;
    private Lexer lexer;

    public CtmlReader(Lexer lexer) {
        this.lexer = lexer;
    }

    @Override
    public void setNextState() {
        lexer.setLexerState(new HtmlReader(lexer));
    }

    @Override
    public void printState() {
        System.out.println("Reader state - CtmlReader");
    }

    @Override
    public Token read() throws Exception {
        final StringBuilder stringBuilder = new StringBuilder();

        if (tokenChar == 0 || Character.isSpaceChar(tokenChar) || tokenChar == '\r') {
            tokenChar = lexer.getNextChar();

            while (continueReading()) {
                tokenChar = lexer.getNextChar();
            }
        } else if (lexer.getIsEnd()) {
            return new Token("", TokenType.END, lexer.getLineCounter(), lexer.getCharCounter());
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
        while((tokenChar = lexer.getNextChar()) != '\"' && lexer.getInputStream().available() != 0) {
            stringBuilder.append(tokenChar);

            if (tokenChar == '\\')
                stringBuilder.append(tokenChar = lexer.getNextChar());
        }

        stringBuilder.append(tokenChar);
        tokenChar = lexer.getNextChar();

        return new Token(stringBuilder.toString(), TokenType.STRING_CONTENT, lexer.getLineCounter(), lexer.getCharCounter());
    }

    private Token buildOperator(StringBuilder stringBuilder) throws Exception {
        if(isOperatorWithDoubleChar(tokenChar, tokenChar = lexer.getNextChar())) {
            stringBuilder.append(tokenChar);
            tokenChar = lexer.getNextChar();
        }

        TokenType tokenType = PredefinedTokens.OPERATORS.get(stringBuilder.toString());

        if (lexer.getInputStream().available() == 0) {
            lexer.setIsEnd();
        }
        else if (tokenType == TokenType.CTML_END) {
            setNextState();
            return new Token(stringBuilder.toString(), TokenType.CTML_END, lexer.getLineCounter(), lexer.getCharCounter());
        } else if (tokenType == TokenType.COMMENT) {
            while(tokenChar != '\n') {
                tokenChar = lexer.getNextChar();
            }
            return read();
        } else if (tokenType == TokenType.NEXT_LINE)
            return read();

        if(tokenType == null)
            throw Logger.error("Undefined token - " + stringBuilder.toString() +
                    " at line: " + lexer.getLineCounter() + " character: " + lexer.getCharCounter());

        return new Token(stringBuilder.toString(), tokenType, lexer.getLineCounter(), lexer.getCharCounter());
    }

    private Token buildIdOrKeyword(StringBuilder stringBuilder) throws Exception {
        while (Character.isLetter(tokenChar = lexer.getNextChar()) || Character.isDigit(tokenChar) || tokenChar == '_') {
            stringBuilder.append(tokenChar);
        }

        if (PredefinedTokens.KEYWORDS.containsKey(stringBuilder.toString())) {
            return new Token(stringBuilder.toString(), PredefinedTokens.KEYWORDS.get(stringBuilder.toString()),
                    lexer.getLineCounter(), lexer.getCharCounter());
        }

        return new Token(stringBuilder.toString(), TokenType.ID, lexer.getLineCounter(), lexer.getCharCounter());
    }

    private Token buildNumberToken(StringBuilder stringBuilder) throws Exception {

        while (Character.isDigit(tokenChar = lexer.getNextChar()))
            stringBuilder.append(tokenChar);

        if(tokenChar == '.') {
            stringBuilder.append(tokenChar);

            while (Character.isDigit(tokenChar = lexer.getNextChar()))
                stringBuilder.append(tokenChar);

            return new Token(stringBuilder.toString(), TokenType.FLOAT_NUMBER, lexer.getLineCounter(), lexer.getCharCounter());
        }

        return new Token(stringBuilder.toString(), TokenType.NUMBER, lexer.getLineCounter(), lexer.getCharCounter());
    }

    private boolean continueReading() throws IOException {
        return lexer.getInputStream().available() != 0 && Character.isSpaceChar(tokenChar);

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
