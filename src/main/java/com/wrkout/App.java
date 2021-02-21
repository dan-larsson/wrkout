package com.wrkout;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;
import com.wrkout.ui.ActivityTable;
import com.wrkout.ui.ActivityTableModel;
import com.wrkout.ui.UserTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import static javax.swing.UIManager.getSystemLookAndFeelClassName;

public class App {

    private JPanel mainPanel;
    private JPanel toolBarPanel;
    private JPanel statusPanel;
    private String[] columnNames;
    private String[] keyNames;
    private JLabel[] labels;
    private JComponent[] fields;
    private ActivityTable table;
    private GridBagConstraints layoutConstraints;

    private int userId;

    public App() {
        try {
            UIManager.setLookAndFeel(getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        mainPanel = new JPanel(new BorderLayout());
        toolBarPanel = new JPanel(new GridBagLayout());
        statusPanel = new JPanel();
        userId = 1;

        keyNames = ActivityHandler.getUniqueKeys();
        columnNames = ActivityHandler.getUniqueLabels();

        labels = new JLabel[keyNames.length];
        fields = new JComponent[keyNames.length];

        table = new ActivityTable(fields, userId);


        layoutConstraints = new GridBagConstraints();
        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
        layoutConstraints.anchor = GridBagConstraints.NORTH;
        layoutConstraints.gridheight = (keyNames.length*2) + 1;
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;
        layoutConstraints.insets = new Insets(10,10,10,0);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(toolBarPanel, BorderLayout.LINE_END);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        mainPanel.setOpaque(true);

    }

    public static void main(String[] args) {
        try {
            System.setProperty( "com.apple.mrj.application.apple.menu.about.name", "Workout" );
            System.setProperty( "com.apple.macos.useScreenMenuBar", "true" );
            System.setProperty( "apple.laf.useScreenMenuBar", "true" ); // for older versions of Java
        } catch (SecurityException e) {
            /* probably running via webstart, do nothing */
        }
        UserTableModel userModel = new UserTableModel();
        ActivityTableModel activityModel = new ActivityTableModel();

        try {
            userModel.createTables();
            activityModel.createTables();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userModel.disconnectFromDatabase();
            activityModel.disconnectFromDatabase();
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private int addFieldGroup(int index, int row) {
        fields[index] = newField(keyNames[index]);
        labels[index] = newLabelFor(fields[index], columnNames[index]);

        layoutConstraints.insets = new Insets(5,15,0,10);
        layoutConstraints.gridy = row;

        toolBarPanel.add(labels[index], layoutConstraints);

        row += 1;

        layoutConstraints.insets = new Insets(0,10,0,10);
        layoutConstraints.gridy = row;

        toolBarPanel.add(fields[index], layoutConstraints);

        row += 1;

        return row;
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Workout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        com.wrkout.ui.MenuBar menuBar = new com.wrkout.ui.MenuBar(frame);
        frame.setJMenuBar(menuBar);

        App ui = new App();
        frame.setContentPane(ui.mainPanel);

        ui.layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
        ui.layoutConstraints.weighty = 0;
        ui.layoutConstraints.gridx = 1;
        ui.layoutConstraints.gridheight = 1;
        ui.layoutConstraints.ipady = 10;
        ui.layoutConstraints.ipadx = 10;

        int fieldRow = 0;
        for (int i = 0; i < ui.keyNames.length; i++) {
            fieldRow = ui.addFieldGroup(i, fieldRow);
        }

        ui.table.setFields(ui.fields);

        JButton sub = new JButton("Spara");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(100, 40);
        sub.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] keys = new String[ui.fields.length];
                String[] vals = new String[ui.fields.length];
                int index = 0;
                BaseActivity instance = null;
                String text = null;

                for (JComponent obj : ui.fields) {
                    String name = obj.getName();
                    switch (name) {
                        case "name":
                            text = (String) ((JComboBox) obj).getSelectedItem();
                            instance = ActivityHandler.newActivity(text);
                        default:
                            if (obj instanceof JTextField) {
                                text = ((JTextField) obj).getText();
                            } else if (obj instanceof JComboBox) {
                                text = (String) ((JComboBox) obj).getSelectedItem();
                            }

                            keys[index] = name;
                            vals[index] = text;

                            index += 1;

                    }
                }
                if (instance != null) {
                    for (int i = 0; i < keys.length; i++) {
                        if (keys[i] != null && vals[i] != null) {
                            instance.set(keys[i], vals[i]);
                        }
                    }
                    ui.table.addActivity(instance, ui.userId);
                }
            }
        });

        ui.layoutConstraints.insets = new Insets(25,10,10,10);
        ui.layoutConstraints.gridy = fieldRow;
        ui.layoutConstraints.ipady = 20;
        ui.layoutConstraints.weighty = 1;

        ui.toolBarPanel.add(sub, ui.layoutConstraints);


        ui.statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 40));
        ui.statusPanel.setLayout(new BoxLayout(ui.statusPanel, BoxLayout.X_AXIS));
        JLabel statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        ui.statusPanel.add(statusLabel);

        frame.pack();
        frame.setVisible(true);
    }

    private static JLabel newLabelFor(JComponent field, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setSize(100, 30);
        lbl.setLabelFor(field);

        return lbl;
    }

    private static JComponent newField(String fieldName) {
        JComponent field = null;
        String defaultValue = BaseActivity.getDefaultValue(fieldName);
        switch (fieldName) {
            case "name":
                field = new JComboBox();
                for (String s : ActivityHandler.oneOfEach()) {
                    ((JComboBox) field).addItem(s);
                }
                break;

            case "date":
                field = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
                if (defaultValue != null) {
                    ((JFormattedTextField) field).setText(defaultValue);
                }
                break;

            default:
                if (defaultValue != null) {
                    field = new JTextField(defaultValue);
                } else {
                    field = new JTextField();
                }
        }

        field.setSize(300, 30);
        field.setName(fieldName);
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        return field;
    }
}
