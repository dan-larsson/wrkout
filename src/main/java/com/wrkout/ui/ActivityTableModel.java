package com.wrkout.ui;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ActivityTableModel extends AbstractTableModel {

    private final String[] columnNames;
    private final String[] columnKeys;

    private ArrayList<BaseActivity> activityList;
    private ActivityHandler activityHandler;

    public ActivityTableModel() {
        columnNames = ActivityHandler.getUniqueLabels();
        columnKeys = ActivityHandler.getUniqueKeys();

        activityList = new ArrayList<>();
        activityHandler = new ActivityHandler();
    }

    public Class getColumnClass(int column) {
        return String.class;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public int getRowCount() {
        return activityList.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return activityList.get(row).get(columnKeys[column]);
    }

    public BaseActivity getActivity(int row) {
        return activityList.get(row);
    }

    public void loadActivities(String dateString) {
        try {
            activityList = activityHandler.getActivities(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        fireTableStructureChanged();
    }

    public void addActivity(BaseActivity activity, int user_id) {
        try {
            activityHandler.createActivity(activity, user_id);
            activityList.add(activity);

            fireTableStructureChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRow(Object[] row, int user_id) {
        try {
            activityHandler.createActivity(row, user_id);

            fireTableStructureChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeRow(int rowNum, int activityId) {
        try {
            activityHandler.deleteActivity(rowNum, activityId);
            activityList.remove(rowNum);

            fireTableStructureChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
