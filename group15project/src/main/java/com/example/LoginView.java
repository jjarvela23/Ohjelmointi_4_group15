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

    public LoginView(Runnable goToMain, Runnable GoToRegister) {
        setLayout(new FlowLayout());

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

        registerButton = new JButton("rekisteröidy");
        registerButton.addActionListener(e -> GoToRegister.run());

        username = new JTextField(32);
        password = new JTextField(32);

        usernameText = new JLabel("käyttäjänimi");
        passwordText = new JLabel("salasana");

        add(backButton);
        add(loginButton);
        add(registerButton);
        add(username);
        add(password);
        add(usernameText);
        add(passwordText);
    }
}
