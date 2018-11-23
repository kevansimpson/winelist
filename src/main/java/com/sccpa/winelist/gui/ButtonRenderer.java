package com.sccpa.winelist.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends AbstractPanel implements TableCellRenderer {

    private final JLabel button = new JLabel("", JLabel.CENTER);

    public ButtonRenderer() {
        super(null);
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 2, 3));
        add(button);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        button.setIcon((Icon) value);
        return this;
    }
}
