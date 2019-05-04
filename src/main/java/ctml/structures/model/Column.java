package ctml.structures.model;

public class Column implements Executable{
    private Variable variable;

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    public void execute(Block block) {

    }
}
