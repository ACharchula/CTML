package ctml.structures.model;

import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Expression extends ReturnExecutable {

    private List<TokenType> operators = new ArrayList<>();
    private List<Expression> operands = new ArrayList<>();
    private List<Variable> variables = new ArrayList<>();

    public void addOperator(TokenType operator) {
        operators.add(operator);
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
        final Literal literal = new Literal();

        if (operands.size() == 0){
            if (variables.size() != 0)
                return variables.get(0);
        }

        literal.setValue(operands.get(0).getResult().getValue());
//        if(operators.size() == 0) {
//            Variable v = new Variable();
//            v.setValue(literal.getValue());
//            return v;
//        } else {
            int index = 0;
            for (TokenType op : operators) {
                Expression operand = operands.get(index + 1);
                index ++;

                if (op == TokenType.ADD) {
                    literal.plus(operand.getResult().getValue());
                } else if (op == TokenType.SUBTRACT) {
                    literal.minus(operand.getResult().getValue());
                } else if (op == TokenType.MULTIPLY) {
                    literal.multi(operand.getResult().getValue());
                } else if (op == TokenType.DIVIDE) {
                    literal.div(operand.getResult().getValue());
                }
            }

            Variable v = new Variable();
            v.setValue(literal.getValue());
            return v;
        //}

//        if (operands.size() == 0){
//            if (variables.get(0).getType() == TokenType.STRING_CONTENT)
//                return variables.get(0);
//        }
//
//        return operands.get(0).getResult();
    }
}

class Literal {
    private String value;
    private boolean isBool = false;

    public void setValue(String  value) {
        this.value = value;
    }
    public void setValue(float result) {
        this.value = Float.toString(result);
    }

    public void setIsBool(boolean isBool) {
        this.isBool = isBool;
    }

    public boolean isBool() {
        return isBool;
    }

    public String getValue() {
        return value;
    }

    public void plus(String second) {
        float a = Float.parseFloat(value);
        float b = Float.parseFloat(second);

        setValue(a+b);
    }

    public void minus(String second) {
        float a = Float.parseFloat(value);
        float b = Float.parseFloat(second);

        setValue(a-b);
    }

    public void multi(String second) {
        float a = Float.parseFloat(value);
        float b = Float.parseFloat(second);

        setValue(a*b);
    }

    public void div(String second) {
        float a = Float.parseFloat(value);
        float b = Float.parseFloat(second);

        setValue(a/b);
    }
}
