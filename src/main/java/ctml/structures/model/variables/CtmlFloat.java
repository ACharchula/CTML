package ctml.structures.model.variables;

import java.util.ArrayList;
import java.util.List;

public class CtmlFloat extends Variable {

    private Float value;
    private List<Float> list;

    @Override
    public String getValue() {
        return value.toString();
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

        list.add(Float.parseFloat(value));
    }

    @Override
    public void setValue(String value) {
        this.value = Float.parseFloat(value);
    }

    @Override
    public void setTableValues(List values) {
        list = values;
    }

    @Override
    public Variable cloneVariable() {
        CtmlFloat cloned = new CtmlFloat();

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
            List<Float> clonedList = new ArrayList<>(list);
            cloned.setTableValues(clonedList);
        }

        return cloned;
    }
}
