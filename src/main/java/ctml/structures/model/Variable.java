package ctml.structures.model;

import ctml.structures.token.TokenType;

import java.util.List;

public class Variable {

    private TokenType type;
    private String id;

    private boolean table;
    private boolean csv;

    private String value;
    private List tableValues;

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTable() {
        return table;
    }

    public void setIsTable(boolean table) {
        this.table = table;
    }

    public boolean isCsv() {
        return csv;
    }

    public void setIsCsv(boolean csv) {
        this.csv = csv;
        this.table = csv;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List getTableValues() {
        return tableValues;
    }

    public void setTableValues(List tableValues) {
        this.tableValues = tableValues;
    }


}
