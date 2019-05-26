package ctml.structures.model;

import ctml.structures.model.variables.CtmlCsv;
import ctml.structures.model.variables.Variable;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Load implements ReturnExecutable{

    private Variable variable;

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    public ReturnExecutable cloneReturnExecutable() throws Exception {
        Load load = new Load();
        load.setVariable(variable.cloneVariable());
        return load;
    }

    @Override
    public Variable getResult(CtmlBlock ctmlBlock) throws Exception {
        String value = ctmlBlock.getValue(variable);

        List<List<String>> data = new ArrayList<>(new ArrayList<>());
        Path path = Paths.get(value);
        BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        String line = br.readLine();
        while (line != null) {
            String[] splitted = line.split(",");
            data.add(Arrays.asList(splitted));
            line = br.readLine();
        }

        Variable v = new CtmlCsv();
        v.setTableValues(data);
        return v;
    }
}
