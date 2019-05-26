package ctml.structures.model.variables;

import java.util.ArrayList;
import java.util.List;

public class CtmlString extends Variable<String> {

    private String value;
    private List<String> list;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getValue(int i1, int i2) {
        return list.get(i1);
    }

    @Override
    public List<String> getTableValues() throws Exception {
        return list;
    }

    @Override
    public void addTableValue(String value) throws Exception {
        list.add(value);
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setTableValues(List<String> values) {
        this.list = values;
    }

    @Override
    public Variable<String> cloneVariable() {
        CtmlString cloned = new CtmlString();

        cloned.setId(getId());
        cloned.setTable(isTable());
        cloned.setValue(getValue());
        cloned.setType(getType());
        cloned.setIndex1((CtmlInt) getIndex1().cloneVariable());
        cloned.setIndex2((CtmlInt) getIndex2().cloneVariable());

        if(list != null) {
            List<String> clonedList = new ArrayList<>(list);
            cloned.setTableValues(clonedList);
        }

        return cloned;
    }
}
