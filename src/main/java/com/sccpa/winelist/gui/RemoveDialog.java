package com.sccpa.winelist.gui;

import com.sccpa.winelist.data.WineEntry;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RemoveDialog extends AbstractDialog {

    public RemoveDialog(final AppFrame frame, final WineEntry entry) {
        super(frame, "Remove Entry?", entry, "Are you sure you want to remove this entry?");
    }

    @SuppressWarnings("unused")
    @Override
    void handleAction(final ActionEvent event) {
        getAppFrame().delete(collectEntry());
        setVisible(false);
    }

    @Override
    JPanel buildRow(JTextField field, String labelText) {
        field.setEditable(false);
        return super.buildRow(field, labelText);
    }
}
