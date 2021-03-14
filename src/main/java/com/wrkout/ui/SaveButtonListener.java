package com.wrkout.ui;

import com.wrkout.App;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveButtonListener  implements ActionListener {

    private App app;

    public SaveButtonListener(App app) {
        this.app = app;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        app.submitActivity();
    }

}
