package com.wrkout.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopupClickListener extends MouseAdapter {
    private ActivityTable table;

    public PopupClickListener(ActivityTable table) {
        this.table = table;
    }

    public void mousePressed(MouseEvent e) {
        Point point;
        int column;
        int row;
        if (e.isPopupTrigger()) {
            point = e.getPoint();
            column = table.columnAtPoint(point);
            row = table.rowAtPoint(point);

            show(e, column, row);
        }
    }

    private void show(MouseEvent e, int column, int row) {
        PopupMenu menu = new PopupMenu(this.table, column, row);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}