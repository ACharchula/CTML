package ctml.interpreter.lexer;

import ctml.structures.token.Token;

public interface Reader {
    void setNextState();
    void printState();
    Token read() throws Exception;
}
