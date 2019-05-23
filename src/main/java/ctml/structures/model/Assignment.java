package ctml.structures.model;

public class Assignment implements Executable {

    private Variable variable;
    private ReturnExecutable returnExecutable;

    public void setReturnExecutable(ReturnExecutable returnExecutable) {
        this.returnExecutable = returnExecutable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        Variable v = ctmlBlock.getVariable(variable.getId());

        if(!v.isCsv() && v.isTable())
            v.setAndVerifyTableValues(returnExecutable.getResult(ctmlBlock).getTableValues());
        else if(v.isCsv())
            v.setAndVerifyCsvAssignment(returnExecutable.getResult(ctmlBlock).getTableValues());
        else
            v.setValue(returnExecutable.getResult(ctmlBlock).getValue());
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Assignment assignment = new Assignment();
        assignment.setVariable(variable.cloneVariable());
        assignment.setReturnExecutable(returnExecutable.cloneReturnExecutable());
        return assignment;
    }


}
