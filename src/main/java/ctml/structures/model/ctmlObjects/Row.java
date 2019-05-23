package ctml.structures.model.ctmlObjects;

import ctml.interpreter.Interpreter;
import ctml.structures.model.CtmlBlock;
import ctml.structures.model.Executable;

import java.util.ArrayList;
import java.util.List;

public class Row implements Executable {

    private List<Executable> tableItems = new ArrayList<>();

    public void addTableItem(Executable tableItem) {
        tableItems.add(tableItem);
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) throws Exception {
        Interpreter.writer.println("<tr>");
        for(Executable ex : tableItems) {
            ex.execute(ctmlBlock);
        }
        Interpreter.writer.println("</tr>");
    }

    @Override
    public Executable cloneExecutable() throws Exception {
        Row row = new Row();

        for(Executable tableItem : tableItems) {
            row.addTableItem(tableItem.cloneExecutable());
        }

        return row;
    }
}
