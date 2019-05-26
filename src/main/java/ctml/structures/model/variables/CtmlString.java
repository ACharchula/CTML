package ctml.structures.model.variables;

import java.util.ArrayList;
import java.util.List;

public class CtmlString extends Variable {

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
    public List getTableValues() {
        return list;
    }

    @Override
    public void addTableValue(String value) {
        if(list == null)
            list = new ArrayList<>();

        list.add(value);
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setTableValues(List values) {
        this.list = values;
    }

    @Override
    public Variable cloneVariable() {
        CtmlString cloned = new CtmlString();

        cloned.setId(getId());
        cloned.setTable(isTable());

        if(value != null)
            cloned.setValue(value);

        cloned.setType(getType());

        if(getIndex1() != null)
            cloned.setIndex1((CtmlInt) getIndex1().cloneVariable());

        if(getIndex2() != null)
            cloned.setIndex2((CtmlInt) getIndex2().cloneVariable());

        if(list != null) {
            List<String> clonedList = new ArrayList<>(list);
            cloned.setTableValues(clonedList);
        }

        return cloned;
    }
}
