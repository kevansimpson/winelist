package com.sccpa.winelist.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;

public class CurrencyRenderer extends JLabel implements TableCellRenderer {

    public CurrencyRenderer() {
        setHorizontalAlignment(JLabel.RIGHT);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        final NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        setText(numberFormat.format((double) value));

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
