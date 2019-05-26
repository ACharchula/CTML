package ctml.structures.model;

import ctml.structures.model.variables.CtmlCsv;
import ctml.structures.model.variables.Variable;
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
            result.setTableValues(((CtmlCsv) ctmlBlock.getVariable((String) result.getValue())).getCsvTableValues());
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
