package com.wrkout.ui;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;
import com.wrkout.user.User;

import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;

public class ActivityTableModel extends AbstractTableModel {

    public static final String URL = "jdbc:sqlite:wrkout.db";

    private Connection connection;
    private Statement statement;
    private boolean connectedToDatabase = false;
    private final String[] columnNames;
    private final String[] columnKeys;
    private final String[] objectKeys;
    private ArrayList<BaseActivity> activityList;

    public ActivityTableModel() {
        this(false);
    }

    public ActivityTableModel(boolean setQuery) {
        columnNames = ActivityHandler.getUniqueLabels();
        columnKeys = ActivityHandler.getUniqueKeys();
        objectKeys = ActivityHandler.getUniqueKeys(true);
        activityList = new ArrayList<>();

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

    public void createTables() throws IllegalStateException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        try {
            statement.executeUpdate(ActivityHandler.getCreateSQL());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Class getColumnClass(int column) {
        if (column == 0) {
            return Integer.class;
        }
        return String.class;
    }

    public int getColumnCount() {
        return columnNames.length + 2;
    }

    public String getColumnName(int column) {
        if (column == 0) {
            return "";
        } else if (column == columnNames.length + 1) {
            return "kcal";
        }
        return columnNames[column - 1];
    }

    public int getRowCount() {
        return activityList.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return row + 1;
        } else if (column == columnNames.length + 1) {
            return activityList.get(row).getKCAL();
        }
        return activityList.get(row).get(columnKeys[column - 1]);
    }

    public BaseActivity getActivity(int row) {
        return activityList.get(row);
    }

    public void setQuery() throws IllegalStateException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        try {
            activityList = new ArrayList<>();

            BaseActivity instance;
            boolean first = true;
            StringBuilder sql = new StringBuilder("SELECT ");
            String frmt;
            for (String key : objectKeys) {
                if (key.equals("username")) {
                    frmt = "users.name as username";
                } else if (key.equals("userweight")) {
                    frmt = "users.weight as userweight";
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        fireTableStructureChanged();
    }

    public void addActivity(BaseActivity activity, int user_id) throws IllegalStateException, SQLException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        StringBuilder sql = new StringBuilder("INSERT INTO activities (user_id");
        String[] keys = activity.getKeys();

        for (String key : keys) {
            sql.append(",");
            sql.append(key);
        }
        sql.append(") VALUES (");
        sql.append(user_id);

        for (String key : keys) {
            sql.append(String.format(",'%s'", activity.get(key)));
        }
        sql.append(");");

        statement.executeUpdate(sql.toString());
        activityList.add(activity);

        fireTableStructureChanged();

    }

    public void addRow(Object[] row, int user_id) throws IllegalStateException, SQLException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        StringBuilder sql = new StringBuilder("INSERT INTO activities (user_id");
        String[] keys = ActivityHandler.getUniqueKeys();
        boolean isFirst = true;

        for (String key : keys) {
            sql.append(",");
            sql.append(key);
        }
        sql.append(") VALUES (");
        sql.append(user_id);

        for (Object key : row) {
            sql.append(String.format(",'%s'", key));
        }
        sql.append(");");

        System.out.println(sql.toString());

        statement.executeUpdate(sql.toString());

        fireTableStructureChanged();

    }

    public void removeRow(int rowNum, int activityId) throws IllegalStateException, SQLException {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");

        String sql = String.format("DELETE FROM activities WHERE id = %d", activityId);
        statement.executeUpdate(sql);

        activityList.remove(rowNum);
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
