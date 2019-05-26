package ctml.structures.model.variables;

import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public abstract class Variable {

    private TokenType type;
    private String id;
    private boolean isTable;

    private CtmlInt index1;
    private CtmlInt index2;

    public abstract String getValue() throws Exception;
    public abstract String getValue(int i1, int i2) throws Exception;

    public abstract List getTableValues() throws Exception;
    public abstract void addTableValue(String value) throws Exception;

    public abstract void setValue(String value) throws Exception;
    public abstract void setTableValues(List values);

    public abstract Variable cloneVariable() throws Exception;

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public void setIndex1(CtmlInt index1) {
        this.index1 = index1;
    }

    public void setIndex2(CtmlInt index2) {
        this.index2 = index2;
    }

    public CtmlInt getIndex1() {
        return index1;
    }

    public CtmlInt getIndex2() {
        return index2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTable() {
        return isTable;
    }

    public void setTable(boolean table) {
        isTable = table;
    }
}
