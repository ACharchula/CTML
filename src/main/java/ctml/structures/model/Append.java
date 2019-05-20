package ctml.structures.model;

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

        if(v.isCsv() || !v.isTable())
            throw new Exception("You cannot append to csv type or none table types");

        for(Variable arg: arguments) { //sprawdzanie jaki typ to jest i w razie koniecznosci odrzucic
            v.getTableValues().add(arg.getValue());
        }
    }
}
