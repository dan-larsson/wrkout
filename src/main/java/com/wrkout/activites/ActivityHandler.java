package com.wrkout.activites;

import com.wrkout.SQLite;
import com.wrkout.activites.continual.Running;
import com.wrkout.activites.repetitive.*;

import java.sql.*;
import java.util.*;

public class ActivityHandler extends SQLite {

    public ActivityHandler() {
        connect();
    }

    public ArrayList<BaseActivity> getActivities() throws IllegalStateException, SQLException {
        return getActivities("");
    }

    public ArrayList<BaseActivity> getActivities(String dateString) throws IllegalStateException, SQLException {
        ArrayList<BaseActivity> activityList = new ArrayList<>();
        BaseActivity instance;
        boolean first = true;
        StringBuilder sql = new StringBuilder("SELECT ");
        String[] keys = getUniqueKeys(true);
        String frmt;

        for (String key : keys) {
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
        if (dateString.length() > 0) {
            sql.append(String.format(" WHERE activities.date = '%s'", dateString));
        }
        sql.append(" ORDER BY activities.date;");

        ResultSet resultSet = executeQuery(sql.toString());

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String value;
            String key;

            instance = ActivityHandler.newActivity(name);
            if (instance != null) {
                int i;
                for (i = 0; i < keys.length; i++) {
                    value = resultSet.getString(i+1);
                    key = keys[i];
                    if (value != null) {
                        instance.set(key, value);
                    }
                }
                activityList.add(instance);
            }
        }

        return activityList;
    }


    /**
     * Return an array of activity names.
     * @return array of activity names.
     */
    public static String[] oneOfEach() {
        String[] all = new String[9];
        all[0] = BarbellCurl.NAME;
        all[1] = BarbellRow.NAME;
        all[2] = BarbellSquat.NAME;
        all[3] = BenchPress.NAME;
        all[4] = ChinUp.NAME;
        all[5] = Deadlift.NAME;
        all[6] = Running.NAME;
        all[7] = OverheadPress.NAME;
        all[8] = PullUp.NAME;

        return all;
    }

    public static String[] getUniqueKeys() {
        return getUniqueKeys(false);
    }


    public static String[] getUniqueKeys(boolean includeHidden) {
        String[] all = oneOfEach();
        String[] keys;
        List<String> uniqueKeys = new ArrayList<>();
        BaseActivity current;

        for (String objName : all) {
            current = newActivity(objName);
            assert current != null;
            keys = current.getKeys(includeHidden);
            for (String key : keys) {
                if (uniqueKeys.contains(key)) continue;
                uniqueKeys.add(key);
            }
        }

        return uniqueKeys.toArray(new String[0]);
    }

    public static String[] getUniqueLabels() {
        String[] all = oneOfEach();
        String[] keys;
        List<String> uniqueLabels = new ArrayList<>();
        List<String> uniqueKeys = new ArrayList<>();
        BaseActivity current;

        for (String objName : all) {
            current = newActivity(objName);
            keys = current.getKeys();
            for (String key : keys) {
                if (uniqueKeys.contains(key)) continue;
                uniqueKeys.add(key);
                uniqueLabels.add(current.getLabel(key));
            }
        }

        return uniqueLabels.toArray(new String[0]);
    }

    public static String[] getLabelsFor(String[] include) {
        String[] all = oneOfEach();
        String label;
        List<String> labels = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        BaseActivity current;

        for (String inc : include) {
            for (String objName : all) {
                if (keys.contains(objName)) continue;

                current = newActivity(objName);
                label = current.getLabel(inc);

                if (label != null) {
                    keys.add(objName);
                    labels.add(label);
                    break;
                }
            }
        }

        return labels.toArray(new String[0]);
    }

    public static String getCreateSQL() {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS activities (");
        for (String key : getUniqueKeys()) {
            sql.append(String.format("%s CHAR(32), ", key));
        }
        sql.append("user_id INTEGER NOT NULL, ");
        sql.append("id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sql.append("FOREIGN KEY (user_id) REFERENCES users(id));");

        return sql.toString();
    }

    /**
     * Return a new activity instance given its name.
     * @param type Activity type name
     * @return BaseActivity instance.
     */
    public static BaseActivity newActivity(String type) {
        switch (type) {
            case BarbellCurl.NAME: return new BarbellCurl();
            case BarbellRow.NAME: return new BarbellRow();
            case BarbellSquat.NAME: return new BarbellSquat();
            case BenchPress.NAME: return new BenchPress();
            case ChinUp.NAME: return new ChinUp();
            case Deadlift.NAME: return new Deadlift();
            case Running.NAME: return new Running();
            case OverheadPress.NAME: return new OverheadPress();
            case PullUp.NAME: return new PullUp();
            default:
                return null;
        }
    }

    public void createTables() throws IllegalStateException, SQLException {
        executeUpdate(getCreateSQL());
    }

    public void createActivity(BaseActivity activity, int user_id) throws IllegalStateException, SQLException {
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

        executeUpdate(sql.toString());
    }

    public void deleteActivities(int user_id, String dateString) throws IllegalStateException, SQLException {
        StringBuilder sql = new StringBuilder("DELETE FROM activities WHERE user_id = ");
        sql.append(user_id);
        sql.append(" AND date = \"").append(dateString).append("\";");


        executeUpdate(sql.toString());
    }

    public void copyActivities(int user_id, String oldDateString, String newDateString) throws IllegalStateException, SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO activities (user_id,date");
        String[] keys = ActivityHandler.getUniqueKeys();

        for (String key : keys) {
            if (key.equals("date"))  {
                continue;
            }
            sql.append(",");
            sql.append(key);
        }

        sql.append(") SELECT user_id,\"").append(newDateString).append("\"");

        for (String key : keys) {
            if (key.equals("date"))  {
                continue;
            }
            sql.append(",");
            sql.append(key);
        }

        sql.append(" FROM activities WHERE date = \"").append(oldDateString).append("\"");
        sql.append(" AND user_id = ").append(user_id).append(";");

        executeUpdate(sql.toString());
    }

    public void createActivity(Object[] row, int user_id) throws IllegalStateException, SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO activities (user_id");
        String[] keys = ActivityHandler.getUniqueKeys();

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

        executeUpdate(sql.toString());
    }

    public void deleteActivity(int rowNum, int activityId) throws IllegalStateException, SQLException {
        executeUpdate(String.format("DELETE FROM activities WHERE id = %d", activityId));
    }
}
