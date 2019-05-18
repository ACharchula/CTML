package ctml.structures.model;

public class Load extends ReturnExecutable{

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    private Variable variable;


    @Override
    public void execute(CtmlBlock ctmlBlock) {

    }

    @Override
    Variable getResult() throws Exception {
        return null;
    }
}
