package com.sccpa.winelist.gui;

import com.sccpa.winelist.data.WineEntry;
import com.sccpa.winelist.service.WineService;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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
            tableView.updateData(fetchEntireList());
            cardLayout.show(cardPanel, TABLE_CARD);
            return true;
        }

        return false;
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

    public void setMessage(boolean error, Object... messages) {
        messageLabel.setForeground(error ? Color.RED : Color.GREEN.darker().darker());
        messageLabel.setText(StringUtils.join(messages));
        messageLabel.setVisible(true);
    }

    private void initUI() {
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

//        final JButton btn = new JButton("go back");
//        btn.addActionListener(event -> cardLayout.show(cardPanel, LOGIN_CARD));
//        final JPanel panel = new JPanel();
//        panel.add(btn);
        tableView = new TableView(this);
        cardPanel.add(tableView, TABLE_CARD);
        getContentPane().add(cardPanel, BorderLayout.CENTER);
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
