package ctml.structures.model;

public interface Executable {

    void execute(CtmlBlock ctmlBlock) throws Exception;
    Executable cloneExecutable() throws Exception;
}
