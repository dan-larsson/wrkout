package com.wrkout.ui;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;

import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;

public class SQLiteTableModel extends AbstractTableModel {

    public static final String URL = "jdbc:sqlite:wrkout.db";

    private Connection connection;
    private Statement statement;
    private int numberOfRows;
    private boolean connectedToDatabase = false;
    private final String[] columnNames;
    private final String[] columnKeys;
    private final String[] objectKeys;
    private final ArrayList<BaseActivity> activityList;


    public SQLiteTableModel() {
        columnNames = ActivityHandler.getUniqueLabels();
        columnKeys = ActivityHandler.getUniqueKeys();
        objectKeys = ActivityHandler.getUniqueKeys(true);
        activityList = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URL);
            statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            connectedToDatabase = true;
            setQuery();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Class getColumnClass(int column) throws IllegalStateException {
        return String.class;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public int getRowCount() {
        return numberOfRows;
    }

    @Override
    public Object getValueAt(int row, int column) {
        return activityList.get(row).get(columnKeys[column]);
    }

    public BaseActivity getActivity(int row) {
        return activityList.get(row);
    }

    public void setQuery() throws IllegalStateException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        try {
            BaseActivity instance;
            boolean first = true;
            StringBuilder sql = new StringBuilder("SELECT ");
            String frmt;
            for (String key : objectKeys) {
                if (key.equals("username")) {
                    frmt = "users.name as username";
                } else {
                    frmt = "activities.%s";
                }
                if (first) {
                    sql.append(String.format(frmt, key));
                    first = false;
                } else {
                    sql.append(",");
                    sql.append(String.format(frmt, key));
                }
            }
            sql.append(" FROM activities");
            sql.append(" INNER JOIN users ON activities.user_id = users.id");
            sql.append(" ORDER BY activities.date;");

            ResultSet resultSet = statement.executeQuery(sql.toString());

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String value;
                String key;
                instance = ActivityHandler.newActivity(name);
                if (instance != null) {
                    int i;
                    for (i = 0; i < objectKeys.length; i++) {
                        value = resultSet.getString(i+1);
                        key = objectKeys[i];
                        if (value != null) {
                            instance.set(key, value);
                        }
                    }
                    activityList.add(instance);
                    numberOfRows += 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        fireTableStructureChanged();
    }

    public void removeRow(int rowNum, int activityId) {
        System.out.printf("Removing #%s\n", activityId);
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
