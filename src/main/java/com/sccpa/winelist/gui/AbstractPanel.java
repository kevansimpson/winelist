package com.sccpa.winelist.gui;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Stream;

public class AbstractPanel extends JPanel {

    private final AppFrame appFrame;

    AbstractPanel(final AppFrame frame) {
        appFrame = frame;
    }

    AbstractPanel(final LayoutManager layoutManager, final AppFrame frame) {
        super(layoutManager);
        appFrame = frame;
    }

    protected AppFrame getAppFrame() {
        return appFrame;
    }

    ImageIcon getIcon(final String resource) {
        return new ImageIcon(getClass().getResource(resource));
    }

    JPanel wrap(final JComponent... components) {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        Stream.of(components).forEach(panel::add);
        return panel;
    }
}
