package com.example;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoginView extends JFrame{

    JTextField username;
    JTextField password;
    JLabel topheader;
    JLabel usernameText;
    JLabel passwordText;
    JButton loginButton;
    JButton backButton;

    UserDatabase userDatabase = new UserDatabase();

    public LoginView() {
        username = new JTextField();
        password = new JTextField();
    }
}
