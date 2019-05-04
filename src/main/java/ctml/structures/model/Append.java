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
    public void execute(Block block) {

    }
}
