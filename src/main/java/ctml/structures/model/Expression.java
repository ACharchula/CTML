package ctml.structures.model;

import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static ctml.structures.token.TokenType.*;

public class Expression implements ReturnExecutable {

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
    public ReturnExecutable cloneReturnExecutable() {
        return this;
    }

    @Override
    public Variable getResult(CtmlBlock ctmlBlock) throws Exception {
        final Literal literal = new Literal();

        if (operands.size() == 0){
            if (variables.size() != 0) {
                if (variables.get(0).getId() == null)
                    return variables.get(0);
                else if(variables.get(0).getType() != FUNCTION) {

                    if(variables.get(0).getIndex1() != null) {
                        Variable found = new Variable();
                        found.setValue(ctmlBlock.getValue(variables.get(0)));
                        return found;
                    } else if (ctmlBlock.getVariable(variables.get(0).getId()).getType() == CSV_TYPE)  {
                        Variable csv = new Variable();
                        csv.setValue(variables.get(0).getId());
                        csv.setType(CSV_TYPE);
                        return csv;
                    } else
                        return ctmlBlock.getVariable(variables.get(0).getId());
                } else {
                    return executeFunction(ctmlBlock);
                }
            }
        }

        if(operators.size() != 0 && isCondition()) {
            return executeCondition(ctmlBlock);
        } else {

            if(operands.get(0).getResult(ctmlBlock).getType() == CSV_TYPE) {
                return operands.get(0).getResult(ctmlBlock);
            } else
                literal.setValue(ctmlBlock.getValue(operands.get(0).getResult(ctmlBlock)));

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

    private Variable executeFunction(CtmlBlock ctmlBlock) throws Exception {
        FunctionCall functionCall = new FunctionCall();
        functionCall.setArguments(variables.get(0).getFunctionArguments());
        functionCall.setId(variables.get(0).getId());
        return functionCall.executeFunction(ctmlBlock);
    }

    private Variable executeCondition(CtmlBlock ctmlBlock) throws Exception {
        final Variable result = new Variable();
        switch (operators.get(0)) {
            case OR:
                or(result, ctmlBlock);
                break;
            case AND:
                and(result, ctmlBlock);
                break;
            case EQUALS:
                equals(result, ctmlBlock);
                break;
            case NOT_EQUALS:
                notEquals(result, ctmlBlock);
                break;
            case LESS:
                less(result, ctmlBlock);
                break;
            case LESS_EQUALS:
                lessEquals(result, ctmlBlock);
                break;
            case GREATER:
                greater(result, ctmlBlock);
                break;
            case GREATER_EQUALS:
                greaterEquals(result, ctmlBlock);
                break;
        }
        return result;
    }

    private boolean isCondition() {
        return operators.get(0) == OR || operators.get(0) == AND || operators.get(0) == EQUALS || operators.get(0) == NOT_EQUALS
                || operators.get(0) == GREATER || operators.get(0) == GREATER_EQUALS || operators.get(0) == LESS_EQUALS
                || operators.get(0) == LESS;
    }

    private void or(Variable result, CtmlBlock ctmlBlock) throws Exception {
        for (final Expression operand : operands) {
            if (operand.getResult(ctmlBlock).getValue().equals("1")) {
                result.setValue("1");
                return;
            }
        }
        result.setValue("0");
    }

    private void and(Variable result, CtmlBlock ctmlBlock) throws Exception {
        for (final Expression operand : operands) {
            if (!operand.getResult(ctmlBlock).getValue().equals("1")) {
                result.setValue("0");
                return;
            }
        }
        result.setValue("1");
    }

    private void equals(Variable result, CtmlBlock ctmlBlock) throws Exception {
        Variable left = operands.get(0).getResult(ctmlBlock);
        Variable right = operands.get(1).getResult(ctmlBlock);
        if (left.getValue().equals(right.getValue()))
            result.setValue("1");
        else
            result.setValue("0");
    }

    private void notEquals(Variable result, CtmlBlock ctmlBlock) throws Exception {
        Variable left = operands.get(0).getResult(ctmlBlock);
        Variable right = operands.get(1).getResult(ctmlBlock);
        if (left.getValue().equals(right.getValue()))
            result.setValue("0");
        else
            result.setValue("1");
    }

    private float getFloatValue(Expression expr, CtmlBlock ctmlBlock) throws Exception {
        Variable result = expr.getResult(ctmlBlock);
        return Float.parseFloat(result.getValue());
    }

    private void less(Variable result, CtmlBlock ctmlBlock) throws Exception {
        float leftSide = getFloatValue(operands.get(0), ctmlBlock);
        float rightSide = getFloatValue(operands.get(1), ctmlBlock);

        if(leftSide < rightSide)
            result.setValue("1");
        else
            result.setValue("0");
    }

    private void lessEquals(Variable result, CtmlBlock ctmlBlock) throws Exception {
        float leftSide = getFloatValue(operands.get(0), ctmlBlock);
        float rightSide = getFloatValue(operands.get(1), ctmlBlock);

        if(leftSide <= rightSide)
            result.setValue("1");
        else
            result.setValue("0");
    }

    private void greaterEquals(Variable result, CtmlBlock ctmlBlock) throws Exception {
        float leftSide = getFloatValue(operands.get(0), ctmlBlock);
        float rightSide = getFloatValue(operands.get(1), ctmlBlock);

        if(leftSide >= rightSide)
            result.setValue("1");
        else
            result.setValue("0");
    }

    private void greater(Variable result, CtmlBlock ctmlBlock) throws Exception {
        float leftSide = getFloatValue(operands.get(0), ctmlBlock);
        float rightSide = getFloatValue(operands.get(1), ctmlBlock);

        if(leftSide > rightSide)
            result.setValue("1");
        else
            result.setValue("0");
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
