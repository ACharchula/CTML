package ctml.interpreter.parser;

import ctml.structures.model.Block;
import ctml.structures.model.Function;
import ctml.structures.model.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Program {

    private static List<Function> functionList = new ArrayList<>();
    private List<Block> blocks = new ArrayList<>();
    private static Stack<List<Variable>> stack = new Stack<>();

    public void execute() throws Exception {
        for (Block block : blocks) {
            block.execute();
        }

        functionList.clear();
        stack.clear();
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

    public static List<Function> getFunctionList() {
        return functionList;
    }

    public static Function getFunction(String id) throws Exception {
        for( Function f : functionList) {
            if(f.getId().equals(id)) {
                return f.cloneFunction();
            }
        }

        return null;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public static void push(List<Variable> variables) {
        stack.push(variables);
    }

    public static List<Variable> pop() {
        return stack.pop();
    }

}
