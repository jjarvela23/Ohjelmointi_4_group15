package com.example;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisterView extends JPanel{

    JTextField username;
    JTextField password;
    JTextField fullname;
    JTextField phoneNumber;
    JTextField email;
    JLabel topheader;
    JLabel usernameText;
    JLabel passwordText;
    JLabel fullnameText;
    JLabel phoneNumberText;
    JLabel emailText;
    JButton confirmButton;
    JButton backButton;

    UserDatabase userDatabase = new UserDatabase();

    public RegisterView(Runnable BackToLogin) {
        setLayout(new FlowLayout());

        username = new JTextField(32);
        password = new JTextField(32);
        fullname = new JTextField(32);
        phoneNumber = new JTextField(32);
        email = new JTextField(32);

        topheader = new JLabel("rekisteröinti");
        usernameText = new JLabel("käyttäjänimi");
        passwordText = new JLabel("salasana");
        fullnameText = new JLabel("etu-ja sukunimi");
        phoneNumberText = new JLabel("puhelinnumero");
        emailText = new JLabel("email");

        confirmButton = new JButton("hyväksy");
        backButton = new JButton("peruuta");

        backButton.addActionListener(e -> BackToLogin.run());

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO catch empty
                String uname = username.getText();
                String pword = password.getText();
                String fname = fullname.getText();
                String pnumber = phoneNumber.getText();
                String mail = email.getText();

                try {
                    
                    if (userDatabase.AddUser(uname, pword, fname, pnumber, mail) == 2) {
                        System.out.println("Username was taken");
                    }
                    else {
                        System.out.println("Registration succesful");
                    }
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("registration went wrong");
                }
            }
        });

        add(topheader);
        add(confirmButton);
        add(backButton);
        add(usernameText);
        add(username);
        add(passwordText);
        add(password);
        add(fullnameText);
        add(fullname);
        add(phoneNumberText);
        add(phoneNumber);
        add(emailText);
        add(email);
    }
}
