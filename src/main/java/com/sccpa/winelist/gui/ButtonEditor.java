package com.sccpa.winelist.gui;


import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends AbstractCellEditor
                          implements TableCellEditor, ActionListener {

    protected static final String EDIT = "edit";

    private ImageIcon currentIcon;
    private JButton button;
    private TableView tableView;

    public ButtonEditor(final TableView view) {
        tableView = view;
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);
    }

    public void actionPerformed(ActionEvent e) {
        final Pair<Integer, Integer> selectedCell = tableView.getSelectedCell();
        switch (selectedCell.getRight()) {
            case 0: // edit
                final EditDialog edit = new EditDialog(
                        tableView.getAppFrame(), tableView.getSelectedBottle());
                edit.setVisible(true);
                break;
            case 10: // remove
                final RemoveDialog remove = new RemoveDialog(
                        tableView.getAppFrame(), tableView.getSelectedBottle());
                remove.setVisible(true);
                break;
        }
        //Make the renderer reappear.
        fireEditingStopped();
    }

    //Implement the one CellEditor method that AbstractCellEditor doesn't.
    public Object getCellEditorValue() {
        return currentIcon;
    }

    //Implement the one method defined by TableCellEditor.
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column) {
        currentIcon = (ImageIcon) value;
        return button;
    }}
