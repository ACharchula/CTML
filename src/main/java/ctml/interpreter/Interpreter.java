package ctml.interpreter;

import ctml.interpreter.lexer.Lexer;
import ctml.structures.data.Token;
import ctml.structures.data.TokenType;

import java.io.InputStream;

public class Interpreter {

    private static Lexer lexer;

    public static void run(final InputStream inputStream) throws Exception {
        lexer = new Lexer(inputStream);

        Token token = null;

        try {
            token = lexer.nextToken();
            while (token.getType() != TokenType.END) {
                System.out.printf("%20s %30s c:%d l:%d\n", token.getContent(), token.getType().toString(), token.getCharacterNumber(), token.getLineNumber());
                token = lexer.nextToken();
            }
            System.out.printf("%20s %30s c:%d l:%d\n", token.getContent(), token.getType().toString(), token.getCharacterNumber(), token.getLineNumber());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
