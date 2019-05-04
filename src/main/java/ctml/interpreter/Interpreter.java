package ctml.interpreter;

import ctml.interpreter.lexer.Lexer;
import ctml.interpreter.parser.Parser;
import ctml.interpreter.parser.Program;
import ctml.structures.token.Token;
import ctml.structures.token.TokenType;


import java.io.InputStream;

public class Interpreter {

    private static Lexer lexer;

    public static void run(final InputStream inputStream) throws Exception {

//        Token token = null;
//
//        try {
//            token = lexer.nextToken();
//            while (token.getType() != TokenType.END) {
//                System.out.printf("%20s %30s c:%d l:%d\n", token.getContent(), token.getType().toString(), token.getCharacterNumber(), token.getLineNumber());
//                token = lexer.nextToken();
//            }
//            System.out.printf("%20s %30s c:%d l:%d\n", token.getContent(), token.getType().toString(), token.getCharacterNumber(), token.getLineNumber());
//        } catch (Exception e) {
//            System.out.println(e);
//        }
        Parser parser = new Parser(inputStream);
        parser.parseProgram();


    }
}
