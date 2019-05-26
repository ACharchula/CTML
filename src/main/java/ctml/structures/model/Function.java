package ctml.structures.model;

import ctml.interpreter.Interpreter;
import ctml.interpreter.program.Program;
import ctml.structures.model.variables.CtmlCsv;
import ctml.structures.model.variables.Variable;
import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

import static ctml.structures.token.TokenType.*;

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
                v.setValue(outerCtmlBlock.getValue(arguments.get(i)));

            ctmlBlock.addVariable(v);
        }

        Variable v = ctmlBlock.executeFunction();

        if(v != null) {
            checkAndPrepareProperReturnType(v);
            v.setType(returnType);
        }

        return v;
    }

    private void checkAndPrepareProperReturnType(Variable result) throws Exception {
        if(returnType == INTEGER_TYPE) {
            checkIfTableIsNull(result);
            float floatResult = Float.parseFloat(result.getValue());
            result.setValue(Integer.toString(Math.round(floatResult)));
        } else if (returnType == FLOAT_TYPE) {
            checkIfTableIsNull(result);
            Float.parseFloat(result.getValue());
        } else if (returnType == STRING_TYPE) {
            checkIfTableIsNull(result);
        } else if (returnType == CSV_TYPE) {
            List tableValues = result.getTableValues();
            ((List<String>) tableValues.get(0)).get(0);//throws exception if csv is not returned
        } else if (returnType == VOID) {
            if(result.getValue() != null || result.getTableValues() != null)
                throw new Exception("You cannot return a value when type is void");
        }
    }

    private void checkIfTableIsNull(Variable variable) throws Exception {
        if(variable.getTableValues() != null)
            throw new Exception("It is impossible to return tables!");
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
