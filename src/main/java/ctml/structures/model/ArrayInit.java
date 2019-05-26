package ctml.structures.model;

import ctml.structures.model.variables.*;
import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class ArrayInit implements ReturnExecutable {

    public void setVariableList(List<Variable> variableList) {
        this.variableList = variableList;
    }

    private List<Variable> variableList = new ArrayList<>();
    private String variableId;

    public ArrayInit(String variableId) {
        this.variableId = variableId;
    }

    @Override
    public ReturnExecutable cloneReturnExecutable() throws Exception {
        ArrayInit arrayInit = new ArrayInit(variableId);

        List<Variable> list = new ArrayList<>();

        for(Variable v : variableList) {
            list.add(v.cloneVariable());
        }

        arrayInit.setVariableList(list);

        return arrayInit;
    }

    @Override
    public Variable getResult(CtmlBlock ctmlBlock) throws Exception {
        Variable variable = createNewVariable(ctmlBlock.getVariable(variableId).getType());

        for(Variable v : variableList) {
            String value = ctmlBlock.getValue(v);
            variable.addTableValue(value);
        }
        variable.setTable(true);
        return variable;
    }

    private Variable createNewVariable(TokenType type){
        switch(type) {
            case INTEGER_TYPE:
                return new CtmlInt();
            case FLOAT_TYPE:
                return new CtmlFloat();
            case STRING_TYPE:
                return new CtmlString();
            case CSV_TYPE:
                return new CtmlCsv();
        }
        return null;
    }


}
