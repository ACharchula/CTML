package ctml.structures.model.variables;

import ctml.structures.model.CtmlBlock;
import ctml.structures.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public abstract class Variable<T> {

    private TokenType type;
    private String id;
    private boolean isTable;

    private CtmlInt index1;
    private CtmlInt index2;

    public abstract T getValue() throws Exception;
    public abstract T getValue(int i1, int i2) throws Exception;

    public abstract List<T> getTableValues() throws Exception;
    public abstract void addTableValue(T value) throws Exception;

    public abstract void setValue(String value) throws Exception;
    public abstract void setTableValues(List<T> values);

    public abstract Variable<T> cloneVariable();

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

    public static <T> T getValue(Variable v, CtmlBlock ctmlBlock) throws Exception {
        if(v.getValue() == null && v.getId() != null) {
            Variable found = ctmlBlock.getVariable(v.getId());
            if(v.getIndex2() != null) {
                return (T) found.getValue((int) getValue(v.getIndex1(), ctmlBlock), (int) getValue(v.getIndex2(), ctmlBlock));
            } else if(v.getIndex1() != null) {
                return (T) found.getValue((int) getValue(v.getIndex1(), ctmlBlock), 0);
            } else {
                return (T) found.getValue();
            }
        } else {
            return (T) v.getValue();
        }
    }

    public static<T> String getStringValue(Variable v, CtmlBlock ctmlBlock) throws Exception {
        T result = getValue(v, ctmlBlock);

        if(result instanceof Integer)
            return Integer.toString((int) result);
        else if(result instanceof Float)
            return Float.toString((float) result);
        else if(result instanceof String)
            return (String) result;
        else
            throw new Exception("Wrong Type!");
    }

    //    private TokenType type;
//    private String id;
//
//    private boolean table;
//    private boolean csv;
//
//    private String value;
//    private List tableValues;
//
//    private List<Variable> functionArguments = null;
//
//    private Variable index1 = null;
//    private Variable index2 = null;
//
//    public void setFunctionArguments(List<Variable> functionArguments) {
//        this.functionArguments = functionArguments;
//    }
//
//    public List<Variable> getFunctionArguments() {
//        return functionArguments;
//    }
//
//    private void addFunctionArgument(Variable variable) {
//        if(functionArguments == null)
//            functionArguments = new ArrayList<>();
//
//        functionArguments.add(variable);
//    }
//
//    public Variable getIndex1() {
//        return index1;
//    }
//
//    public Variable getIndex2() {
//        return index2;
//    }
//
//    public void setIndex1(Variable index) {
//        this.index1 = index;
//    }
//
//    public void setIndex2(Variable index2) {
//        this.index2 = index2;
//    }
//
//    public TokenType getType() {
//        return type;
//    }
//
//    public void setType(TokenType type) {
//        this.type = type;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public boolean isTable() {
//        return table;
//    }
//
//    public void setIsTable(boolean table) {
//        this.table = table;
//    }
//
//    boolean isCsv() {
//        return csv;
//    }
//
//    public void setIsCsv(boolean csv) {
//        this.csv = csv;
//        this.table = csv;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public String getValue(int i1, int i2) {
//        if(isCsv()) {
//            return ((List<String>) getTableValues().get(i1)).get(i2);
//        } else if (isTable()) {
//            return (String) getTableValues().get(i1);
//        }
//
//        return null;
//    }
//
//    public void setValue(String value) throws Exception {
//        if(id == null) {
//            this.value = value;
//            return;
//        }
//
//        if(value == null)
//            return;
//
//        this.value = verifyIfValueHasProperType(value);
//    }
//
//    String verifyIfValueHasProperType(String value) throws Exception {
//        float floatValue;
//
//        try {
//            floatValue = Float.parseFloat(value);
//        } catch (Exception e) {
//            if(!(type == TokenType.STRING_TYPE))
//                throw new Exception("Wrong assignment type to variable of id : " + id + " type: " + type);
//            else {
//                return value;
//            }
//        }
//
//        if(type == TokenType.INTEGER_TYPE) {
//            value = Integer.toString(Math.round(floatValue));
//        }
//
//        return value;
//    }
//
//    public void setAndVerifyCsvAssignment(List list) throws Exception {
//        try {
//            ((List<String>) list.get(0)).get(0);
//        } catch (Exception e) {
//            throw new Exception("Wrong assignment to csv variable");
//        }
//
//        this.tableValues = list;
//    }
//
//    public void addTableValue(String value) throws Exception {
//        getTableValues().add(verifyIfValueHasProperType(value));
//    }
//
//    List getTableValues() {
//        return tableValues;
//    }
//
//    void setTableValues(List tableValues) {
//        this.tableValues = tableValues;
//    }
//
//    void setAndVerifyTableValues(List tableValues) throws Exception {
//
//        for(String v : (List<String>) tableValues) {
//            verifyIfValueHasProperType(v);
//        }
//
//        this.tableValues = tableValues;
//    }
//
//    public Variable cloneVariable() throws Exception {
//        Variable v = new Variable();
//
//        v.setId(id);
//        v.setType(type);
//        v.setIsCsv(csv);
//        v.setIsTable(table);
//        v.setValue(value);
//
//        if(functionArguments != null) {
//            for(Variable var : functionArguments) {
//                v.addFunctionArgument(var.cloneVariable());
//            }
//        } else
//            v.setFunctionArguments(null);
//        if(index1 != null)
//            v.setIndex1(index1.cloneVariable());
//        if(index2 != null)
//            v.setIndex2(index2.cloneVariable());
//
//        if(v.isCsv()) {
//            v.setTableValues(tableValues);
//        } else if (v.isTable() && tableValues != null) {
//            List<String> table = new ArrayList<>((List<String>) tableValues);
//            v.setTableValues(table);
//        } else
//            v.setTableValues(null);
//
//        return v;
//    }
}