package ctml.structures.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CtmlBlock implements Block {

    private CtmlBlock parentCtmlBlock = null;

    private HashMap<String, Variable> variables = new HashMap<>();

    public HashMap<String, Variable> getVariables() {
        return variables;
    }

    public List<Executable> getInstructions() {
        return instructions;
    }

    private List<Executable> instructions = new ArrayList<>();

    public void addVariable(Variable variable) throws Exception {

        if(variables.putIfAbsent(variable.getId(), variable) != null) {
            throw new Exception();
        }
    }

    public void addInstruction(Executable executable) {
        instructions.add(executable);
    }

    public CtmlBlock getParentCtmlBlock() {
        return parentCtmlBlock;
    }

    public void setParentCtmlBlock(CtmlBlock parentCtmlBlock) {
        this.parentCtmlBlock = parentCtmlBlock;
    }

    public Variable getVariable(String id) throws Exception {
        Variable variable = variables.get(id);

        if(variable == null) {
            if(parentCtmlBlock != null)
                return parentCtmlBlock.getVariable(id);
            else
                throw new Exception("No variable of id " + id);
        }

        return variable;
    }

    @Override
    public void execute() throws Exception {
        for(Executable instruction : instructions) {
            instruction.execute(this);
        }
    }

    @Override
    public String getStructure() {
        return "";
    }

}
