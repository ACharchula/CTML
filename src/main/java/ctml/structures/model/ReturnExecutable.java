package ctml.structures.model;

import ctml.structures.model.variables.Variable;

public interface ReturnExecutable {
    Variable getResult(CtmlBlock ctmlBlock) throws Exception;
    ReturnExecutable cloneReturnExecutable() throws Exception;
}
