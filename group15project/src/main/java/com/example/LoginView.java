package com.example;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginView extends JPanel {

    JTextField username;
    JTextField password;
    JLabel topheader;
    JLabel usernameText;
    JLabel passwordText;
    JButton loginButton;
    JButton registerButton;
    JButton backButton;

    UserDatabase userDatabase = new UserDatabase();

    public LoginView(Runnable goToMain, Runnable GoToRegister, UserView userView) {
        setLayout(new FlowLayout());

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

        add(topheader);
        add(backButton);
        add(loginButton);
        add(registerButton);
        add(usernameText);
        add(username);
        add(passwordText);
        add(password);
    }
}
