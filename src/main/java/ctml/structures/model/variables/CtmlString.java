package ctml.structures.model.variables;

import java.util.ArrayList;
import java.util.List;

public class CtmlString extends Variable {

    private String value;
    private List<String> list;

    @Override
    public String getValue() throws Exception {
        return value;
    }

    @Override
    public String getValue(int i1, int i2) throws Exception {
        return list.get(i1);
    }

    @Override
    public List getTableValues() throws Exception {
        return list;
    }

    @Override
    public void addTableValue(String value) throws Exception {
        if(list == null)
            list = new ArrayList<>();

        list.add(value);
    }

    @Override
    public void setValue(String value) throws Exception {
        this.value = value;
    }

    @Override
    public void setTableValues(List values) {
        this.list = values;
    }

    @Override
    public Variable cloneVariable() throws Exception {
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
