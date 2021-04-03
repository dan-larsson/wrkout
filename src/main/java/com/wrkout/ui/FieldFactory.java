package com.wrkout.ui;

import com.wrkout.activites.ActivityHandler;
import com.wrkout.activites.BaseActivity;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

public class FieldFactory {

    public static JComponent createField(String fieldName) {
        JComponent field = null;
        String defaultValue = BaseActivity.getDefaultValue(fieldName);
        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 5, 5, 5);

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
                field.setBorder(new CompoundBorder(line, margin));
                break;

            default:
                if (defaultValue != null) {
                    field = new JTextField(defaultValue);
                } else {
                    field = new JTextField();
                }
                field.setBorder(new CompoundBorder(line, margin));
        }

        field.setForeground(Color.BLACK);
        field.setBackground(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(100, 30));
        field.setName(fieldName);

        return field;
    }
}
