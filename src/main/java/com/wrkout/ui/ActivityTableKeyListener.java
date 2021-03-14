package com.wrkout.ui;

import java.awt.event.KeyEvent;

public class ActivityTableKeyListener implements java.awt.event.KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
        ActivityTable target = (ActivityTable) e.getSource();
        switch (e.getKeyChar()) {
            case '-':
                target.deleteActivity();
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
