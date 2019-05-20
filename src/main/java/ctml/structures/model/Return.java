package ctml.structures.model;

public class Return implements Executable {
    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    Expression expression = null;

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        ctmlBlock.setResult(expression.getResult(ctmlBlock));
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Return ret= new Return();
        ret.setExpression((Expression) expression.cloneExecutable());
        return ret;
    }
}
