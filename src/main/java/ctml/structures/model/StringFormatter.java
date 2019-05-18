package ctml.structures.model;

import java.util.ArrayList;
import java.util.List;

public class StringFormatter extends ReturnExecutable {

    public void setVariableList(List<Variable> variableList) {
        this.variableList = variableList;
    }

    List<Variable> variableList = new ArrayList<>();

    @Override
    public void execute(CtmlBlock ctmlBlock) {

    }

    @Override
    Variable getResult(CtmlBlock ctmlBlock) throws Exception {
        return null;
    }
}
