package ctml.structures.model;

import ctml.interpreter.Interpreter;

public class Column implements Executable{
    private Variable variable;

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String value = ctmlBlock.getValue(variable);
        Interpreter.writer.println("<th>" + value + "</th>");
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Column column = new Column();
        column.setVariable(variable.cloneWholeVariable());
        return column;
    }
}
