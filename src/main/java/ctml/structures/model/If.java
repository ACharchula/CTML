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
    public void execute(CtmlBlock parentCtmlBlock) throws Exception {
        ctmlBlock.setParentCtmlBlock(parentCtmlBlock);

        if(elseCtmlBlock != null) {
            elseCtmlBlock.setParentCtmlBlock(parentCtmlBlock);
        }

        if(expression.getResult(ctmlBlock).getValue().equals("1")) {
            parentCtmlBlock.setResult(ctmlBlock.executeFunction());
        } else if (expression.getResult(ctmlBlock).getValue().equals("0")) {
            if(elseCtmlBlock != null) {
                parentCtmlBlock.setResult(elseCtmlBlock.executeFunction());
            }
        }
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        If ifSt = new If();
        ifSt.setExpression((Expression) expression.cloneReturnExecutable());
        ifSt.setCtmlBlock(ctmlBlock.cloneCtmlBlock());
        if(elseCtmlBlock != null)
            ifSt.setElseCtmlBlock(elseCtmlBlock.cloneCtmlBlock());
        return ifSt;
    }
}
