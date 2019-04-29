package ctml.structures.token;

public class Token {

    private String content;
    private TokenType type;

    private int lineNumber;
    private int characterNumber;

    public Token(String content, TokenType type, int lineNumber, int characterNumber) {
        this.content = content;
        this.type = type;
        this.lineNumber = lineNumber;
        this.characterNumber = ( characterNumber - content.length() );
    }

    public String getContent() {
        return content;
    }


    public TokenType getType() {
        return type;
    }

    public int getCharacterNumber() {
        return this.characterNumber;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }
}

