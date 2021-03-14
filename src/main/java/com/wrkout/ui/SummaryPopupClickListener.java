package com.wrkout.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SummaryPopupClickListener extends MouseAdapter {
    private SummaryTable table;

    public SummaryPopupClickListener(SummaryTable table) {
        this.table = table;
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            show(e);
        }
    }

    private void show(MouseEvent e) {
        SummaryPopupMenu menu = new SummaryPopupMenu(this.table);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}