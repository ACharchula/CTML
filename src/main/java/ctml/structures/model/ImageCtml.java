package ctml.structures.model;

import ctml.interpreter.Interpreter;

public class ImageCtml implements Executable{
    private Variable link;

    public void setLink(Variable link) {
        this.link = link;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String value = null;
        if(link.getValue() == null && link.getId() != null) {
            value = ctmlBlock.getVariable(link.getId()).getValue();
        } else {
            value = link.getValue();
        }

        Interpreter.writer.println("<img src=\"" + value + "\">");
    }
}
