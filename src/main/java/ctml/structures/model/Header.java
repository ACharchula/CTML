package ctml.structures.model;

import ctml.interpreter.Interpreter;

public class Header implements Executable {
    public void setText(Variable text) {
        this.text = text;
    }

    private Variable text;

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String value = null;
        if(text.getValue() == null && text.getId() != null) {
            value = ctmlBlock.getVariable(text.getId()).getValue();
        } else {
            value = text.getValue();
        }
        Interpreter.writer.println("<h1>" + value + "</h1>");
    }
}
