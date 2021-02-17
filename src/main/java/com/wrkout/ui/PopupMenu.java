package com.wrkout.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PopupMenu extends JPopupMenu {
    JMenuItem anItem;
    ActivityTable table;
    int column;
    int row;

    public PopupMenu(ActivityTable table, int column, int row) {
        this.table = table;
        this.column = column;
        this.row = row;

        anItem = new JMenuItem(new AbstractAction("Ta bort rad " + row) {
            public void actionPerformed(ActionEvent e) {
                table.removeRow(row);
            }
        });

        add(anItem);
    }
}


