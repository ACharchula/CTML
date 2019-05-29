package ctml.structures.model.ctmlObjects;

import ctml.interpreter.Interpreter;
import ctml.structures.model.CtmlBlock;
import ctml.structures.model.Executable;
import ctml.structures.model.variables.Variable;

public class ImageCtml implements Executable {
    private Variable link;

    public void setLink(Variable link) {
        this.link = link;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        String value = ctmlBlock.getValue(link);
        Interpreter.writer.println("<img width=\"100%\" src=\"" + value + "\">");
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        ImageCtml imageCtml = new ImageCtml();
        imageCtml.setLink(link.cloneVariable());
        return imageCtml;
    }
}
