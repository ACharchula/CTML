package ctml.structures.model;

import ctml.interpreter.Interpreter;

public class Paragraph implements Executable{
    public void setText(Variable text) {
        this.text = text;
    }

    private Variable text;

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String value = ctmlBlock.getValue(text);
        Interpreter.writer.println("<p>" + value + "</p>");
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Paragraph paragraph = new Paragraph();
        paragraph.setText(text.cloneWholeVariable());
        return paragraph;
    }
}
