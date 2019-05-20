package ctml.structures.model;

import java.util.ArrayList;
import java.util.List;

public class ArrayInit extends ReturnExecutable {

    public void setVariableList(List<Variable> variableList) {
        this.variableList = variableList;
    }

    List<Variable> variableList = new ArrayList<>();

    @Override
    public void execute(CtmlBlock ctmlBlock) {

    }

    @Override
    public Executable cloneExecutable() throws Exception {
        ArrayInit arrayInit = new ArrayInit();

        List<Variable> list = new ArrayList<>();

        for(Variable v : variableList) {
            list.add(v.cloneWholeVariable());
        }

        arrayInit.setVariableList(list);

        return arrayInit;
    }

    @Override
    Variable getResult(CtmlBlock ctmlBlock) throws Exception {
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
