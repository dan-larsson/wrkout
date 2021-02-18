package com.wrkout.ui;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
        int[] selectedIndices = lsm.getSelectedIndices();
        if (selectedIndices.length > 0) {
            int selectedIndex = selectedIndices[0];
            BaseActivity selected = ((SQLiteTableModel) table.getModel()).getActivity(table.convertRowIndexToModel(selectedIndex));
            String[] keys = ActivityHandler.getUniqueKeys();
            for (int index = 0; index < keys.length; index++) {
                try {
                    String value = selected.get(keys[index]);
                    if (fields[index] instanceof JTextField) {
                        ((JTextField) fields[index]).setText(value);
                    } else if (fields[index] instanceof JComboBox) {
                        ((JComboBox) fields[index]).setSelectedItem(value);
                    }
                } catch (Exception exc) {
                    System.out.println(exc);
                }
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
