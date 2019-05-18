package ctml.interpreter.parser;

import ctml.structures.model.Function;
import ctml.structures.model.Node;

import java.util.ArrayList;
import java.util.List;

public class Program {

    private List<Function> functionList = new ArrayList<>();
    private List<Node> nodes = new ArrayList<>();

    public void execute() {
        nodes.forEach(Node::execute);
    }

    public void addFunction(Function function) {
        functionList.add(function);
    }

    public void addBlock(Node node) {
        nodes.add(node);
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

    public List<Node> getNodes() {
        return nodes;
    }


}
