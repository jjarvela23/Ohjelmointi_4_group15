package com.example;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserView extends JPanel {


    JLabel title;
    JLabel username;
    JLabel password;
    JLabel fullname;
    JLabel phonenumber;
    JLabel email;

    JButton backButton;
    JButton editAccount;
    JButton deleteAccount;

    String usernameString;
    String passwordString;
    String fullnameString;
    String phonenumberString;
    String emailString;

    UserDatabase userDatabase = new UserDatabase();

    public UserView(Runnable BackToMain)  {
        //view should look like the users. each user has their own database of products.
        setLayout(new FlowLayout());
        //adding values to strings from the user database.

        title = new JLabel("Oma tili");
        username = new JLabel();
        password = new JLabel();
        fullname = new JLabel();
        phonenumber = new JLabel();
        email = new JLabel();

        backButton = new JButton("takaisin");
        backButton.addActionListener(e -> BackToMain.run());

        add(title);
        add(username);
        add(password);
        add(fullname);
        add(phonenumber);
        add(email);   
        add(backButton);
    }

    public void setUser() {
        try {
            ResultSet rs = userDatabase.GetUser(Main.CurrentUser);
            while (rs.next()) {
                username.setText(rs.getString("username"));
                usernameString = rs.getString("username");
                passwordString = rs.getString("password");
                fullnameString = rs.getString("fullname");
                phonenumberString = rs.getString("phonenumber");
                emailString = rs.getString("email");
                //username.setText(usernameString);
                password.setText(passwordString);
                fullname.setText(fullnameString);
                phonenumber.setText(phonenumberString);
                email.setText(emailString);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void SetUserProducts() {
        try {
                ResultSet rs = userDatabase.GetProductsFromUser(Main.CurrentUser);
                while (rs.next()) {
                    JPanel productContainer = new JPanel(new GridLayout());
                    productContainer.setBorder(BorderFactory.createLineBorder(Color.black));
                    JLabel name = new JLabel("nimi");
                    JLabel price = new JLabel("hinta");
                    JLabel location = new JLabel("sijainti");
                    productContainer.add(name);
                    productContainer.add(new JLabel(rs.getString("name")));
                    productContainer.add(price);
                    productContainer.add(new JLabel(rs.getString("price")));
                    productContainer.add(location);
                    productContainer.add(new JLabel(rs.getString("location")));
                    
                    this.add(productContainer);
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
            
        }
    }
}
