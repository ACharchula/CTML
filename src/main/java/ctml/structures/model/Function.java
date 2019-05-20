package ctml.structures.model;

import ctml.interpreter.parser.Program;
import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Function extends ReturnExecutable {

    private List<Variable> parameters = new ArrayList<>();
    private TokenType returnType;
    private CtmlBlock ctmlBlock;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Variable> getParameters() {
        return parameters;
    }

    public void setParameters(List<Variable> parameters) {
        this.parameters = parameters;
    }

    public TokenType getReturnType() {
        return returnType;
    }

    public void setReturnType(TokenType returnType) {
        this.returnType = returnType;
    }

    public CtmlBlock getCtmlBlock() {
        return ctmlBlock;
    }

    public void setCtmlBlock(CtmlBlock ctmlBlock) {
        this.ctmlBlock = ctmlBlock;
    }

    public void addParameter(Variable v) {
        parameters.add(v);
    }

    @Override
    Variable getResult(CtmlBlock outerCtmlBlock) throws Exception {
        List<Variable> arguments = Program.pop();

        if(arguments.size() != parameters.size()) //sprawdzic czy sa dobrego typu
            throw new Exception("Wrong amount of arguments!");

        for(int i = 0; i < arguments.size(); ++i) {
            Variable v = parameters.get(i);

            v.setValue(outerCtmlBlock.getValue(arguments.get(i)));
            ctmlBlock.addVariable(v);
        }

        Variable v = ctmlBlock.executeFunction();

        for(Variable var : parameters) { //clean arguments
            ctmlBlock.getVariables().remove(var.getId());
        }

        return v;
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {

    }

    public Function cloneFunction() throws Exception {
        Function function = new Function();

        function.setReturnType(returnType);

        for(Variable v: parameters) {
            function.addParameter(v.cloneParameter());
        }

        function.setId(id);

        function.setCtmlBlock(ctmlBlock.cloneCtmlBlock());

        return function;
    }
}
