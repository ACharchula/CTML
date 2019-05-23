package ctml.structures.model.ctmlObjects;


import ctml.interpreter.Interpreter;
import ctml.structures.model.CtmlBlock;
import ctml.structures.model.Executable;
import ctml.structures.model.Variable;

import java.util.ArrayList;
import java.util.List;

public class ListCtml implements Executable {
    private List<Variable> variableList = new ArrayList<>();

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
            listCtml.addVariable(v.cloneVariable());
        }

        return listCtml;
    }
}
