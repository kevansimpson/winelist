package com.sccpa.winelist.gui;

import com.sccpa.winelist.data.WineEntry;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class WineTableModel extends AbstractTableModel {
    private static String[] COLUMNS = {
            "", "Producer", "Name", "Type", "Year", "Price", "Qty", "Bin", "Ready", "Rating", ""
    };

    private final ImageIcon editIcon;
    private final ImageIcon removeIcon;

    @Getter
    private List<WineEntry> wineEntryList = new ArrayList<>();

    public WineTableModel(final ImageIcon edit, final ImageIcon remove) {
        editIcon = edit;
        removeIcon = remove;
    }

    public void updateData(final WineEntry newData) {
        // TODO implement
    }

    public void updateData(final List<WineEntry> newData) {
        wineEntryList = newData;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return wineEntryList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final WineEntry entry = getEntryAt(rowIndex);
        if (entry != null) {
            switch (columnIndex) {
                case 0:
                    return editIcon;
                case 1:
                    return entry.getProducer();
                case 2:
                    return entry.getName();
                case 3:
                    return entry.getType();
                case 4:
                    return entry.getYear();
                case 5:
                    return entry.getPrice();
                case 6:
                    return entry.getQty();
                case 7:
                    return entry.getBin();
                case 8:
                    return entry.getReady();
                case 9:
                    return entry.getRating();
                case 10:
                    return removeIcon;
            }
        }

        return ""; // avoids NPE in getColumnClass when table is empty
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 10:
                return ImageIcon.class;
            case 5:
                return Double.class;
            case 6:
                return Integer.class;
            default:
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0 || columnIndex == 10;
    }

    WineEntry getEntryAt(final int rowIndex) {
        return (WineEntry) CollectionUtils.get(wineEntryList, rowIndex);
    }
}
