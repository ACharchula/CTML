package ctml.structures.model;

import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Variable {

    private TokenType type;
    private String id;

    private boolean table;
    private boolean csv;

    private String value;
    private List tableValues;

    public List<Variable> functionArguments = null;

    private Variable index1 = null;
    private Variable index2 = null;

    public void setFunctionArguments(List<Variable> functionArguments) {
        this.functionArguments = functionArguments;
    }

    public List<Variable> getFunctionArguments() {
        return functionArguments;
    }

    public void addFunctionArgument(Variable variable) {
        if(functionArguments == null)
            functionArguments = new ArrayList<>();

        functionArguments.add(variable);
    }

    public Variable getIndex1() {
        return index1;
    }

    public Variable getIndex2() {
        return index2;
    }

    public void setIndex1(Variable index) {
        this.index1 = index;
    }

    public void setIndex2(Variable index2) {
        this.index2 = index2;
    }

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

    public String getValue(int i1, int i2) { //maybe need to add ctmlblock to find ex. tab[a]
        if(isCsv()) {
            return ((List<String>) getTableValues().get(i1)).get(i2);
        } else if (isTable()) {
            return (String) getTableValues().get(i1);
        }

        return null;
    }

    public void setValue(String value) {
        if(type == TokenType.INTEGER_TYPE) {
            float result = Float.parseFloat(value);
            value = Integer.toString(Math.round(result));
        }

        this.value = value;
    }

    public List getTableValues() {
        return tableValues;
    }

    public void setTableValues(List tableValues) {
        this.tableValues = tableValues;
    }

//    public Variable cloneVariable() {
//        Variable v = new Variable();
//
//        v.setTableValues(tableValues);
//        v.setValue(value);
//        v.setIsCsv(csv);
//        v.setType(type);
//        v.setId(id);
//        v.setIndex2(index1.cloneVariable());
//        v.setIndex1(index2.cloneVariable());
//        v.setIsTable(table);
//        v.setFunctionArguments(functionArguments);
//
//        return v;
//    }


}
