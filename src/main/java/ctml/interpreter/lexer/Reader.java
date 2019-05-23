package ctml.interpreter.lexer;

import ctml.structures.token.Token;

public interface Reader {
    void setNextState();
    Token read() throws Exception;
}
