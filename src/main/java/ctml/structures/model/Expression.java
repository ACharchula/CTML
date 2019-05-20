package ctml.structures.model;

import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static ctml.structures.token.TokenType.*;

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
    public Executable cloneExecutable() throws Exception {
        return this;
    }

    @Override
    Variable getResult(CtmlBlock ctmlBlock) throws Exception {
        final Literal literal = new Literal();

        if (operands.size() == 0){
            if (variables.size() != 0) {
                if (variables.get(0).getId() == null)
                    return variables.get(0);
                else if(variables.get(0).getType() != FUNCTION) {
                    return ctmlBlock.getVariable(variables.get(0).getId());
                } else {
                    FunctionCall functionCall = new FunctionCall();
                    functionCall.setArguments(variables.get(0).getFunctionArguments());
                    functionCall.setId(variables.get(0).getId());
                    return functionCall.executeFunction(ctmlBlock);
                }
            }
        }

        if(operators.size() != 0 && (operators.get(0) == OR || operators.get(0) == AND || operators.get(0) == EQUALS || operators.get(0) == NOT_EQUALS
                || operators.get(0) == GREATER || operators.get(0) == GREATER_EQUALS || operators.get(0) == LESS_EQUALS
                || operators.get(0) == LESS)) {
            return executeCondition(ctmlBlock);
        } else {

            literal.setValue(operands.get(0).getResult(ctmlBlock).getValue());

            int index = 0;
            for (TokenType op : operators) {
                Expression operand = operands.get(index + 1);
                index++;

                if (op == ADD) {
                    literal.plus(operand.getResult(ctmlBlock).getValue());
                } else if (op == SUBTRACT) {
                    literal.minus(operand.getResult(ctmlBlock).getValue());
                } else if (op == MULTIPLY) {
                    literal.multi(operand.getResult(ctmlBlock).getValue());
                } else if (op == DIVIDE) {
                    literal.div(operand.getResult(ctmlBlock).getValue());
                }
            }

            Variable v = new Variable();
            v.setValue(literal.getValue());
            return v;
        }
    }

    public Variable executeCondition(CtmlBlock ctmlBlock) throws Exception {
        final Variable result = new Variable();
        switch (operators.get(0)) {
            case OR:
                for (final Expression operand : operands) {
                    if (operand.getResult(ctmlBlock).getValue().equals("1")) {
                        result.setValue("1");
                        return result;
                    }
                }
                result.setValue("0");
                return result;
            case AND:
                for (final Expression operand : operands) {
                    if (!operand.getResult(ctmlBlock).getValue().equals("1")) {
                        result.setValue("0");
                        return result;
                    }
                }
                result.setValue("1");
                return result;
            case EQUALS:
                Variable left = operands.get(0).getResult(ctmlBlock);
                Variable right = operands.get(1).getResult(ctmlBlock);
                if (left.getValue().equals(right.getValue()))
                    result.setValue("1");
                else
                    result.setValue("0");
                break;
            case NOT_EQUALS:
                left = operands.get(0).getResult(ctmlBlock);
                right = operands.get(1).getResult(ctmlBlock);
                if (left.getValue().equals(right.getValue()))
                    result.setValue("0");
                else
                    result.setValue("1");
                break;
            case LESS:
                left = operands.get(0).getResult(ctmlBlock);
                right = operands.get(1).getResult(ctmlBlock);

                float leftSide = Float.parseFloat(left.getValue());
                float righSide = Float.parseFloat(right.getValue());

                if(leftSide < righSide)
                    result.setValue("1");
                else
                    result.setValue("0");

                break;
            case LESS_EQUALS:
                left = operands.get(0).getResult(ctmlBlock);
                right = operands.get(1).getResult(ctmlBlock);

                leftSide = Float.parseFloat(left.getValue());
                righSide = Float.parseFloat(right.getValue());

                if(leftSide <= righSide)
                    result.setValue("1");
                else
                    result.setValue("0");

                break;
            case GREATER:
                left = operands.get(0).getResult(ctmlBlock);
                right = operands.get(1).getResult(ctmlBlock);

                leftSide = Float.parseFloat(left.getValue());
                righSide = Float.parseFloat(right.getValue());

                if(leftSide > righSide)
                    result.setValue("1");
                else
                    result.setValue("0");

                break;
            case GREATER_EQUALS:
                left = operands.get(0).getResult(ctmlBlock);
                right = operands.get(1).getResult(ctmlBlock);

                leftSide = Float.parseFloat(left.getValue());
                righSide = Float.parseFloat(right.getValue());

                if(leftSide >= righSide)
                    result.setValue("1");
                else
                    result.setValue("0");

                break;
        }
        return result;
    }
}

class Literal {
    private String value;

    void setValue(String value) {
        this.value = value;
    }
    private void setValue(float result) {
        this.value = Float.toString(result);
    }

    String getValue() {
        return value;
    }

    void plus(String second) {
        float a = Float.parseFloat(value);
        float b = Float.parseFloat(second);

        setValue(a+b);
    }

    void minus(String second) {
        float a = Float.parseFloat(value);
        float b = Float.parseFloat(second);

        setValue(a-b);
    }

    void multi(String second) {
        float a = Float.parseFloat(value);
        float b = Float.parseFloat(second);

        setValue(a*b);
    }

    void div(String second) {
        float a = Float.parseFloat(value);
        float b = Float.parseFloat(second);

        setValue(a/b);
    }
}
