package com.wrkout.ui;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;
import com.wrkout.user.User;

import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;

public class UserTableModel extends AbstractTableModel {

    public static final String URL = "jdbc:sqlite:wrkout.db";

    private Connection connection;
    private Statement statement;
    private boolean connectedToDatabase = false;
    private final String[] columnNames;
    private final String[] columnKeys;
    private final String[] objectKeys;
    private ArrayList<User> userList;

    public UserTableModel() {
        this(false);
    }

    public UserTableModel(boolean setQuery) {
        columnNames = User.getLabels();
        columnKeys = User.getKeys();
        objectKeys = User.getKeys(true);
        userList = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URL);
            statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            connectedToDatabase = true;
            if (setQuery) {
                this.setQuery();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Class getColumnClass(int column) throws IllegalStateException {
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

    public void setQuery() throws IllegalStateException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        try {
            userList = new ArrayList<>();

            User instance;
            boolean first = true;
            StringBuilder sql = new StringBuilder("SELECT ");
            String frmt;
            for (String key : objectKeys) {
                if (first) {
                    sql.append(String.format("users.%s", key));
                    first = false;
                } else {
                    sql.append(",");
                    sql.append(String.format("users.%s", key));
                }
            }
            sql.append(" FROM users");
            sql.append(" ORDER BY users.name;");

            ResultSet resultSet = statement.executeQuery(sql.toString());

            while (resultSet.next()) {
                instance = new User();
                for (int i = 0; i < objectKeys.length; i++) {
                    String value = resultSet.getString(i+1);
                    String key = objectKeys[i];
                    if (value != null) {
                        instance.set(key, value);
                    }
                }
                userList.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        fireTableStructureChanged();
    }

    public void addUser(User user) throws IllegalStateException, SQLException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        boolean isFirst = true;
        StringBuilder sqlKeys = new StringBuilder("INSERT INTO users (");
        StringBuilder sqlVals = new StringBuilder(") VALUES (");
        StringBuilder sql = new StringBuilder();

        for (String key : columnKeys) {
            if (isFirst) {
                sqlKeys.append(key);
                sqlVals.append(String.format("'%s'", user.get(key)));
                isFirst = false;
            } else {
                sqlKeys.append(String.format(",%s", key));
                sqlVals.append(String.format(",'%s'", user.get(key)));
            }
        }

        sql.append(sqlKeys.toString());
        sql.append(sqlVals.toString());
        sql.append(");");

        statement.executeUpdate(sql.toString());
        userList.add(user);

        fireTableStructureChanged();

    }

    public void createTables() throws IllegalStateException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        try {
            statement.executeUpdate(User.getCreateSQL());
            statement.executeUpdate(User.getFirstUserSQL());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void removeRow(int rowNum, int userId) throws IllegalStateException, SQLException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        String sql = String.format("DELETE FROM users WHERE id = %d", userId);
        statement.executeUpdate(sql);

        userList.remove(rowNum);
        fireTableStructureChanged();
    }

    public void disconnectFromDatabase() {
        if (!connectedToDatabase)
            return;

        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectedToDatabase = false;
        }
    }
}
