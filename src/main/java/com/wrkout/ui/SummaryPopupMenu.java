package com.wrkout.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SummaryPopupMenu extends JPopupMenu {
    public static final String COPY = "Kopiera";
    public static final String DELETE = "Ta bort";

    SummaryTable table;

    public SummaryPopupMenu(SummaryTable table) {
        this.table = table;

        add(new JMenuItem(new AbstractAction(COPY) {
            public void actionPerformed(ActionEvent e) {
                table.copyActivities();
                table.reload();
            }
        }));

        add(new JMenuItem(new AbstractAction(DELETE) {
            public void actionPerformed(ActionEvent e) {
                table.deleteActivities();
                table.reload();
            }
        }));

    }
}


