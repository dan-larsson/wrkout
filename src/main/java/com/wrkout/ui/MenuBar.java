package com.wrkout.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

public class MenuBar extends JMenuBar {

    public static final String LABEL_ABOUT = "Om";
    public static final String LABEL_PREFS = "Inställningar";
    public static final String LABEL_QUIT = "Avsluta";

    private int preferredMetaKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

    private JMenu menuFile;

    private static Hashtable menuItemsHashtable = new Hashtable();

    private Object[][] fileItems =
            {
                    {LABEL_ABOUT, KeyEvent.VK_E},
                    {LABEL_PREFS, KeyEvent.VK_W},
                    {null, null},
                    {LABEL_QUIT, KeyEvent.VK_Q}
            };

    public MenuBar(JFrame frame) {
        menuFile = new JMenu("Arkiv");
        setupMenu(menuFile, fileItems, frame);
        this.add(menuFile);
    }

    private void dispatchEvent(ActionEvent evt, String tag) {
        JFrame frame = null;
        if (evt.getSource() instanceof JComponent) {
            frame = (JFrame) (((JComponent)evt.getSource()).getClientProperty("window"));
        }

        switch (tag) {
            case LABEL_ABOUT:
                callAbout(frame);
                break;
            case LABEL_PREFS:
                callPrefs(frame);
                break;
            case LABEL_QUIT:
                callQuit(frame);
                break;
        }
    }

    private void callAbout(JFrame frame) {
        System.out.println(LABEL_ABOUT);
    }

    private void callPrefs(JFrame frame) {
        System.out.println(LABEL_PREFS);
    }

    private void callQuit(JFrame frame) {
        System.out.println(LABEL_QUIT);
    }

    private ActionListener actionListenerHandler = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            Object src = evt.getSource();
            if (src instanceof JMenuItem) {
                String input = ((JMenuItem)src).getText();
                dispatchEvent(evt, input);
            }
        }
    };

    private void setupMenu(JMenu menu, Object[][] config, JFrame frame) {
        JMenuItem currentMenuItem;
        for (int i = 0; i < config.length; i++) {
            if (config[i][0] != null) {
                currentMenuItem = new JMenuItem((String)config[i][0]);
                if(config[i][1] != null) {
                    int keyCode = ((Integer)config[i][1]).intValue();
                    KeyStroke key = KeyStroke.getKeyStroke(keyCode, preferredMetaKey);
                    currentMenuItem.setAccelerator(key);
                }
                currentMenuItem.setEnabled(true);
                currentMenuItem.setActionCommand((String)config[i][0]);
                currentMenuItem.putClientProperty("window", frame);

                currentMenuItem.addActionListener(actionListenerHandler);

                menuItemsHashtable.put((String)config[i][0], currentMenuItem);

                menu.add(currentMenuItem);
            } else {
                javax.swing.JSeparator sep = new javax.swing.JSeparator();
                menu.add(sep);
            }
        }
    }
}
