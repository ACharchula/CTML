package ctml.structures.model;

import ctml.interpreter.Interpreter;

public class ImageCtml implements Executable{
    private Variable link;

    public void setLink(Variable link) {
        this.link = link;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) {
        Interpreter.writer.println("<img src=\"" + link.getValue() + "\">");
    }
}
