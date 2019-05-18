package ctml.structures.model;

import ctml.interpreter.Interpreter;

public class Header implements Executable {
    public void setText(Variable text) {
        this.text = text;
    }

    private Variable text;

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String value = ctmlBlock.getValue(text);
        Interpreter.writer.println("<h1>" + value + "</h1>");
    }
}
