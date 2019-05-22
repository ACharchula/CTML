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

        if(!v.isCsv() && v.isTable())
            v.setAndVerifyTableValues(executable.getResult(ctmlBlock).getTableValues());
        else if(v.isCsv())
            v.setAndVerifyCsvAssignment(executable.getResult(ctmlBlock).getTableValues());
        else
            v.setValue(executable.getResult(ctmlBlock).getValue());
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Assignment assignment = new Assignment();
        assignment.setVariable(variable.cloneWholeVariable());
        assignment.setExecutable((ReturnExecutable) executable.cloneExecutable());
        return assignment;
    }


}
