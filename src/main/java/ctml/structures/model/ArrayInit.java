package ctml.structures.model;

import java.util.ArrayList;
import java.util.List;

public class ArrayInit implements Executable {

    public void setVariableList(List<Variable> variableList) {
        this.variableList = variableList;
    }

    List<Variable> variableList = new ArrayList<>();

    @Override
    public void execute(CtmlBlock ctmlBlock) {

    }
}
