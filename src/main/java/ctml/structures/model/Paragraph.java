package ctml.structures.model;

public class Paragraph implements Executable{
    public void setText(Variable text) {
        this.text = text;
    }

    private Variable text;

    @Override
    public void execute(CtmlBlock ctmlBlock) {

    }
}
