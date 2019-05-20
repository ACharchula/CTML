package ctml.structures.model;

import ctml.interpreter.Interpreter;

public class ImageCtml implements Executable{
    private Variable link;

    public void setLink(Variable link) {
        this.link = link;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String value = ctmlBlock.getValue(link);

        Interpreter.writer.println("<img src=\"" + value + "\">");
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        ImageCtml imageCtml = new ImageCtml();
        imageCtml.setLink(link.cloneWholeVariable());
        return imageCtml;
    }
}
