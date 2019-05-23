package ctml.structures.model;

import java.util.ArrayList;
import java.util.List;

//This class is not used.
public class StringFormatter implements ReturnExecutable {

    public void setVariableList(List<Variable> variableList) {
        this.variableList = variableList;
    }

    private List<Variable> variableList = new ArrayList<>();

    @Override
    public ReturnExecutable cloneReturnExecutable() {
        return null;
    }

    @Override
    public Variable getResult(CtmlBlock ctmlBlock) {
        return null;
    }
}
