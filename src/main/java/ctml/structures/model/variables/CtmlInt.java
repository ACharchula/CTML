package ctml.structures.model.variables;

import java.util.ArrayList;
import java.util.List;

public class CtmlInt extends Variable<Integer> {

    private Integer value;
    private List<Integer> list;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public Integer getValue(int i1, int i2) {
        return list.get(i1);
    }

    @Override
    public List<Integer> getTableValues() throws Exception {
        return list;
    }

    @Override
    public void addTableValue(Integer value) throws Exception {
        list.add(value);
    }

    @Override
    public void setValue(String value) {
        float floatValue = Float.parseFloat(value);
        this.value = Math.round(floatValue);
    }

    @Override
    public void setTableValues(List<Integer> values) {
        this.list = values;
    }

    @Override
    public Variable<Integer> cloneVariable() {
        CtmlInt cloned = new CtmlInt();

        cloned.setId(getId());
        cloned.setTable(isTable());

        if(getValue() != null)
            cloned.setValue(getValue().toString());

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
