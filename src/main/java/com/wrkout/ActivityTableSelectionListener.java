package com.wrkout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class ActivityTableSelectionListener implements ListSelectionListener {

    private ActivityTable table;
    private JComponent[] fields;

    public ActivityTableSelectionListener(ActivityTable table, JComponent[] fields) {
        super();
        this.table = table;
        this.fields = fields;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        int selectedIndex = lsm.getSelectedIndices()[0];
        Vector selectedRow = ((DefaultTableModel)table.getModel()).getDataVector().elementAt(table.convertRowIndexToModel(selectedIndex));
        for (int index = 0; index < selectedRow.size(); index++) {
            try {
                String value = (String) selectedRow.get(index);
                System.out.println(fields[index]);
                if (fields[index] instanceof JTextField) {
                    ((JTextField) fields[index]).setText(value);
                } else if (fields[index] instanceof JComboBox) {
                    ((JComboBox) fields[index]).setSelectedItem(value);
                }
            } catch (Exception exc) {
                System.out.println("Doh");
                System.out.println(exc);
            }
        }
    }

    public void setFields(JComponent[] fields) {
        this.fields = fields;
    }

    public void setTable(ActivityTable table) {
        this.table = table;
    }
}
