package ctml.interpreter.parser;


import ctml.structures.model.Block;
import ctml.structures.model.Function;

import java.util.ArrayList;
import java.util.List;

public class Program {

    private List<Function> functionList = new ArrayList<>();
    private List<Block> blockList = new ArrayList<>();

    public void execute() {
        blockList.forEach(Block::execute);
    }

    public void addFunction(Function function) {
        functionList.add(function);
    }

    public void addBlock(Block block) {
        blockList.add(block);
    }

    public boolean checkIfFunctionExsists(String id) {
        for(Function function : functionList) {
            if(function.getId().equals(id))
                return true;
        }
        return false;
    }

    public List<Function> getFunctionList() {
        return functionList;
    }

    public List<Block> getBlockList() {
        return blockList;
    }


}
