package com.wrkout.ui;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;
import com.wrkout.activites.Summary;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class SummaryTableModel extends AbstractTableModel {

    private final String[] columnNames;
    private final String[] columnKeys;

    private ArrayList<Summary> summaryList;
    private ActivityHandler activityHandler;

    public SummaryTableModel() {
        this(false);
    }

    public SummaryTableModel(boolean setQuery) {
        columnKeys = Summary.getKeys();
        columnNames = Summary.getLabels();

        summaryList = new ArrayList<>();
        activityHandler = new ActivityHandler();

        if (setQuery) {
            this.loadActivities();
        }
    }

    public void deleteActivities(int userId, String dateString) {
        try {
            activityHandler.deleteActivities(userId, dateString);
            loadActivities();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void copyActivities(int userId, String oldDateString, String newDateString) {
        try {
            activityHandler.copyActivities(userId, oldDateString, newDateString);
            loadActivities();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
        return summaryList.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return summaryList.get(row).get(columnKeys[column]);
    }

    public Summary getSummary(int row) {
        try {
            return summaryList.get(row);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void loadActivities() {
        summaryList = new ArrayList<>();
        try {
            String current = "";
            Summary summary = null;
            for (BaseActivity activity : activityHandler.getActivities()) {
                if (!current.equals(activity.get("date"))) {
                    if (summary != null) {
                        summaryList.add(summary);
                    }
                    current = activity.get("date");
                    summary = new Summary(activity.getDate());
                }
                summary.add(activity);
            }
            summaryList.add(summary);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        fireTableStructureChanged();
    }
}
