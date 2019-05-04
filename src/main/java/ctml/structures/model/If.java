package ctml.structures.model;

public class If implements Executable {

    private Expression expression;
    private Block block;

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public void setElseBlock(Block elseBlock) {
        this.elseBlock = elseBlock;
    }

    private Block elseBlock = null;

    @Override
    public void execute(Block block) {

    }
}
