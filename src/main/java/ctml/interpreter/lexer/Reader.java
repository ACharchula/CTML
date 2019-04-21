package ctml.interpreter.lexer;

import ctml.structures.data.Token;

public interface Reader {
    void setNextState();
    void printState();
    Token read() throws Exception;
}
