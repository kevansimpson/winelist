package com.sccpa.winelist.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;

public class NumericRenderer extends JLabel implements TableCellRenderer {

    public NumericRenderer() {
        setHorizontalAlignment(JLabel.CENTER);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        setText(String.valueOf(value));

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        }
        else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        return this;
    }
}
