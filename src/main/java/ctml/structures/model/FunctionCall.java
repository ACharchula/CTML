package ctml.structures.model;

import ctml.interpreter.program.Program;
import ctml.structures.model.variables.Variable;

import java.util.ArrayList;
import java.util.List;

public class FunctionCall implements Executable {
    private String id;
    private List<Variable> arguments = new ArrayList<>();

    public void setId(String id) {
        this.id = id;
    }

    private void addArgument(Variable variable) {
        arguments.add(variable);
    }

    public void setArguments(List<Variable> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        executeFunction(ctmlBlock);
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        FunctionCall functionCall = new FunctionCall();
        functionCall.setId(id);

        for( Variable v : arguments) {
            functionCall.addArgument(v.cloneVariable());
        }

        return functionCall;
    }

    Variable executeFunction(CtmlBlock ctmlBlock) throws Exception {
        Program.push(arguments);
        Function function = Program.getFunction(id);
        return function.getResult(ctmlBlock);
    }
}
