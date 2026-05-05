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
    JLabel usernameLabel;
    JLabel fullnameLabel;
    JLabel phonenumberLabel;
    JLabel emailLabel;

    JButton backButton;

    JPanel productsPanel = new JPanel(new FlowLayout());

    UserDatabase userDatabase = new UserDatabase();

    public UserView(Runnable BackToMain) {
        //view should look like the users. each user has their own database of products.
        setLayout(new FlowLayout());
        //adding values to strings from the user database.

        title = new JLabel("Oma tili");
        usernameLabel = new JLabel();
        fullnameLabel = new JLabel();
        phonenumberLabel = new JLabel();
        emailLabel = new JLabel();

        backButton = new JButton("takaisin");
        backButton.addActionListener(e -> BackToMain.run());

        add(title);
        add(new JLabel("Käyttäjänimi:"));
        add(usernameLabel);
        add(new JLabel("Nimi:"));
        add(fullnameLabel);
        add(new JLabel("Puhelin:"));
        add(phonenumberLabel);
        add(new JLabel("Sähköposti:"));
        add(emailLabel);
        add(backButton);
        add(productsPanel);
    }

    public void setUser() {
        try {
            ResultSet rs = userDatabase.GetUser(Main.CurrentUser);
            while (rs.next()) {
                usernameLabel.setText(rs.getString("username"));
                fullnameLabel.setText(rs.getString("fullname"));
                phonenumberLabel.setText(rs.getString("phonenumber"));
                emailLabel.setText(rs.getString("email"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void SetUserProducts() {
        productsPanel.removeAll();

        try {
            ResultSet rs = userDatabase.GetProductsFromUser(Main.CurrentUser);
            while (rs.next()) {
                JPanel productContainer = new JPanel(new GridLayout(0, 2, 4, 4));
                productContainer.setBorder(BorderFactory.createLineBorder(Color.black));

                productContainer.add(new JLabel("nimi:"));
                productContainer.add(new JLabel(rs.getString("name")));
                productContainer.add(new JLabel("hinta:"));
                productContainer.add(new JLabel(rs.getString("price")));
                productContainer.add(new JLabel("sijainti:"));
                productContainer.add(new JLabel(rs.getString("location")));

                productsPanel.add(productContainer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        revalidate();
        repaint();
    }
}
