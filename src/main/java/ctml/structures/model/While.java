package ctml.structures.model;

public class While implements Executable {

    private Expression expression;

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    private Block block;

    @Override
    public void execute(Block block) {

    }
}
