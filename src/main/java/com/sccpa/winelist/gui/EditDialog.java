package com.sccpa.winelist.gui;

import com.sccpa.winelist.data.WineEntry;
import java.awt.event.ActionEvent;

public class EditDialog extends AbstractDialog {

    public EditDialog(final AppFrame frame, final WineEntry entry) {
        super(frame, "Edit Wine", entry, "Edit and click Save Changes");
    }

    @SuppressWarnings("unused")
    @Override
    void handleAction(final ActionEvent event) {
        getAppFrame().update(collectEntry());
        setVisible(false);
    }
}
