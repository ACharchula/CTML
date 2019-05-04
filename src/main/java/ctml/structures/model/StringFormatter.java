package ctml.structures.model;

import java.util.ArrayList;
import java.util.List;

public class StringFormatter implements Executable {

    public void setVariableList(List<Variable> variableList) {
        this.variableList = variableList;
    }

    List<Variable> variableList = new ArrayList<>();

    @Override
    public void execute(Block block) {

    }
}
