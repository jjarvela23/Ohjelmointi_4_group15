package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LoginView extends JPanel {

    JTextField username;
    JTextField password;
    JLabel topheader;
    JLabel usernameText;
    JLabel passwordText;
    JButton loginButton;
    JButton registerButton;
    JButton backButton;

    JPanel mainPanel = new JPanel();

    Dimension d = new Dimension(0, 10);

    UserDatabase userDatabase = new UserDatabase();

    public LoginView(Runnable goToMain, Runnable GoToRegister, UserView userView) {

        JPanel mainPanel = new JPanel();
        mainPanel.setSize(new Dimension(800,600));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(10, 10, 10, 10)));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        topheader = new JLabel("Kirjaudu sisään");

        backButton = new JButton("takaisin");
        backButton.addActionListener(e -> goToMain.run());

        loginButton = new JButton("kirjaudu");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (username.getText().isEmpty() || password.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "yksi tai useampi kenttä oli tyhjä", "virhe", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String uname = username.getText();
                    String pword = password.getText();
                    try {

                        if (userDatabase.login(uname, pword) > 0) {
                            //do login stuff
                            int userId = userDatabase.login(uname, pword);
                            Main.CurrentUser = userId;
                            userView.setUser();
                            JOptionPane.showMessageDialog(null, "kirjautuminen onnistui", "", JOptionPane.INFORMATION_MESSAGE);
                            goToMain.run();
                        } else {
                            JOptionPane.showMessageDialog(null, "käyttäjänimi tai salasana oli väärin", "virhe", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "käyttäjänimi tai salasana oli väärin", "virhe", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        registerButton = new JButton("rekisteröidy");
        registerButton.addActionListener(e -> GoToRegister.run());

        username = new JTextField(32);
        password = new JTextField(32);

        usernameText = new JLabel("käyttäjänimi");
        passwordText = new JLabel("salasana");

        JPanel TopRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        TopRow.add(topheader, BorderLayout.CENTER);
        TopRow.add(backButton, BorderLayout.EAST);
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow.add(loginButton, BorderLayout.WEST);
        buttonRow.add(registerButton, BorderLayout.EAST);
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(TopRow);
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(createRow(usernameText, username));
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(createRow(passwordText, password));
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(buttonRow);

        this.add(mainPanel);
    }

    private JPanel createRow(JLabel label, JTextField text) {
            JPanel row = new JPanel(new BorderLayout(10,0));
            row.add(label, BorderLayout.WEST);
            row.add(text, BorderLayout.CENTER);
            return row;
        }
}
