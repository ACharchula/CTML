package ctml.structures.model;

import java.util.ArrayList;
import java.util.List;

public class ArrayInit implements ReturnExecutable {

    public void setVariableList(List<Variable> variableList) {
        this.variableList = variableList;
    }

    private List<Variable> variableList = new ArrayList<>();

    @Override
    public ReturnExecutable cloneReturnExecutable() throws Exception {
        ArrayInit arrayInit = new ArrayInit();

        List<Variable> list = new ArrayList<>();

        for(Variable v : variableList) {
            list.add(v.cloneVariable());
        }

        arrayInit.setVariableList(list);

        return arrayInit;
    }

    @Override
    public Variable getResult(CtmlBlock ctmlBlock) throws Exception {
        List<String> tableValues = new ArrayList<>();

        for(Variable v : variableList) {
            String value = ctmlBlock.getValue(v);
            tableValues.add(value);
        }

        Variable result = new Variable();
        result.setTableValues(tableValues);

        return result;
    }
}
