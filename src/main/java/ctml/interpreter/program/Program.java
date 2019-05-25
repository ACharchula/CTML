package ctml.interpreter.program;

import ctml.structures.model.Block;
import ctml.structures.model.Function;
import ctml.structures.model.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Program {

    private static HashMap<String, Function> functions = new HashMap<>();
    private List<Block> blocks = new ArrayList<>();
    private static Stack<List<Variable>> stack = new Stack<>();

    public void execute() throws Exception {
        for (Block block : blocks) {
            block.execute();
        }

        functions.clear();
        blocks.clear();
        stack.clear();
    }

    public void addFunction(Function function) throws Exception {
        if(functions.putIfAbsent(function.getId(), function) != null) {
            throw new Exception("There is already function of id: " + function.getId());
        }
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public static Function getFunction(String id) throws Exception {
        Function function = functions.get(id);

        if(function == null)
            throw new Exception("There is no function of id: " + id);

        return (Function) function.cloneReturnExecutable();
    }

    public static void push(List<Variable> variables) {
        stack.push(variables);
    }

    public static List<Variable> pop() {
        return stack.pop();
    }

}
