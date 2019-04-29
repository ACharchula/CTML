package ctml.structures.model;

import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Function {

    private List<Variable> parameters = new ArrayList<>();
    private TokenType returnType;
    private Block block;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Variable> getParameters() {
        return parameters;
    }

    public void setParameters(List<Variable> parameters) {
        this.parameters = parameters;
    }

    public TokenType getReturnType() {
        return returnType;
    }

    public void setReturnType(TokenType returnType) {
        this.returnType = returnType;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
