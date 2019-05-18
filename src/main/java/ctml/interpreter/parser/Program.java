package ctml.interpreter.parser;

import ctml.structures.model.Block;
import ctml.structures.model.Function;

import java.util.ArrayList;
import java.util.List;

public class Program {

    private List<Function> functionList = new ArrayList<>();
    private List<Block> blocks = new ArrayList<>();

    public void execute() throws Exception {
        for (Block block : blocks) {
            block.execute();
        }
    }

    public void addFunction(Function function) {
        functionList.add(function);
    }

    public void addBlock(Block block) {
        blocks.add(block);
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

    public List<Block> getBlocks() {
        return blocks;
    }


}
