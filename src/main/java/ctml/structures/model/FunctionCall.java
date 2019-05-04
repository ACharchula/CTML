package ctml.structures.model;

import java.util.ArrayList;
import java.util.List;

public class FunctionCall implements Executable {
    private String id;
    private List<Variable> arguments = new ArrayList<>();

    public void setId(String id) {
        this.id = id;
    }

    public void addArgument(Variable variable) {
        arguments.add(variable);
    }

    public void setArguments(List<Variable> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(Block block) {

    }
}
