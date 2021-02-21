package com.wrkout.ui;

import javax.swing.*;

public class UserTable extends JTable {
    public UserTable() {
        super(new UserTableModel(true));
    }
}
