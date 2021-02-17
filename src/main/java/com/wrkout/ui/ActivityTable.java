package com.wrkout.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityTable extends JTable {

    private ActivityTableSelectionListener tableListener;

    public ActivityTable(Object[][] data, Object[] columnNames, JComponent[] fields) {
        super();

        DefaultTableModel model = new DefaultTableModel(null, columnNames);

        setPreferredScrollableViewportSize(new Dimension(800, 600));

        setFillsViewportHeight(true);
        setRowHeight(30);
        setRowMargin(1);
        setShowHorizontalLines(true);
        setShowGrid(true);

        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableListener = new ActivityTableSelectionListener(this, fields);
        selectionModel.addListSelectionListener(tableListener);

        this.addMouseListener(new PopupClickListener(this));

        for (Object[] row : data) model.addRow(row);
        setModel(model);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(getModel());
        List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        setRowSorter(sorter);
    }

    public void removeRow(int rowToRemove) {
        ((DefaultTableModel)this.dataModel).removeRow(rowToRemove);
    }

    public void addRow(Object[] row) {
        ((DefaultTableModel)this.dataModel).addRow(row);
    }

    public void addRows(Object[][] data) {
        for (Object[] row : data) addRow(row);
    }

    public void setFields(JComponent[] fields) {
        this.tableListener.setFields(fields);
    }
}
