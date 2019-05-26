package ctml.structures.model.variables;

import java.util.ArrayList;
import java.util.List;

public class CtmlArguments extends Variable<Variable> {

    private List<Variable> argumentList;

    @Override
    public Variable getValue() throws Exception {
        return null;
    }

    @Override
    public Variable getValue(int i1, int i2) throws Exception {
        throw new Exception("It is impossible to get value from argument!");
    }

    @Override
    public List<Variable> getTableValues() {
        return argumentList;
    }

    @Override
    public void addTableValue(Variable value) throws Exception {

    }

    @Override
    public void setValue(String value) throws Exception {
        throw new Exception("It is impossible to set value for argument!");
    }

    @Override
    public void setTableValues(List<Variable> values) {
        this.argumentList = values;
    }

    @Override
    public Variable<Variable> cloneVariable() {
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
