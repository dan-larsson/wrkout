package com.wrkout.ui;

import com.wrkout.activites.Summary;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SummaryTableMouseAdapter extends MouseAdapter {

    public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2) {
            JTable target = (JTable)me.getSource();
            int row = target.getSelectedRow(); // select a row
            Summary selected = ((SummaryTableModel) target.getModel()).getSummary(target.convertRowIndexToModel(row));
            ((SummaryTable)target).displayActivities(selected.get("date"));
        }
    }
}
