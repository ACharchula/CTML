package ctml.structures.model;

import java.util.ArrayList;
import java.util.List;

public class Row implements Executable {

    private List<Executable> tableItems = new ArrayList<>();

    public void setTableItems(List<Executable> tableItems) {
        this.tableItems = tableItems;
    }

    public void addTableItem(Executable tableItem) {
        tableItems.add(tableItem);
    }

    @Override
    public void execute(CtmlBlock ctmlBlock) {

    }
}
