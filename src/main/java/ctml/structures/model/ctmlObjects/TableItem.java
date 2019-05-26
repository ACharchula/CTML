package ctml.structures.model.ctmlObjects;

import ctml.interpreter.Interpreter;
import ctml.structures.model.CtmlBlock;
import ctml.structures.model.Executable;
import ctml.structures.model.variables.Variable;

public class TableItem implements Executable {

    private Variable variable;

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String value = Variable.getStringValue(variable, ctmlBlock);
        Interpreter.writer.println("<td>" + value + "</td>");
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        TableItem tableItem = new TableItem();
        tableItem.setVariable(variable.cloneVariable());
        return tableItem;
    }
}
