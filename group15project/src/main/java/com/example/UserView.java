package com.example;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UserView extends JPanel {

    JLabel title;
    JLabel usernameLabel;
    JLabel fullnameLabel;
    JLabel phonenumberLabel;
    JLabel emailLabel;

    JButton backButton;
    JButton deleteUserButton;

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

        backButton = new JButton("etusivu");
        backButton.addActionListener(e -> BackToMain.run());

        deleteUserButton = new JButton("poista tili");
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmResult = JOptionPane.showConfirmDialog(null, "Haluatko varmasti poistaa tilisi?", "Varoitus", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    try {
                        if (userDatabase.DeleteUser(Main.CurrentUser)) {
                            JOptionPane.showMessageDialog(null, "Käyttäjä poistettu. Palataan etusivulle.", "onnistui", JOptionPane.INFORMATION_MESSAGE);
                            Main.CurrentUser = 0;
                            BackToMain.run();
                        }
                    } catch (SQLException e1) {
                        System.out.println("failed to delete user");
                    }
                }    
            }
        });

        add(title);
        add(new JLabel("Käyttäjänimi:"));
        add(usernameLabel);
        add(new JLabel("Nimi:"));
        add(fullnameLabel);
        add(new JLabel("Puhelin:"));
        add(phonenumberLabel);
        add(new JLabel("Sähköposti:"));
        add(emailLabel);
        add(deleteUserButton);
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
