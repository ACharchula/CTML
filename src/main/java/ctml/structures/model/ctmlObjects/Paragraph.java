package ctml.structures.model.ctmlObjects;

import ctml.interpreter.Interpreter;
import ctml.structures.model.CtmlBlock;
import ctml.structures.model.Executable;
import ctml.structures.model.variables.Variable;

public class Paragraph implements Executable {

    private Variable text;

    public void setText(Variable text) {
        this.text = text;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String value = Variable.getStringValue(text, ctmlBlock);
        Interpreter.writer.println("<p>" + value + "</p>");
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Paragraph paragraph = new Paragraph();
        paragraph.setText(text.cloneVariable());
        return paragraph;
    }
}
