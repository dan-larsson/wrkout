package com.wrkout.ui;

import com.wrkout.activites.BaseActivity;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ActivityPopupClickListener extends MouseAdapter {
    private ActivityTable table;

    public ActivityPopupClickListener(ActivityTable table) {
        this.table = table;
    }

    public void mousePressed(MouseEvent e) {
        Point point;
        int column;
        int row;
        BaseActivity current;

        if (e.isPopupTrigger()) {
            point = e.getPoint();
            column = table.columnAtPoint(point);
            row = table.rowAtPoint(point);
            current = table.getActivity(row);

            show(e, column, row, current);
        }
    }

    private void show(MouseEvent e, int column, int row, BaseActivity current) {
        ActivityPopupMenu menu = new ActivityPopupMenu(this.table, column, row, current);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}