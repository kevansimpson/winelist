package com.sccpa.winelist.gui;


import javax.swing.*;

public class LoginScreen extends AbstractPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen(final AppFrame frame) {
        super(frame);
        final BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);
        usernameField = new JTextField("", 20);
        passwordField = new JPasswordField("", 20);


        final JButton loginBtn = new JButton(" Login", getIcon("/icons/heineken.png"));
        loginBtn.addActionListener(event -> getAppFrame().login(
                usernameField.getText(), new String(passwordField.getPassword())));

        add(Box.createVerticalStrut(50));
        add(wrap(new JLabel("Username: "), usernameField));
        add(wrap(new JLabel("Password: "), passwordField));
        add(wrap(loginBtn));
        add(Box.createVerticalBox());
    }
}
