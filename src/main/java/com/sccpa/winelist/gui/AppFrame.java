package com.sccpa.winelist.gui;

import com.sccpa.winelist.data.WineEntry;
import com.sccpa.winelist.service.WineService;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Consumer;

public class AppFrame extends JFrame implements WineService {

    private static final String LOGIN_CARD = "LoginCard";
    private static final String TABLE_CARD = "TableCard";

    private final WineService wineService;

    private JLabel messageLabel;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private TableView tableView;

    public AppFrame(final WineService service) {
        super("JMS Winelist");
        wineService = service;
        initUI();
    }

    public void reloadData() {
        tableView.updateData(fetchEntireList());
    }

    public void printData() {
        // TODO implement
        JOptionPane.showMessageDialog(null, "Not implemented yet... sorry!");
    }

    public void setMessage(boolean error, Object... messages) {
        messageLabel.setForeground(error ? Color.RED : Color.GREEN.darker().darker());
        messageLabel.setText(StringUtils.join(messages));
        messageLabel.setVisible(true);
    }

    @Override
    public boolean isLoggedIn() {
        return wineService.isLoggedIn();
    }

    @Override
    public boolean login(String user, String pswd) {
        boolean login = false;
        try {
            login = wineService.login(user, pswd);
        } catch (Exception ex) {
            setMessage(true, "Cannot connect to service: ", ex.getMessage());
        }

        if (login) {
            setMessage(false);
            reloadData();
            gotoView(TABLE_CARD);
            return true;
        }

        return false;
    }

    @Override
    public void logout() {
        wineService.logout();
        gotoView(LOGIN_CARD);
    }

    @Override
    public List<WineEntry> fetchEntireList() {
        return wineService.fetchEntireList();
    }

    @Override
    public WineEntry fetchEntry(final long id) {
        return wineService.fetchEntry(id);
    }

    @Override
    public WineEntry insert(WineEntry entry) {
        final WineEntry inserted = wineService.insert(entry);
        tableView.updateData(inserted);
        return inserted;
    }

    @Override
    public WineEntry update(WineEntry entry) {
        final WineEntry updated = wineService.update(entry);
        tableView.updateData(updated);
        return updated;
    }

    @Override
    public WineEntry delete(WineEntry entry) {
        tableView.removeData(entry);
        return wineService.delete(entry);
    }

    private void gotoView(final String cardId) {
        cardLayout.show(cardPanel, cardId);
    }

    private void initUI() {
        createMenu();
        createLayout();

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //height of the task bar
        final Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        //available size of the screen
        setSize(screenSize.width, screenSize.height - scnMax.bottom);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void createLayout() {
        final Container pane = getContentPane();
        pane.setLayout(new BorderLayout(5, 5));

        messageLabel = new JLabel("");
        messageLabel.setMinimumSize(new Dimension(500, 80));
        pane.add(messageLabel, BorderLayout.NORTH);

        cardPanel = new JPanel((cardLayout = new CardLayout(5, 5)));
        final JPanel loginPnl = new JPanel(new FlowLayout());
        loginPnl.add(new LoginScreen(this));
        cardPanel.add(loginPnl, LOGIN_CARD);

        tableView = new TableView(this);
        cardPanel.add(tableView, TABLE_CARD);
        getContentPane().add(cardPanel, BorderLayout.CENTER);
    }

    private void createMenu() {
        final JMenuBar menuBar = new JMenuBar();
        final JMenu menu = new JMenu("Winelist");
        menu.setMnemonic(KeyEvent.VK_W);
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("Print List",
                new ImageIcon(getClass().getResource("/icons/Report.gif")));
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.addActionListener(listener(event -> printData()));
        menu.add(menuItem);

        menuItem = new JMenuItem("Reload Data",
                new ImageIcon(getClass().getResource("/icons/heineken.png")));
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.addActionListener(listener(event -> reloadData()));
        menu.add(menuItem);

        menuItem = new JMenuItem("Create New Entry",
                new ImageIcon(getClass().getResource("/icons/Create.gif")));
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.addActionListener(listener(event -> {
            final CreateDialog dialog = new CreateDialog(AppFrame.this);
            dialog.setVisible(true);
        }));
        menu.add(menuItem);

        menu.addSeparator();
        menuItem = new JMenuItem("Exit",
                new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        menuItem.setMnemonic(KeyEvent.VK_X);
        menuItem.addActionListener(event -> System.exit(0));
        menu.add(menuItem);
        setJMenuBar(menuBar);
    }

    private ActionListener listener(final Consumer<ActionEvent> action) {
        return event -> {
            if (isLoggedIn())
                action.accept(event);
            else {
                gotoView(LOGIN_CARD);
                setMessage(true, "Please login");
            }
        };
    }

    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }
}
