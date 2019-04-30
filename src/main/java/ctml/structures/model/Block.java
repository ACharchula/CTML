package ctml.structures.model;

import ctml.helpers.Logger;

import java.util.HashMap;
import java.util.List;

public class Block {

    private HashMap<String, Variable> variables = new HashMap<>();

    public void addVariable(Variable variable) throws Exception {

        if(variables.putIfAbsent(variable.getId(), variable) != null) {
            throw new Exception();
        }

    }

}
