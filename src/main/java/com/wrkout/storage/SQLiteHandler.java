package com.wrkout.storage;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;

import java.sql.*;
import java.util.*;

public class SQLiteHandler implements StorageHandler {

    public static final String URL = "jdbc:sqlite:wrkout.db";

    private Connection c = null;
    private Statement stmt = null;
    private ResultSet res = null;

    public SQLiteHandler() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        initTables();
        createFirstUser();
    }

    public void addActivity(BaseActivity activity, int user_id) {
        StringBuilder sql = new StringBuilder("INSERT INTO activities (user_id");
        String[] keys = activity.getKeys();
        boolean isFirst = true;

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

        System.out.println(sql.toString());

        try {
            connect();
            stmt = c.createStatement();
            stmt.executeUpdate(sql.toString());
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            close();
        }

    }

    private void connect() {
        try {
            c = DriverManager.getConnection(URL);
        } catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void close() {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException ignore) {}
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignore) {}
        }
        /*
        if (c != null) {
            try {
                c.close();
            } catch (SQLException ignore) {}
        }
        */

    }

    private void initTables() {
        String[] keys = ActivityHandler.getUniqueKeys();

        StringBuilder activitiesSql = new StringBuilder("CREATE TABLE IF NOT EXISTS activities (");
        for (String key : keys) {
            activitiesSql.append(String.format("%s CHAR(32), ", key));
        }
        activitiesSql.append("user_id INTEGER NOT NULL, ");
        activitiesSql.append("id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        activitiesSql.append("FOREIGN KEY (user_id) REFERENCES users(id));");

        try {
            connect();
            stmt = c.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT,name CHAR(128))");
            stmt.executeUpdate(activitiesSql.toString());
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            close();
        }
    }

    private void createFirstUser() {
        try {
            connect();
            stmt = c.createStatement();
            stmt.executeUpdate("INSERT INTO users (id,name) VALUES (1, 'Dan');");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            close();
        }
    }

    public void write(ArrayList<BaseActivity> activityList) {

    }

    public void read(ArrayList<BaseActivity> activityList) {
        connect();
        try {
            BaseActivity instance;
            String[] keys = ActivityHandler.getUniqueKeys();

            StringBuilder sql = new StringBuilder("SELECT ");
            for (String key : keys) {
                sql.append(String.format("activities.%s, ", key));
            }
            sql.append("activities.id, users.name FROM activities ");
            sql.append("INNER JOIN users ON activities.user_id = users.id ");
            sql.append("ORDER BY activities.date;");

            res = stmt.executeQuery(sql.toString());

            while (res.next()) {
                String name = res.getString("name");
                instance = ActivityHandler.newActivity(name);
                if (instance != null) {
                    for (int i = 0; i < keys.length; i++) {
                        instance.set(keys[i], res.getString(i+1));
                    }
                    activityList.add(instance);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            close();
        }
    }

    public String[][] getRows(int user_id) {
        try {
            List<String[]> activityList = new ArrayList<>();
            String[] keys = ActivityHandler.getUniqueKeys();

            StringBuilder sql = new StringBuilder("SELECT ");
            for (String key : keys) {
                sql.append(String.format("%s, ", key));
            }
            sql.append("id FROM activities ");
            sql.append(String.format("WHERE user_id = %d ", user_id));
            sql.append("ORDER BY date;");

            System.out.println(sql.toString());

            res = stmt.executeQuery(sql.toString());
            BaseActivity instance;

            while (res.next()) {
                String name = res.getString("name");
                instance = ActivityHandler.newActivity(name);
                if (instance != null) {
                    for (int i = 0; i < keys.length; i++) {
                        instance.set(keys[i], res.getString(i+1));
                    }
                    activityList.add(instance.getArray(keys));
                }
            }
            return activityList.toArray(String[][]::new);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            close();
        }
        return null;
    }
}
