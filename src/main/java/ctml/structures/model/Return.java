package ctml.structures.model;

public class Return implements Executable {
    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    Expression expression = null;

    @Override
    public void execute(Block block) {

    }
}
