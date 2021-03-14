package com.wrkout.ui;

import java.awt.event.KeyEvent;

public class SummaryTableKeyListener implements java.awt.event.KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
        SummaryTable target = (SummaryTable) e.getSource();
        switch (e.getKeyChar()) {
            case '+':
                target.copyActivities();
                break;
            case '-':
                target.deleteActivities();
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Not implemented
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not implemented
    }
}
