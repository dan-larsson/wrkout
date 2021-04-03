package com.wrkout.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonFactory {

    public static JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);

        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 15));
        button.setSize(100, 40);
        button.setBorder(new CompoundBorder(line, margin));
        button.addActionListener(listener);
        return button;
    }
}
