package ctml.interpreter;

import ctml.structures.data.Token;
import ctml.structures.data.TokenType;

import java.io.InputStream;

public class Interpreter {

    private static Lexer lexer;

    public static void run(final InputStream inputStream) throws Exception {
        lexer = new Lexer(inputStream);

        Token token = null;
        token = lexer.nextToken();
        while(token.getType() != TokenType.END ) {
            System.out.println(token.getTitle().concat(" "+token.getType().toString()));
            token = lexer.nextToken();
        }
        System.out.println(token.getTitle().concat(" "+token.getType().toString()));
    }
}
