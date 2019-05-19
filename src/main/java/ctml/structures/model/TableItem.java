package ctml.structures.model;

import ctml.interpreter.Interpreter;

public class TableItem implements Executable {
    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    Variable variable;

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String value = ctmlBlock.getValue(variable);
        Interpreter.writer.println("<td>" + value + "</td>");
    }
}
