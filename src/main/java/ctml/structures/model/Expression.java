package ctml.structures.model;

import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Expression {

    private TokenType operator;
    private List<Expression> operands = new ArrayList<>();

    public void setOperator(TokenType operator) {
        this.operator = operator;
    }

    public void addOperand(Expression expression) {
        operands.add(expression);
    }

}
