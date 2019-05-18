package ctml.structures.model;

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
    public void execute(CtmlBlock ctmlBlock) {

    }
}
