package ctml.structures.model;

import ctml.interpreter.Interpreter;

import java.util.ArrayList;
import java.util.List;

public class Table implements Executable{
    List<Executable> tableRows = new ArrayList<>();

    public void setTableRows(List<Executable> tableItems) {
        this.tableRows = tableItems;
    }

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
}
