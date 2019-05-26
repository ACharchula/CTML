package ctml.structures.model;

import ctml.structures.model.variables.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CtmlBlock implements Block {

    private CtmlBlock parentCtmlBlock = null;
    private HashMap<String, Variable> variables = new HashMap<>();
    private List<Executable> instructions = new ArrayList<>();
    private Variable result;

    public void addVariable(Variable variable) throws Exception {
        if(variables.putIfAbsent(variable.getId(), variable) != null) {
            throw new Exception("There is already variable of id: " + variable.getId());
        }
    }

    public void addInstruction(Executable executable) {
        instructions.add(executable);
    }

    void setParentCtmlBlock(CtmlBlock parentCtmlBlock) {
        this.parentCtmlBlock = parentCtmlBlock;
    }

    void setResult(Variable result) {
        this.result = result;
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

    List getTableValue(Variable v) throws Exception {
        Variable found = getVariable(v.getId());
        return found.getTableValues();
    }

    @Override
    public void execute() throws Exception {
        for(Executable instruction : instructions) {
            instruction.execute(this);
        }
    }

    Variable executeFunction() throws Exception {

        for(Executable instruction : instructions) {
            instruction.execute(this);
            if(result != null)
                break;
        }

        return result;
    }

    CtmlBlock cloneCtmlBlock() throws Exception {
        CtmlBlock ctmlBlock = new CtmlBlock();

        for(Variable v: variables.values()) {
            ctmlBlock.addVariable(v.cloneVariable());
        }

        for(Executable ex : instructions) {
            ctmlBlock.addInstruction(ex.cloneExecutable());
        }

        return ctmlBlock;

    }

}
