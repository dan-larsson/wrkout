package com.wrkout.ui;

import com.wrkout.App;
import com.wrkout.activites.BaseActivity;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityTable extends JTable {

    private int userId;
    private App app;
    private ActivityTableSelectionListener tableListener;

    public ActivityTable(App app, JComponent[] fields, int userId) {
        super(new ActivityTableModel());
        this.userId = userId;
        this.app = app;

        setPreferredScrollableViewportSize(new Dimension(800, 600));

        JTableHeader header = this.getTableHeader();
        header.setPreferredSize(new Dimension(100, 32));

        getColumnModel().getColumn(0).setPreferredWidth(5);

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

        this.addMouseListener(new ActivityPopupClickListener(this));
        this.addKeyListener(new ActivityTableKeyListener());
    }

    public void reload(String dateString) {
        ((ActivityTableModel)this.dataModel).loadActivities(dateString);
    }

    public BaseActivity getActivity(int row) {
        return ((ActivityTableModel)this.dataModel).getActivity(row);
    }

    public void removeRow(int rowNum, int activityId) {
        try {
            ((ActivityTableModel) this.dataModel).removeRow(rowNum, activityId);
            this.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRow(Object[] row) {
        try {
            ((ActivityTableModel)this.dataModel).addRow(row, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addActivity(BaseActivity activity, int user_id) {
        try {
            ((ActivityTableModel) this.dataModel).addActivity(activity, user_id);
            this.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteActivity() {
        BaseActivity selected;
        int selectedRow = getSelectedRow();
        int convertedRow;

        if (selectedRow == -1) {
            // No row selected, do nothing
            return;
        }

        convertedRow = convertRowIndexToModel(selectedRow);
        selected = ((ActivityTableModel) getModel()).getActivity(convertedRow);
        if (selected != null) {
            ((ActivityTableModel)this.dataModel).removeRow(convertedRow, selected.getId());
        }
    }

    public void setFields(JComponent[] fields) {
        this.tableListener.setFields(fields);
    }
}
