package com.wrkout;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;
import com.wrkout.ui.*;
import com.wrkout.user.UserHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import static javax.swing.UIManager.getSystemLookAndFeelClassName;

public class App {

    final String SUMMARY_PANEL = "Summary Panel";
    final String ACTIVITIES_PANEL = "Activities Panel";
    final String SETTINGS_PANEL = "Settings Panel";

    private JPanel cards;
    private JPanel summaryPanel;
    private JPanel activitiesPanel;
    private JPanel settingsPanel;
    private JPanel toolBarPanel;
    private JPanel topPanel;
    private JPanel statusPanel;
    private String[] columnNames;
    private String[] keyNames;
    private JLabel[] labels;
    private JComponent[] fields;
    private ActivityTable activityTable;
    private UserTable userTable;
    private SummaryTable summaryTable;
    private CardLayout cardLayout;


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

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        userId = 1;

        keyNames = ActivityHandler.getUniqueKeys();
        columnNames = ActivityHandler.getUniqueLabels();

        labels = new JLabel[keyNames.length];
        fields = new JComponent[keyNames.length];

        setupActivities();
        setupSummary();

        toolBarPanel = new JPanel(new GridBagLayout());
        statusPanel = new JPanel();
        topPanel = new JPanel();
        settingsPanel = new JPanel(new BorderLayout());


        userTable = new UserTable();

        cards.add(settingsPanel, SETTINGS_PANEL);
    }

    public String[] getKeyNames() {
        return keyNames;
    }

    public void submitActivity() {
        String[] keys = new String[fields.length];
        String[] vals = new String[fields.length];
        int index = 0;
        BaseActivity instance = null;
        String text = null;

        for (JComponent obj : fields) {
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
                if (keys[i] != null && vals[i] != null && vals[i] != "") {
                    instance.set(keys[i], vals[i]);
                }
            }
            activityTable.addActivity(instance, userId);
        }
        summaryTable.reload();
    }

    private void setupSummary() {
        summaryTable = new SummaryTable(this, userId);
        summaryPanel = new JPanel(new BorderLayout());

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        southPanel.add(Box.createHorizontalStrut(15));

        for (int i = 0; i < keyNames.length; i++) {
            //fields[i] = newField(keyNames[i]);
            fields[i] = FieldFactory.createField(keyNames[i]);
            labels[i] = newLabelFor(fields[i], keyNames[i], columnNames[i]);
            southPanel.add(labels[i]);
            southPanel.add(fields[i]);
            southPanel.add(Box.createHorizontalStrut(15));
        }

        activityTable.setFields(fields);

        JButton sub = ButtonFactory.createButton("Ny aktivitet", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitActivity();
            }
        });

        southPanel.add(sub);
        southPanel.add(Box.createHorizontalStrut(15));

        summaryPanel.add(new JScrollPane(summaryTable), BorderLayout.CENTER);
        summaryPanel.add(southPanel, BorderLayout.SOUTH);
        cards.add(summaryPanel, SUMMARY_PANEL);
    }

    private void setupActivities() {
        activityTable = new ActivityTable(this, fields, userId);
        activitiesPanel = new JPanel(new BorderLayout());

        JPanel southPanel = new JPanel();

        activitiesPanel.add(new JScrollPane(activityTable), BorderLayout.CENTER);

        southPanel.add(
                ButtonFactory.createButton(
                        "Tillbaka",
                        new BackButtonListener(this)
                )
        );

        activitiesPanel.add(southPanel, BorderLayout.SOUTH);
        cards.add(activitiesPanel, ACTIVITIES_PANEL);
    }

    public void displayActivities(String dateString) {
        activityTable.reload(dateString);
        cardLayout.show(cards, ACTIVITIES_PANEL);
    }

    public void displaySummary() {
        summaryTable.reload();
        cardLayout.show(cards, SUMMARY_PANEL);
    }

    public void displaySettings() {
        cardLayout.show(cards, SETTINGS_PANEL);
    }

    public static void main(String[] args) {
        try {
            System.setProperty( "com.apple.mrj.application.apple.menu.about.name", "Workout" );
            System.setProperty( "com.apple.macos.useScreenMenuBar", "true" );
            System.setProperty( "apple.laf.useScreenMenuBar", "true" ); // for older versions of Java
        } catch (SecurityException e) {
            /* probably running via webstart, do nothing */
        }
        UserHandler userHandler = new UserHandler();
        ActivityHandler activityHandler = new ActivityHandler();

        try {
            userHandler.createTables();
            activityHandler.createTables();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userHandler.disconnect();
            activityHandler.disconnect();
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }



    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Workout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);

        frame.setJMenuBar(new com.wrkout.ui.MenuBar(frame));

        App ui = new App();
        frame.setContentPane(ui.cards);


        ui.displaySummary();

        frame.pack();
        frame.setVisible(true);
    }

    protected ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getClassLoader().getResource(path);
        if (imgURL != null) {
            ImageIcon imageIcon = new ImageIcon(imgURL, description);
            Image image = imageIcon.getImage();
            Image newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(newimg);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private JLabel newLabelFor(JComponent field, String key, String text) {
        ImageIcon icon = createImageIcon(String.format("icons/%s.png", key), text);
        JLabel lbl;
        if (icon != null) {
            lbl = new JLabel(icon, JLabel.LEFT);
        } else {
            lbl = new JLabel(text, JLabel.LEFT);
        }

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
        field.setPreferredSize(new Dimension(100, 30));
        field.setName(fieldName);
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        return field;
    }
}
