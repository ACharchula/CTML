package ctml.structures.model;

public class Assignment implements Executable {
    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    private Variable variable;

    public void setExecutable(ReturnExecutable executable) {
        this.executable = executable;
    }

    private ReturnExecutable executable;

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        Variable v = ctmlBlock.getVariable(variable.getId());
        v.setValue(executable.getResult(ctmlBlock).getValue());
    }


}
