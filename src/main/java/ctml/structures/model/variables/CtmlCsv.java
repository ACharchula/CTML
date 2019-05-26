package ctml.structures.model.variables;

import java.util.ArrayList;
import java.util.List;

public class CtmlCsv extends Variable<String> {

    private List<List<String>> values = new ArrayList<>(new ArrayList<>());

    public CtmlCsv() {
        setTable(true);
    }

    @Override
    public String getValue() throws Exception {
        throw new Exception("Impossible to take value from CSV type without passing an indexes");
    }

    @Override
    public String getValue(int i1, int i2) {
        return values.get(i1).get(i2);
    }

    @Override
    public List<String> getTableValues() throws Exception {
        throw new Exception("Impossible to take all table values from csv variable!");
    }

    @Override
    public void addTableValue(String value) throws Exception {

    }

    public List<List<String>> getCsvTableValues() throws Exception {
        return values;
    }

    @Override
    public void setValue(String value) throws Exception {
        throw new Exception("Impossible to set value to CSV type without passing an table");
    }

    @Override
    public void setTableValues(List<String> values) {
        this.values.add(values);
    }

    @Override
    public Variable<String> cloneVariable() {
        CtmlCsv cloned = new CtmlCsv();

        cloned.setId(getId());
        cloned.setTable(isTable());
        cloned.setType(getType());
        cloned.setIndex1((CtmlInt) getIndex1().cloneVariable());
        cloned.setIndex2((CtmlInt) getIndex2().cloneVariable());

        if(values != null) {
            for(List<String> row : values) {
                cloned.setTableValues(row);
            }
        }

        return cloned;
    }
}
