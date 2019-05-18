package ctml.structures.model;

public class If implements Executable {

    private Expression expression;
    private CtmlBlock ctmlBlock;

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public void setCtmlBlock(CtmlBlock ctmlBlock) {
        this.ctmlBlock = ctmlBlock;
    }

    public void setElseCtmlBlock(CtmlBlock elseCtmlBlock) {
        this.elseCtmlBlock = elseCtmlBlock;
    }

    private CtmlBlock elseCtmlBlock = null;

    @Override
    public void execute(CtmlBlock parentCtmlBlock) {
//        ctmlBlock.setParentCtmlBlock(parentCtmlBlock);
//        ctmlBlock.execute(); //only test
    }
}
