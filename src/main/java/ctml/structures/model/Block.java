package ctml.structures.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Block {

    private HashMap<String, Variable> variables = new HashMap<>();

    private List<Executable> instructions = new ArrayList<>();

    public void addVariable(Variable variable) throws Exception {

        if(variables.putIfAbsent(variable.getId(), variable) != null) {
            throw new Exception();
        }
    }

    public void addInstruction(Executable executable) {
        instructions.add(executable);
    }

    public void execute() {
        for(Executable instruction : instructions) {
            instruction.execute(this);
        }
    }

}
