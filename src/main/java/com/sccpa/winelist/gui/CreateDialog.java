package com.sccpa.winelist.gui;

import com.sccpa.winelist.data.WineEntry;

import java.awt.event.ActionEvent;

public class CreateDialog extends AbstractDialog {

    public CreateDialog(final AppFrame frame) {
        super(frame, "New Entry", new WineEntry(), "Add data and click Save Changes");
    }

    @SuppressWarnings("unused")
    @Override
    void handleAction(final ActionEvent event) {
        getAppFrame().insert(collectEntry());
        setVisible(false);
    }
}
