package ctml.structures.model.ctmlObjects;

import ctml.interpreter.Interpreter;
import ctml.structures.model.CtmlBlock;
import ctml.structures.model.Executable;
import ctml.structures.model.Variable;

public class Link implements Executable {
    private Variable link;
    private Variable text;

    public void setLink(Variable link) {
        this.link = link;
    }

    public void setText(Variable text) {
        this.text = text;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String linkValue = ctmlBlock.getValue(link);
        String textValue = ctmlBlock.getValue(text);
        Interpreter.writer.println("<a href=" + linkValue + ">" + textValue + "</a>");
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Link linkCtml = new Link();
        linkCtml.setText(text.cloneVariable());
        linkCtml.setLink(link.cloneVariable());
        return linkCtml;
    }
}
