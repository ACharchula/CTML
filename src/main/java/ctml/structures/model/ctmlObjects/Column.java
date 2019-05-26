package ctml.structures.model.ctmlObjects;

import ctml.interpreter.Interpreter;
import ctml.structures.model.CtmlBlock;
import ctml.structures.model.Executable;
import ctml.structures.model.variables.Variable;

public class Column implements Executable {
    private Variable text;

    public void setText(Variable text) {
        this.text = text;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String value = Variable.getStringValue(text, ctmlBlock);
        Interpreter.writer.println("<th>" + value + "</th>");
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Column column = new Column();
        column.setText(text.cloneVariable());
        return column;
    }
}
