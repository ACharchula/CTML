package ctml.structures.model;

public class Assignment implements Executable {
    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    private Variable variable;

    public void setExecutable(Executable executable) {
        this.executable = executable;
    }

    private Executable executable;

    @Override
    public void execute(Block block) {

    }


}
