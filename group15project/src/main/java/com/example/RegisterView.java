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
        //set panel layout
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
                //check if one or more fields are empty
                if (username.getText().isEmpty() || password.getText().isEmpty() || fullname.getText().isEmpty() || phoneNumber.getText().isEmpty() || email.getText().isEmpty()) { 
                    JOptionPane.showMessageDialog(null, "yksi tai useampi kenttä oli tyhjä", "virhe", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    //get the strings from the text fields
                    String uname = username.getText();
                    String pword = password.getText();
                    String fname = fullname.getText();
                    String pnumber = phoneNumber.getText();
                    String mail = email.getText();

                    try {
                        //add user to database. Method returns 2 if the username already exists.
                        if (userDatabase.AddUser(uname, pword, fname, pnumber, mail) == 2) {
                            JOptionPane.showMessageDialog(null, "käyttäjänimi on jo olemassa", "virhe", JOptionPane.INFORMATION_MESSAGE);
                        }
                        //send back to login screen after user is added.
                        else {
                            JOptionPane.showMessageDialog(null, "Uusi käyttäjä rekisteröity. Kirjaudu sisään.", "rekisteröinti onnistui", JOptionPane.INFORMATION_MESSAGE);
                            BackToLogin.run();
                        }
                        
                    } catch (SQLException ex) {
                        System.out.println("registration went wrong");
                    }
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
