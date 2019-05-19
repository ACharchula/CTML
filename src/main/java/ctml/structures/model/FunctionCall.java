package ctml.structures.model;

import ctml.interpreter.parser.Program;

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
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        executeFunction(ctmlBlock);
    }

    public Variable executeFunction(CtmlBlock ctmlBlock) throws Exception {
        Program.push(arguments);
        Function function = Program.getFunction(id);
        return function.getResult(ctmlBlock);
    }
}
