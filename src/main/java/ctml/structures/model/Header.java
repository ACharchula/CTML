package ctml.structures.model;

import ctml.interpreter.Interpreter;

public class Header implements Executable {
    public void setText(Variable text) {
        this.text = text;
    }

    private Variable text;

    @Override
    public void execute(CtmlBlock ctmlBlock) {
        Interpreter.writer.println("<h1>" + text.getValue() + "</h1>");
    }
}
