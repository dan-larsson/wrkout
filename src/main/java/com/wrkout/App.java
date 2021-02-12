package com.wrkout;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class App {

    private JPanel mainPanel;
    private ActivityHandler activityHandler;
    private String[] columnNames;
    private Object[][] data;
    private String[] keyNames;
    private JLabel[] labels;
    private JComponent[] fields;
    private ActivityTable table;

    private int userId;

    public App() {
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        userId = 1;

        activityHandler = new ActivityHandler();

        keyNames = ActivityHandler.getUniqueKeys();
        columnNames = ActivityHandler.getUniqueLabels();

        c.gridheight = (keyNames.length*2) + 1;

        data = activityHandler.getRows(userId);
        table = new ActivityTable(data, columnNames, fields);
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.6;
        c.insets = new Insets(10,10,10,0);

        //Add the scroll pane to this panel.
        mainPanel.add(scrollPane, c);

    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        App newContentPane = new App();
        newContentPane.mainPanel.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane.mainPanel);

        newContentPane.labels = new JLabel[newContentPane.keyNames.length];
        newContentPane.fields = new JComponent[newContentPane.keyNames.length];

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.4;
        c.gridx = 1;

        int fieldRow = 0;
        for (int i = 0; i < newContentPane.keyNames.length; i++) {

            newContentPane.labels[i] = new JLabel(newContentPane.columnNames[i]);
            newContentPane.labels[i].setFont(new Font("Arial", Font.PLAIN, 12));
            newContentPane.labels[i].setSize(100, 30);
            newContentPane.labels[i].setLabelFor(newContentPane.fields[i]);

            c.insets = new Insets(5,15,0,10);  //top padding
            c.gridy = fieldRow;
            c.ipadx = 10;
            newContentPane.mainPanel.add(newContentPane.labels[i], c);

            fieldRow += 1;

            newContentPane.fields[i] = newField(newContentPane.keyNames[i]);

            c.insets = new Insets(0,10,0,10);  //top padding
            c.gridy = fieldRow;
            c.ipady = 10;
            c.ipadx = 10;

            newContentPane.mainPanel.add(newContentPane.fields[i], c);

            fieldRow += 1;

        }

        newContentPane.table.setFields(newContentPane.fields);

        JButton sub = new JButton("Spara");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(100, 40);
        sub.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] keys = new String[newContentPane.fields.length];
                String[] vals = new String[newContentPane.fields.length];
                int index = 0;
                BaseActivity instance = null;
                String text = null;

                for (JComponent obj : newContentPane.fields) {
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
                    newContentPane.activityHandler.addActivity(instance, newContentPane.userId);
                    newContentPane.table.addRow(instance.getArray(keys));
                }
            }
        });

        c.insets = new Insets(25,10,10,10);  //top padding
        c.gridy = fieldRow;
        c.ipady = 20;

        //sub.addActionListener(this);
        newContentPane.mainPanel.add(sub, c);

        frame.pack();
        frame.setVisible(true);
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
