package ctml.structures.model;

import ctml.interpreter.program.Program;
import ctml.structures.model.variables.Variable;
import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Function implements ReturnExecutable {

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

    public void setParameters(List<Variable> parameters) {
        this.parameters = parameters;
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

    private void addParameter(Variable v) {
        parameters.add(v);
    }

    @Override
    public Variable getResult(CtmlBlock outerCtmlBlock) throws Exception {
        List<Variable> arguments = Program.pop();

        if(arguments.size() != parameters.size())
            throw new Exception("Wrong amount of arguments!");

        for(int i = 0; i < arguments.size(); ++i) {
            Variable v = parameters.get(i);

            if(v.isTable())
                v.setTableValues(outerCtmlBlock.getTableValue(arguments.get(i)));
            else
                v.setValue(Variable.getStringValue(arguments.get(i), ctmlBlock));

            ctmlBlock.addVariable(v);
        }

        Variable v = ctmlBlock.executeFunction();

        if(v != null) {
            v.setType(returnType);
//            if(returnType != TokenType.CSV_TYPE)
//                v.verifyIfValueHasProperType(v.getValue());
        }

        return v;
    }

    @Override
    public ReturnExecutable cloneReturnExecutable() throws Exception {
        Function function = new Function();

        function.setReturnType(returnType);
        function.setId(id);
        function.setCtmlBlock(ctmlBlock.cloneCtmlBlock());

        for(Variable v: parameters) {
            function.addParameter(v.cloneVariable());
        }

        return function;
    }
}
