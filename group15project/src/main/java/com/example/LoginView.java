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


//view for login. 
public class LoginView extends JPanel {

    JTextField username;
    JTextField password;
    JLabel topheader;
    JLabel usernameText;
    JLabel passwordText;
    JButton loginButton;
    JButton registerButton;
    JButton backButton;

    JPanel mainPanel;

    Dimension d = new Dimension(0, 10);

    UserDatabase userDatabase = new UserDatabase();

    public LoginView(Runnable goToMain, Runnable GoToRegister, UserView userView) {

        //create a panel where the components will be put
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(new Dimension(800,600));
        //outer border and an empty line border to add an inset.
        mainPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(10, 10, 10, 10)));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        topheader = new JLabel("Kirjaudu sisään");

        //returns to main view
        backButton = new JButton("takaisin");
        backButton.addActionListener(e -> goToMain.run());

        //confirms the login. Checks if the text fields are empty. Checks if the user exists in the database, and if the password is correct.
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
                        //check if the password and username are correct
                        if (userDatabase.login(uname, pword) > 0) {
                            //do login stuff
                            int userId = userDatabase.login(uname, pword);
                            //sets the new user.
                            Main.CurrentUser = userId;
                            //updates the view in another class to reflect the user.
                            userView.setUser();
                            JOptionPane.showMessageDialog(null, "kirjautuminen onnistui", "", JOptionPane.INFORMATION_MESSAGE);
                            //go back to main
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

        //to register
        registerButton = new JButton("rekisteröidy");
        registerButton.addActionListener(e -> GoToRegister.run());

        username = new JTextField(32);
        password = new JTextField(32);

        usernameText = new JLabel("käyttäjänimi");
        passwordText = new JLabel("salasana");

        //create and add rows to the main panel
        JPanel TopRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        TopRow.add(topheader);
        TopRow.add(Box.createRigidArea(new Dimension(240, 0)));
        TopRow.add(backButton);
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

    //method to create label + textfield rows
    private JPanel createRow(JLabel label, JTextField text) {
            JPanel row = new JPanel(new BorderLayout(10,0));
            row.add(label, BorderLayout.CENTER);
            row.add(text, BorderLayout.EAST);
            return row;
        }
}
