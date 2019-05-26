package ctml.structures.model;

import ctml.structures.model.variables.CtmlCsv;
import ctml.structures.model.variables.Variable;

import java.util.ArrayList;
import java.util.List;

public class Append implements Executable {
    private Variable variable;
    private List<Variable> arguments = new ArrayList<>();

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public void setArguments(List<Variable> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        Variable v = ctmlBlock.getVariable(variable.getId());

        if(v instanceof CtmlCsv || !v.isTable())
            throw new Exception("You cannot append to csv type or none table types");

        for(Variable arg: arguments) {
            v.addTableValue(arg.getValue());
        }
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Append append = new Append();
        append.setVariable(variable.cloneVariable());

        List<Variable> arg = new ArrayList<>();

        for(Variable v : arguments) {
            arg.add(v.cloneVariable());
        }

        append.setArguments(arg);

        return append;
    }
}
