package com.wrkout.ui;

import com.wrkout.activites.BaseActivity;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActivityPopupMenu extends JPopupMenu {
    public static final String DELETE = "Ta bort";
    JMenuItem anItem;
    ActivityTable table;
    BaseActivity current;
    int column;
    int row;

    public ActivityPopupMenu(ActivityTable table, int column, int row, BaseActivity current) {
        this.table = table;
        this.column = column;
        this.row = row;
        this.current = current;

        anItem = new JMenuItem(new AbstractAction(DELETE) {
            public void actionPerformed(ActionEvent e) {
                table.removeRow(row, current.getId());
            }
        });

        add(anItem);
    }
}


