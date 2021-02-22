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
        this(false);
    }

    public ActivityTableModel(boolean setQuery) {
        columnNames = ActivityHandler.getUniqueLabels();
        columnKeys = ActivityHandler.getUniqueKeys();

        activityList = new ArrayList<>();
        activityHandler = new ActivityHandler();

        if (setQuery) {
            this.loadActivities();
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

    public void loadActivities() {
        try {
            activityList = activityHandler.getActivities();
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
