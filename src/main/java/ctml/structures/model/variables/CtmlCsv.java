package ctml.structures.model.variables;

import java.util.ArrayList;
import java.util.List;

public class CtmlCsv extends Variable {

    private List<List<String>> values = new ArrayList<>(new ArrayList<>());

    public CtmlCsv() {
        setTable(true);
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getValue(int i1, int i2) {
        return values.get(i1).get(i2);
    }

    @Override
    public List getTableValues() {
        return values;
    }

    @Override
    public void addTableValue(String value) {

    }

    @Override
    public void setValue(String value) {

    }

    @Override
    public void setTableValues(List values) {
        this.values = values;
    }

    @Override
    public Variable cloneVariable() {
        CtmlCsv cloned = new CtmlCsv();

        cloned.setId(getId());
        cloned.setTable(isTable());
        cloned.setType(getType());

        if(getIndex1() != null)
            cloned.setIndex1((CtmlInt) getIndex1().cloneVariable());

        if(getIndex2() != null)
            cloned.setIndex2((CtmlInt) getIndex2().cloneVariable());

        if(values != null) {
             cloned.setTableValues(values);
        }

        return cloned;
    }
}
