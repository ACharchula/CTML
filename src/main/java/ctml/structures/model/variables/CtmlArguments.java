package ctml.structures.model.variables;

import java.util.ArrayList;
import java.util.List;

public class CtmlArguments extends Variable{

    private List<Variable> argumentList;

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getValue(int i1, int i2) {
        return null;
    }

    @Override
    public List getTableValues() {
        return argumentList;
    }

    @Override
    public void addTableValue(String value) {

    }

    @Override
    public void setValue(String value) {

    }

    @Override
    public void setTableValues(List values) {
        this.argumentList = values;
    }

    @Override
    public Variable cloneVariable() {
        CtmlArguments cloned = new CtmlArguments();

        cloned.setId(getId());
        cloned.setType(getType());

        if(getIndex1() != null)
            cloned.setIndex1((CtmlInt) getIndex1().cloneVariable());

        if(getIndex2() != null)
            cloned.setIndex2((CtmlInt) getIndex2().cloneVariable());

        if(argumentList != null) {
            List<Variable> clonedList = new ArrayList<>(argumentList);
            cloned.setTableValues(clonedList);
        }

        return cloned;
    }
}
