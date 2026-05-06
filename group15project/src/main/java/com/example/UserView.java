package com.example;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JScrollPane;

public class UserView extends JPanel {

    JLabel title;
    JLabel usernameLabel;
    JLabel fullnameLabel;
    JLabel phonenumberLabel;
    JLabel emailLabel;

    JButton backButton;
    JButton deleteUserButton;

    JPanel userPanel = new JPanel(new FlowLayout());
    JPanel productsPanel = new JPanel(new FlowLayout());

    UserDatabase userDatabase = new UserDatabase();

    public UserView(Runnable BackToMain) {
        //view should look like the users. each user has their own database of products.
        setLayout(new FlowLayout());
        //set panel sizes
        userPanel.setPreferredSize(new Dimension(1000, 200));
        userPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        productsPanel.setPreferredSize(new Dimension(1000, 435));

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

        userPanel.add(title);
        userPanel.add(new JLabel("Käyttäjänimi:"));
        userPanel.add(usernameLabel);
        userPanel.add(new JLabel("Nimi:"));
        userPanel.add(fullnameLabel);
        userPanel.add(new JLabel("Puhelin:"));
        userPanel.add(phonenumberLabel);
        userPanel.add(new JLabel("Sähköposti:"));
        userPanel.add(emailLabel);
        userPanel.add(deleteUserButton);
        userPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setPreferredSize(new Dimension(1000, 435));
        this.add(userPanel);
        this.add(scrollPane);
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
            System.out.println("Failure to set user in UserView");
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

                int id = rs.getInt("ID");

                JButton deleteProduct = new JButton();
                deleteProduct.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int confirmResult = JOptionPane.showConfirmDialog(null, "Haluatko varmasti poistaa tuotteen?", "Varoitus", JOptionPane.YES_NO_OPTION);
                        if (confirmResult == JOptionPane.YES_OPTION) {
                            try {
                                if (userDatabase.deleteProduct(id)) {
                                    JOptionPane.showMessageDialog(null, "tuote poistettu.", "onnistui", JOptionPane.INFORMATION_MESSAGE);
                                    SetUserProducts();
                                    //TODO find a way to call SetProducts(false) on main.
                                }
                            } catch (SQLException e1) {
                                System.out.println("failed to delete user");
                            }
                        }    
                    }
                });
                //add to container
                productContainer.add(deleteProduct);

                productsPanel.add(productContainer);
            }
        } catch (SQLException ex) {
            System.out.println("Failure to add products to user view");
        }

        revalidate();
        repaint();
    }
}
