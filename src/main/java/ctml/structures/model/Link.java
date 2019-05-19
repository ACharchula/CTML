package ctml.structures.model;

import ctml.interpreter.Interpreter;

public class Link implements Executable{
    private Variable link;

    public void setLink(Variable link) {
        this.link = link;
    }

    public void setText(Variable text) {
        this.text = text;
    }

    private Variable text;

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String linkValue = ctmlBlock.getValue(link);
        String textValue = ctmlBlock.getValue(text);
        Interpreter.writer.println("<a href=" + linkValue + ">" + textValue + "</a>");
    }
}
