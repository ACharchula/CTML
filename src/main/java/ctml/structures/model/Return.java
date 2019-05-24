package ctml.structures.model;

import ctml.structures.token.TokenType;

public class Return implements Executable {

    private Expression expression;

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        Variable result = expression.getResult(ctmlBlock);

        if(result.getType() == TokenType.CSV_TYPE) {
            result.setTableValues(ctmlBlock.getVariable(result.getValue()).getTableValues());
        }

        ctmlBlock.setResult(result);
    }

    @Override
    public Executable cloneExecutable() {
        Return ret= new Return();
        ret.setExpression((Expression) expression.cloneReturnExecutable());
        return ret;
    }
}
