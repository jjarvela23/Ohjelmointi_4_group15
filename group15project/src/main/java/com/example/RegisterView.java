package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

    Dimension d = new Dimension(0, 10);

    UserDatabase userDatabase = new UserDatabase();

    public RegisterView(Runnable BackToLogin) {
        //set panel layout
        
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(new Dimension(800,600));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(10, 10, 10, 10)));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));


        username = new JTextField(32);
        password = new JTextField(32);
        fullname = new JTextField(32);
        phoneNumber = new JTextField(32);
        email = new JTextField(32);

        topheader = new JLabel("luo tili");
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

        JPanel topRow = new JPanel(new BorderLayout(200,0));
        topRow.add(Box.createRigidArea(d), BorderLayout.WEST);
        topRow.add(topheader, BorderLayout.CENTER);
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow.add(confirmButton, BorderLayout.WEST);
        buttonRow.add(backButton, BorderLayout.EAST);
        mainPanel.add(topRow, BorderLayout.CENTER);
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(createRow(usernameText, username));
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(createRow(passwordText, password));
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(createRow(fullnameText, fullname));
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(createRow(phoneNumberText, phoneNumber));
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(createRow(emailText, email));
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(buttonRow);

        add(mainPanel);

        
    }

    private JPanel createRow(JLabel label, JTextField text) {
            JPanel row = new JPanel(new BorderLayout(10,0));
            row.add(label, BorderLayout.WEST);
            row.add(text, BorderLayout.CENTER);
            return row;
        }
}
