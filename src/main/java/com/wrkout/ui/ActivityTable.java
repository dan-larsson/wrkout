package com.wrkout.ui;

import com.wrkout.activites.BaseActivity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityTable extends JTable {

    private int userId;
    private ActivityTableSelectionListener tableListener;

    public ActivityTable(JComponent[] fields, int userId) {
        super(new SQLiteTableModel());
        this.userId = userId;

        setPreferredScrollableViewportSize(new Dimension(800, 600));

        setFillsViewportHeight(true);
        setRowHeight(30);
        setRowMargin(1);
        setShowHorizontalLines(true);
        setShowGrid(true);

        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableListener = new ActivityTableSelectionListener(this, fields);
        selectionModel.addListSelectionListener(tableListener);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(getModel());
        List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        setRowSorter(sorter);

        this.addMouseListener(new PopupClickListener(this));
    }

    public BaseActivity getActivity(int row) {
        return ((SQLiteTableModel)this.dataModel).getActivity(row);
    }

    public void removeRow(int rowNum, int activityId) {
        try {
            ((SQLiteTableModel) this.dataModel).removeRow(rowNum, activityId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRow(Object[] row) {
        try {
            ((SQLiteTableModel)this.dataModel).addRow(row, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addActivity(BaseActivity activity, int user_id) {
        try {
            ((SQLiteTableModel) this.dataModel).addActivity(activity, user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFields(JComponent[] fields) {
        this.tableListener.setFields(fields);
    }
}
