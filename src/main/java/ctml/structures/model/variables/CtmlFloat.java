package ctml.structures.model.variables;

import java.util.ArrayList;
import java.util.List;

public class CtmlFloat extends Variable {

    private Float value;
    private List<Float> list;

    @Override
    public String getValue() throws Exception {
        return value.toString();
    }

    @Override
    public String getValue(int i1, int i2) throws Exception {
        return list.get(i1).toString();
    }

    @Override
    public List getTableValues() throws Exception {
        return list;
    }

    @Override
    public void addTableValue(String value) throws Exception {
        if(list == null)
            list = new ArrayList<>();

        list.add(Float.parseFloat(value));
    }

    @Override
    public void setValue(String value) throws Exception {
        this.value = Float.parseFloat(value);
    }

    @Override
    public void setTableValues(List values) {
        list = values;
    }

    @Override
    public Variable cloneVariable() throws Exception {
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
