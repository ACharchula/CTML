package ctml.structures.model.ctmlObjects;

import ctml.interpreter.Interpreter;
import ctml.structures.model.CtmlBlock;
import ctml.structures.model.Executable;
import ctml.structures.model.variables.Variable;

public class Header implements Executable {
    private Variable text;

    public void setText(Variable text) {
        this.text = text;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String value = ctmlBlock.getValue(text);
        Interpreter.writer.println("<h1>" + value + "</h1>");
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Header header = new Header();
        header.setText(text.cloneVariable());
        return header;
    }
}
