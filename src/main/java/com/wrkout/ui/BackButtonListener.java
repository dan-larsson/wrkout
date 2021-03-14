package com.wrkout.ui;

import com.wrkout.App;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackButtonListener implements ActionListener {

    private App app;

    public BackButtonListener(App app) {
        this.app = app;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        app.displaySummary();
    }

}
