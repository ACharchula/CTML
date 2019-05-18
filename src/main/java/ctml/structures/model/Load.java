package ctml.structures.model;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Load extends ReturnExecutable{

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    private Variable variable;


    @Override
    public void execute(CtmlBlock ctmlBlock) {

    }

    @Override
    Variable getResult(CtmlBlock ctmlBlock) throws Exception {
        String value;
        if(variable.getValue() == null && variable.getId() != null) {
            value = ctmlBlock.getVariable(variable.getId()).getValue();
        } else {
            value = variable.getValue();
        }

        List<List<String>> data = new ArrayList<>(new ArrayList<>());
        Path path = Paths.get(value);
        BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        String line = br.readLine();
        while (line != null) {
            String[] splitted = line.split(",");
            data.add(Arrays.asList(splitted));
            line = br.readLine();
        }

        Variable v = new Variable();
        v.setIsCsv(true);
        v.setTableValues(data);
        return v;
    }
}
