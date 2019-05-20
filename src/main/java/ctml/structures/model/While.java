package ctml.structures.model;

public class While implements Executable {

    private Expression expression;

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public void setCtmlBlock(CtmlBlock ctmlBlock) {
        this.ctmlBlock = ctmlBlock;
    }

    private CtmlBlock ctmlBlock;

    @Override
    public void execute(CtmlBlock outerCtmlBlock) throws Exception {
        ctmlBlock.setParentCtmlBlock(outerCtmlBlock);

        while(expression.getResult(ctmlBlock).getValue().equals("1")) {
            ctmlBlock.execute();
        }
    }
}
