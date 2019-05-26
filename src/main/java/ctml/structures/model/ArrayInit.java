package ctml.structures.model;

import ctml.structures.model.variables.CtmlInt;
import ctml.structures.model.variables.CtmlString;
import ctml.structures.model.variables.Variable;

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
        List<Variable> tableValues = new ArrayList<>();

        for(Variable v : variableList) {
            tableValues.add( Variable.getValue(v, ctmlBlock));
        }

        Variable result = new CtmlInt();
        result.setTableValues(tableValues);

        return result;
    }
}
