package com.wrkout.ui;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UserTableSelectionListener implements ListSelectionListener {

    private UserTable table;

    public UserTableSelectionListener(UserTable table) {
        super();
        this.table = table;
    }

    public void setTable(UserTable table) {
        this.table = table;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
