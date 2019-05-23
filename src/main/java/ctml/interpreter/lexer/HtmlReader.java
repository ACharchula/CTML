package ctml.interpreter.lexer;

import ctml.structures.token.Token;
import ctml.structures.token.TokenType;

import static ctml.interpreter.lexer.Lexer.*;

public class HtmlReader implements Reader {

    private char tokenChar;
    private Lexer lexer;

    public HtmlReader(Lexer lexer) {
        this.lexer = lexer;
    }

    @Override
    public void setNextState() {
        lexer.setLexerState(new CtmlReader(lexer));
    }

    @Override
    public Token read() throws Exception {
        final StringBuilder stringBuilder = new StringBuilder();

        if (tokenChar == 0 && lexer.getInputStream().available() == 0) //empty file case
            return new Token("", TokenType.END, lexer.getLineCounter(), lexer.getCharCounter());
        else if (tokenChar == 0 )
            tokenChar = lexer.getNextChar();
        else if (lexer.getInputStream().available() == 0 && !lexer.getIsEnd()) {
            lexer.setIsEnd();
            stringBuilder.append(tokenChar);
            return new Token(stringBuilder.toString(), TokenType.HTML_CONTENT, lexer.getLineCounter(), lexer.getCharCounter());
        } else if (lexer.getIsEnd()) {
            return new Token("", TokenType.END, lexer.getLineCounter(), lexer.getCharCounter());
        }

        stringBuilder.append(tokenChar);

        int line = lexer.getLineCounter();
        int character = lexer.getCharCounter();

        if (tokenChar == '<' ) {
            if((tokenChar = lexer.getNextChar()) == '?') {
                setNextState();
                stringBuilder.append(tokenChar);
                tokenChar = lexer.getNextChar();
                return new Token(stringBuilder.toString(), TokenType.CTML_START, lexer.getLineCounter(), lexer.getCharCounter());
            } else {
                return new Token(stringBuilder.toString(), TokenType.HTML_CONTENT, line, character);
            }
        }

        tokenChar = lexer.getNextChar();
        return new Token(stringBuilder.toString(), TokenType.HTML_CONTENT, line, character);
    }


}
