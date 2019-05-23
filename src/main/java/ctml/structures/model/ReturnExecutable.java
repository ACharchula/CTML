package ctml.structures.model;

public interface ReturnExecutable {
    Variable getResult(CtmlBlock ctmlBlock) throws Exception;
    ReturnExecutable cloneReturnExecutable() throws Exception;
}
