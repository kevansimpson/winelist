package com.sccpa.winelist.gui;

import com.sccpa.winelist.data.WineEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AbstractDialog extends JDialog {

    private final WineEntry wineEntry;

    private JTextField producerFld;
    private JTextField nameFld;
    private JTextField typeFld;
    private JTextField yearFld;
    private JTextField priceFld;
    private JTextField quantityFld;
    private JTextField binFld;
    private JTextField readyFld;
    private JTextField ratingFld;

    AbstractDialog(final AppFrame frame, final String title,
                   final WineEntry entry, final String header) {
        super(frame, title, true);
        wineEntry = entry;
        initUI(header);
        populate();
    }

    abstract void handleAction(final ActionEvent event);

    WineEntry collectEntry() {
        wineEntry.setProducer(StringUtils.defaultIfBlank(producerFld.getText(), "A  Producer"));
        wineEntry.setName(StringUtils.defaultIfBlank(nameFld.getText(), ""));
        wineEntry.setType(StringUtils.defaultIfBlank(typeFld.getText(), ""));
        wineEntry.setYear(StringUtils.defaultIfBlank(yearFld.getText(), ""));
        wineEntry.setPrice(NumberUtils.toDouble(priceFld.getText(), 0.0d));
        wineEntry.setQty(NumberUtils.toInt(quantityFld.getText(), 0));
        wineEntry.setBin(StringUtils.defaultIfBlank(binFld.getText(), ""));
        wineEntry.setReady(StringUtils.defaultIfBlank(readyFld.getText(), ""));
        wineEntry.setRating(StringUtils.defaultIfBlank(ratingFld.getText(), ""));

        return wineEntry;
    }

    void populate() {
        producerFld.setText(wineEntry.getProducer());
        nameFld.setText(wineEntry.getName());
        typeFld.setText(wineEntry.getType());
        yearFld.setText(wineEntry.getYear());
        priceFld.setText(String.valueOf(wineEntry.getPrice()));
        quantityFld.setText(String.valueOf(wineEntry.getQty()));
        binFld.setText(wineEntry.getBin());
        readyFld.setText(wineEntry.getReady());
        ratingFld.setText(wineEntry.getRating());
    }

    AppFrame getAppFrame() {
        return (AppFrame) getOwner();
    }

    JPanel buildRow(final JTextField field, final String labelText) {
        final JLabel lbl = new JLabel(labelText);
        lbl.setLabelFor(field);
        final JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl.add(lbl);
        pnl.add(field);

        return pnl;
    }

    private void initUI(final String header) {
        final JPanel content = new JPanel(new GridLayout(11, 1, 5, 5));
        final JPanel headerPnl = new JPanel();
        headerPnl.setBorder(BorderFactory.createLineBorder(Color.blue));
        headerPnl.add(new JLabel(header, JLabel.CENTER));
        content.add(headerPnl);

        content.add(buildRow((producerFld = new JTextField(40)), "Producer: "));
        content.add(buildRow((nameFld = new JTextField(40)), "Name: "));
        content.add(buildRow((typeFld = new JTextField(40)), "Type: "));
        content.add(buildRow((yearFld = new JTextField(40)), "Year: "));
        content.add(buildRow((priceFld = new JTextField(40)), "Price: "));
        content.add(buildRow((quantityFld = new JTextField(40)), "Quantity: "));
        content.add(buildRow((binFld = new JTextField(40)), "Bin: "));
        content.add(buildRow((readyFld = new JTextField(40)), "Ready: "));
        content.add(buildRow((ratingFld = new JTextField(40)), "Rating: "));

        final JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPnl.add(buildButton("Cancel", (event) -> AbstractDialog.this.setVisible(false)));
        buttonPnl.add(buildButton("Save Changes", this::handleAction));
        content.add(buttonPnl);

        setContentPane(content);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private JButton buildButton(final String text, final ActionListener listener) {
        final JButton btn = new JButton(text);
        btn.addActionListener(listener);
        return btn;
    }

}
