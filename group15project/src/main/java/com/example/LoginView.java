package com.example;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
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

    public LoginView(Runnable goToMain) {
        setLayout(new FlowLayout());
        username = new JTextField();
        password = new JTextField();

        backButton = new JButton("takaisin");
        backButton.addActionListener(e -> goToMain.run());

        loginButton = new JButton("kirjaudu");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uname = username.getText();
                String pword = password.getText();
                try {

                    if (userDatabase.login(uname, pword)) {
                        System.out.println("login succesful");
                        //do login stuff
                    }
                    else {
                        System.out.println("login failed");
                    }
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    System.out.println("Username or password was incorrect");
                }
            }
        });

        username = new JTextField(32);
        password = new JTextField(32);

        add(backButton);
        add(loginButton);
        add(username);
        add(password);
    }
}
