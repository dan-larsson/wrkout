package com.wrkout.ui;

import com.wrkout.activites.BaseActivity;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityTable extends JTable {

    private int userId;
    private ActivityTableSelectionListener tableListener;

    public ActivityTable(JComponent[] fields, int userId) {
        super(new ActivityTableModel(true));
        this.userId = userId;

        setPreferredScrollableViewportSize(new Dimension(800, 600));

        JTableHeader header = this.getTableHeader();
        header.setPreferredSize(new Dimension(100, 32));

        getColumnModel().getColumn(0).setCellRenderer(new RowNumberRenderer());
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

    public void setFields(JComponent[] fields) {
        this.tableListener.setFields(fields);
    }

    private static class RowNumberRenderer extends DefaultTableCellRenderer {
        public RowNumberRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (table != null) {
                JTableHeader header = table.getTableHeader();
                if (header != null) {
                    setForeground(header.getForeground());
                    setBackground(header.getBackground());
                    setFont(header.getFont());
                }
            }

            if (isSelected) {
                setFont(getFont().deriveFont(Font.BOLD));
            }

            setText((value == null) ? "" : value.toString());
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));

            return this;
        }
    }
}
