package ctml.structures.model;


import java.util.ArrayList;
import java.util.List;

public class ListCtml implements Executable {
    List<Variable> variableList = new ArrayList<>();

    public void setVariableList(List<Variable> variableList) {
        this.variableList = variableList;
    }

    public void addVariable(Variable variable) {
        variableList.add(variable);
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) {

    }
}
