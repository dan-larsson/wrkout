package com.wrkout.ui;

import com.wrkout.App;
import com.wrkout.activites.BaseActivity;
import com.wrkout.activites.Summary;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class SummaryTable extends JTable {

    private int userId;
    private App app;

    public SummaryTable(App app, int userId) {
        super(new SummaryTableModel(true));
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

        this.addMouseListener(new SummaryPopupClickListener(this));
        this.addMouseListener(new SummaryTableMouseAdapter());
        this.addKeyListener(new SummaryTableKeyListener());
    }

    public void displayActivities(String dateString) {
        app.displayActivities(dateString);
    }

    public void copyActivities() {
        String newDateString = BaseActivity.getDefaultValue("date");
        Summary selected;
        String result = null;
        int row;

        row = getSelectedRow();
        selected = ((SummaryTableModel) getModel()).getSummary(convertRowIndexToModel(row));
        if (selected != null) {
            result = (String)JOptionPane.showInputDialog(
                    this,
                    "Till vilket datum vill du kopiera  träningspasset?",
                    "Kopiera träningspass",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    newDateString
            );
            // TODO: validate input string "result"
            ((SummaryTableModel)this.dataModel).copyActivities(userId, selected.get("date"),  result);
        }

    }

    public void deleteActivities() {
        Summary selected;
        int row;

        row = getSelectedRow();
        selected = ((SummaryTableModel) getModel()).getSummary(convertRowIndexToModel(row));
        if (selected != null) {
            int n = JOptionPane.showConfirmDialog(
                    this,
                    String.format("Är det säkert att du vill ta bort\nträningspasset %s?", selected.get("date")),
                    "Ta bort träningspass",
                    JOptionPane.YES_NO_OPTION
            );
            if (n == 0) {
                ((SummaryTableModel)this.dataModel).deleteActivities(userId, selected.get("date"));
            }
        }
    }

    public void reload() {
        ((SummaryTableModel)this.dataModel).loadActivities();
    }
}
