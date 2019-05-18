package ctml.structures.model;

import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Expression extends ReturnExecutable {

    private TokenType operator;
    private List<Expression> operands = new ArrayList<>();
    private List<Variable> variables = new ArrayList<>();

    public void setOperator(TokenType operator) {
        this.operator = operator;
    }

    public void addOperand(Expression expression) {
        operands.add(expression);
    }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) {

    }

    @Override
    Variable getResult() throws Exception {

        if (operands.size() == 0){
            if (variables.get(0).getType() == TokenType.STRING_CONTENT)
                return variables.get(0);
        }

        return operands.get(0).getResult();
    }
}
