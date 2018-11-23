package com.sccpa.winelist.gui;

import com.sccpa.winelist.data.WineEntry;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableView extends AbstractPanel {

    private static float[] WIDTHS = {
            2.5f, 20.0f, 15.0f, 12.0f, 5.0f, 7.0f, 5.0f, 10.0f, 10.0f, 10.0f, 2.5f
    };

    private JLabel bottleCountLbl;
    private JLabel totalValueLbl;
    private JLabel filterLbl;
    private JTable wineTable;
    private WineTableModel wineTableModel;
    private TableRowSorter<WineTableModel> sorter;

    public TableView(final AppFrame frame) {
        super(frame);
        wineTableModel = new WineTableModel(
                getIcon("/icons/Go.gif"), getIcon("/icons/No-entry.gif"));
        initUI();
    }

    public WineEntry getSelectedBottle() {
        final int row = wineTable.convertRowIndexToModel(wineTable.getSelectedRow());
        return wineTableModel.getEntryAt(row);
    }

    public Pair<Integer, Integer> getSelectedCell() {
        final int row = wineTable.convertRowIndexToModel(wineTable.getSelectedRow());
        return Pair.of(row, wineTable.getSelectedColumn());
    }

    public void removeData(final WineEntry newData) {
        if (newData != null) {
            final List<WineEntry> fullList = wineTableModel.getWineEntryList();
            fullList.remove(newData);
            updateData(fullList);
            wineTable.setRowSelectionInterval(0, 0);
        }
    }

    public void updateData(final WineEntry newData) {
        if (newData != null) {
            final List<WineEntry> fullList = wineTableModel.getWineEntryList()
                    .stream()
                    .filter(entry -> entry.getId() != newData.getId())
                    .collect(Collectors.toList());
            fullList.add(0, newData);
            updateData(fullList);
            wineTable.setRowSelectionInterval(0, 0);
        }
    }

    public void updateData(final List<WineEntry> newData) {
        wineTable.clearSelection();
        if (newData == null) {
            wineTableModel.updateData(new ArrayList<>());
            bottleCountLbl.setText("0");
            totalValueLbl.setText("0.00");
        } else {
            wineTableModel.updateData(newData);
            final int bottleCount = newData.stream().mapToInt(WineEntry::getQty).sum();
            final double totalValue = newData.stream()
                    .mapToDouble(entry -> (double) entry.getQty() * entry.getPrice()).sum();

            bottleCountLbl.setText(String.valueOf(bottleCount));
            bottleCountLbl.paintImmediately(bottleCountLbl.getVisibleRect());
            totalValueLbl.setText(String.valueOf(totalValue));
            totalValueLbl.paintImmediately(totalValueLbl.getVisibleRect());
        }
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));

        bottleCountLbl = new JLabel("0");
        totalValueLbl = new JLabel("0.00");
        filterLbl = new JLabel("0 filter(s)");

        final JButton createBtn = new JButton("Create New", getIcon("/icons/Create.gif"));
        createBtn.addActionListener(event -> {
            final CreateDialog dialog = new CreateDialog(getAppFrame());
            dialog.setVisible(true);
        });
        final JPanel totalsPnl = wrap(new JLabel("Bottle Count: "), bottleCountLbl);
        final JPanel valuesPnl = wrap(new JLabel("Collection Value: USD "), totalValueLbl);
        final JButton filterBtn = new JButton("Filter Data", getIcon("/icons/Filter.gif"));
        filterBtn.addActionListener(event -> {
            // TODO implement
            JOptionPane.showMessageDialog(null, "Not implemented yet... sorry!");
        });
        final JPanel northPnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 5));
        Stream.of(createBtn, totalsPnl, valuesPnl, filterBtn, filterLbl).forEach(northPnl::add);
        add(northPnl, BorderLayout.NORTH);

        wineTable = new JTable(wineTableModel);
        sorter = new TableRowSorter<>(wineTableModel);
        wineTable.setRowSorter(sorter);
//        wineTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        wineTable.setFillsViewportHeight(true);
        wineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wineTable.setRowHeight(36);
        final Dimension dim = new Dimension(20,1);
        wineTable.setIntercellSpacing(new Dimension(dim));
        wineTable.setDefaultRenderer(ImageIcon.class, new ButtonRenderer());
        wineTable.setDefaultEditor(ImageIcon.class, new ButtonEditor(this));
        wineTable.setDefaultRenderer(Double.class, new CurrencyRenderer());
        wineTable.setDefaultRenderer(Integer.class, new NumericRenderer());

        // table may not be visible, ergo width=0
        final int tableWidth = Toolkit.getDefaultToolkit().getScreenSize().width - 50;
        for (int i = 0; i < WIDTHS.length; i++) {
            final TableColumn column = wineTable.getTableHeader().getColumnModel().getColumn(i);
            int pWidth = Math.round(WIDTHS[i] / 100 * tableWidth);
            column.setMinWidth(pWidth);
            column.setMaxWidth(pWidth);
            column.setPreferredWidth(pWidth);
        }
        JScrollPane scrollPane = new JScrollPane(wineTable);
        add(scrollPane, BorderLayout.CENTER);
    }
}
