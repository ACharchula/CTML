package ctml.structures.model;

import ctml.structures.model.variables.Variable;

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

        if(v.isTable()) {
            Variable result = returnExecutable.getResult(ctmlBlock);
            if(result.getTableValues() == null)
                throw new Exception("Wrong assignment to table of id: " + variable.getId());
            v.setTableValues(result.getTableValues());
        } else {
            v.setValue(returnExecutable.getResult(ctmlBlock).getValue());
        }
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Assignment assignment = new Assignment();
        assignment.setVariable(variable.cloneVariable());
        assignment.setReturnExecutable(returnExecutable.cloneReturnExecutable());
        return assignment;
    }


}
