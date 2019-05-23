package ctml.structures.model;

public class Return implements Executable {

    private Expression expression;

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        ctmlBlock.setResult(expression.getResult(ctmlBlock));
    }

    @Override
    public Executable cloneExecutable() {
        Return ret= new Return();
        ret.setExpression((Expression) expression.cloneReturnExecutable());
        return ret;
    }
}
