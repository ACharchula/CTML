package ctml.structures.model;

public abstract class ReturnExecutable implements Executable{

    abstract Variable getResult() throws Exception;
}
