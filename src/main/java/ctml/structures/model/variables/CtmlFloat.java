package ctml.structures.model.variables;

import java.util.ArrayList;
import java.util.List;

public class CtmlFloat extends Variable<Float> {

    private Float value;
    private List<Float> list;

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public Float getValue(int i1, int i2) {
        return list.get(i1);
    }

    @Override
    public List<Float> getTableValues() throws Exception {
        return list;
    }

    @Override
    public void addTableValue(Float value) throws Exception {
        list.add(value);
    }

    @Override
    public void setValue(String value) {
        this.value = Float.parseFloat(value);
    }

    @Override
    public void setTableValues(List<Float> values) {
        list = values;
    }

    @Override
    public Variable<Float> cloneVariable() {
        CtmlFloat cloned = new CtmlFloat();

        cloned.setId(getId());
        cloned.setTable(isTable());
        cloned.setValue(getValue().toString());
        cloned.setType(getType());
        cloned.setIndex1((CtmlInt) getIndex1().cloneVariable());
        cloned.setIndex2((CtmlInt) getIndex2().cloneVariable());

        if(list != null) {
            List<Float> clonedList = new ArrayList<>(list);
            cloned.setTableValues(clonedList);
        }

        return cloned;
    }
}
