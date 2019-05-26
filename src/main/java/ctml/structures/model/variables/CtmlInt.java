package ctml.structures.model.variables;

import java.util.ArrayList;
import java.util.List;

public class CtmlInt extends Variable {

    private Integer value;
    private List<Integer> list;

    @Override
    public String getValue() {
        return value != null ? value.toString() : null;
    }

    @Override
    public String getValue(int i1, int i2) {
        return list.get(i1).toString();
    }

    @Override
    public List getTableValues() {
        return list;
    }

    @Override
    public void addTableValue(String value) {
        if(list == null)
            list = new ArrayList<>();

        list.add(Integer.parseInt(value));
    }

    @Override
    public void setValue(String value) {
        float floatValue = Float.parseFloat(value);
        this.value = Math.round(floatValue);
    }

    @Override
    public void setTableValues(List values) {
        this.list = values;
    }

    @Override
    public Variable cloneVariable() {
        CtmlInt cloned = new CtmlInt();

        cloned.setId(getId());
        cloned.setTable(isTable());

        if(value != null)
            cloned.setValue(value.toString());

        cloned.setType(getType());

        if(getIndex1() != null)
            cloned.setIndex1((CtmlInt) getIndex1().cloneVariable());

        if(getIndex2() != null)
            cloned.setIndex2((CtmlInt) getIndex2().cloneVariable());

        if(list != null) {
            List<Integer> clonedList = new ArrayList<>(list);
            cloned.setTableValues(clonedList);
        }

        return cloned;
    }
}
