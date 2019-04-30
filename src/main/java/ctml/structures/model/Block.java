package ctml.structures.model;

import ctml.helpers.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Block {

    private HashMap<String, Variable> variables = new HashMap<>();

    private List<Instruction> instructions = new ArrayList<>();

    public void addVariable(Variable variable) throws Exception {

        if(variables.putIfAbsent(variable.getId(), variable) != null) {
            throw new Exception();
        }

    }

}
