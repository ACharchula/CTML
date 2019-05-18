package ctml.structures.model;

public class Header implements Executable {
    public void setText(Variable text) {
        this.text = text;
    }

    private Variable text;

    @Override
    public void execute(CtmlBlock ctmlBlock) {

    }
}
