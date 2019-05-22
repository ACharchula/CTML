package ctml.structures.model;


import ctml.interpreter.Interpreter;

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
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        Interpreter.writer.println("<ul>");

        for (Variable v : variableList) {
            String value = ctmlBlock.getValue(v);
            Interpreter.writer.println("<li>" + value + "</li>");
        }

        Interpreter.writer.println("</ul>");
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        ListCtml listCtml = new ListCtml();

        for(Variable v : variableList) {
            listCtml.addVariable(v.cloneWholeVariable());
        }

        return listCtml;
    }
}
