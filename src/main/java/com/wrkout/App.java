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
    private GridBagConstraints layoutConstraints;

    private int userId;

    public App() {
        mainPanel = new JPanel(new GridBagLayout());
        userId = 1;

        activityHandler = new ActivityHandler();

        keyNames = ActivityHandler.getUniqueKeys();
        columnNames = ActivityHandler.getUniqueLabels();

        labels = new JLabel[keyNames.length];
        fields = new JComponent[keyNames.length];

        data = activityHandler.getRows(userId);
        table = new ActivityTable(data, columnNames, fields);

        layoutConstraints = new GridBagConstraints();
        layoutConstraints.fill = GridBagConstraints.BOTH;
        layoutConstraints.gridheight = (keyNames.length*2) + 1;
        layoutConstraints.gridx = 0;
        layoutConstraints.gridy = 0;
        layoutConstraints.weightx = 0.6;
        layoutConstraints.insets = new Insets(10,10,10,0);

        mainPanel.add(new JScrollPane(table), layoutConstraints);
        mainPanel.setOpaque(true);

    }

    public static void main(String[] args) {
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

        mainPanel.add(labels[index], layoutConstraints);

        row += 1;

        layoutConstraints.insets = new Insets(0,10,0,10);
        layoutConstraints.gridy = row;

        mainPanel.add(fields[index], layoutConstraints);

        row += 1;

        return row;
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        App ui = new App();
        frame.setContentPane(ui.mainPanel);

        ui.layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
        ui.layoutConstraints.weightx = 0.4;
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
                    ui.activityHandler.addActivity(instance, ui.userId);
                    ui.table.addRow(instance.getArray(keys));
                }
            }
        });

        ui.layoutConstraints.insets = new Insets(25,10,10,10);
        ui.layoutConstraints.gridy = fieldRow;
        ui.layoutConstraints.ipady = 20;

        ui.mainPanel.add(sub, ui.layoutConstraints);

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
