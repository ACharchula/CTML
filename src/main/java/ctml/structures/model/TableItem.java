package ctml.structures.model;

public class TableItem implements Executable {
    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    Variable variable;

    @Override
    public void execute(CtmlBlock ctmlBlock) {

    }
}
