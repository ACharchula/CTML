package ctml.structures.model;

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
    public void execute(Block block) {

    }
}
