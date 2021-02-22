package com.wrkout.ui;

import com.wrkout.user.User;
import com.wrkout.user.UserHandler;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class UserTableModel extends AbstractTableModel {

    private final String[] columnNames;
    private final String[] columnKeys;
    private ArrayList<User> userList;
    private UserHandler userHandler;

    public UserTableModel() {
        this(false);
    }

    public UserTableModel(boolean setQuery) {
        columnNames = User.getLabels();
        columnKeys = User.getKeys();
        userList = new ArrayList<>();
        userHandler = new UserHandler();

        if (setQuery) {
            this.loadUsers();
        }
    }

    public Class getColumnClass(int column) {
        if (column == 0) {
            return Integer.class;
        }
        return String.class;
    }

    public int getColumnCount() {
        return columnNames.length + 1;
    }

    public String getColumnName(int column) {
        if (column == 0) {
            return "";
        }
        return columnNames[column - 1];
    }

    public int getRowCount() {
        return userList.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return row + 1;
        }
        return userList.get(row).get(columnKeys[column - 1]);
    }

    public User getUser(int row) {
        return userList.get(row);
    }

    public void loadUsers() {
        try {
            userList = userHandler.getUsers();
            fireTableStructureChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user) {
        try {
            userHandler.addUser(user);
            userList.add(user);
            fireTableStructureChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeRow(int rowNum, int userId) {
        try {
            userHandler.deleteUser(userId);
            userList.remove(rowNum);
            fireTableStructureChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
