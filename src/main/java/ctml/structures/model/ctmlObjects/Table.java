package ctml.structures.model.ctmlObjects;

import ctml.interpreter.Interpreter;
import ctml.structures.model.CtmlBlock;
import ctml.structures.model.Executable;

import java.util.ArrayList;
import java.util.List;

public class Table implements Executable {

    private List<Executable> tableRows = new ArrayList<>();

    public void addRow(Row row) {
        tableRows.add(row);
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        Interpreter.writer.println("<table style=\"width:100%\" border=\"1\">");
        for(Executable ex : tableRows) {
            ex.execute(ctmlBlock);
        }
        Interpreter.writer.println("</table>");
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Table table = new Table();

        for(Executable row : tableRows) {
            table.addRow((Row) row.cloneExecutable());
        }

        return table;
    }
}
