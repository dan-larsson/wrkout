package com.wrkout.ui;

import com.wrkout.activites.BaseActivity;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PopupMenu extends JPopupMenu {
    public final String FRMT_DELETE = "Ta bort rad %d";
    JMenuItem anItem;
    ActivityTable table;
    BaseActivity current;
    int column;
    int row;

    public PopupMenu(ActivityTable table, int column, int row, BaseActivity current) {
        this.table = table;
        this.column = column;
        this.row = row;
        this.current = current;

        anItem = new JMenuItem(new AbstractAction(String.format(FRMT_DELETE, row)) {
            public void actionPerformed(ActionEvent e) {
                table.removeRow(row, current.getId());
            }
        });

        add(anItem);
    }
}


