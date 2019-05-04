package ctml.structures.model;

public class Load implements Executable {

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    private Variable variable;


    @Override
    public void execute(Block block) {

    }
}
