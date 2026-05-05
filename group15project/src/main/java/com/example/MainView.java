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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainView extends JPanel {

    //list of components
    JButton UserButton;
    JButton SellButton;
    JButton SearchButton;
    JLabel label;
    JTextField searchBar;
    JComboBox<String> Category;
    JComboBox<String> Location;

    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();

    JPanel productsPanel = new JPanel(new FlowLayout());

    //database reference
    UserDatabase userDatabase = new UserDatabase();

    public MainView(Runnable goToLogin, Runnable SellProduct, Runnable goToUser, UserView userView) {
        //set panel layout
        setLayout(new FlowLayout());

        //TODO splits the panel into two horizontally. change if neccessary.
        JPanel p1 = new JPanel();

        JSplitPane splitPane = new JSplitPane(SwingConstants.HORIZONTAL, p1, p2);

        label = new JLabel();
        label.setText("Käytettyjen tavaroiden kauppapaikka");

        //searchbar component
        searchBar = new JTextField(40);
        //location and category as a dropdown menu
        Location = new JComboBox<>(Main.LocationList);
        Category = new JComboBox<>(Main.CateroryList);

        // make an else-if that checks if the user has logged in. button name changes, and the button sends to a different screen.
        UserButton = new JButton("käyttäjä");
        UserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Main.CurrentUser > 0) {
                    //sets products the user owns.
                    userView.SetUserProducts();
                    goToUser.run();
                } else {
                    goToLogin.run();
                }
            }
        });

        //same as userbutton, but sends to sellview if the user is logged in.
        SellButton = new JButton("myy");
        SellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Main.CurrentUser > 0) {
                    SellProduct.run();
                } else {
                    goToLogin.run();
                }
            }
        });

        SearchButton = new JButton("hae");
        //TODO search functionality

        p1.add(label);
        p1.add(searchBar);
        p1.add(Location);
        p1.add(Category);
        p1.add(UserButton);
        p1.add(SellButton);
        p1.add(SearchButton);

        this.add(p1);
        this.add(productsPanel);
    }

    //create a list of containers and add them to the main panel.
    public void SetProducts() {
        productsPanel.removeAll();
        try {
            ResultSet rs = userDatabase.GetAllProducts();
            while (rs.next()) {
                JPanel productContainer = new JPanel(new GridLayout(0, 2, 4, 4)); // CHANGED: was "new GridLayout()" - now 2 columns so labels and values line up
                productContainer.setBorder(BorderFactory.createLineBorder(Color.black));

                String productName = rs.getString("name");
                String productPrice = rs.getString("price");
                String productLocation = rs.getString("location");
                String productDescription = rs.getString("description"); // CHANGED: new line - now read so it can be shown in detail window
                String productCategory = rs.getString("category"); // CHANGED: new line - now read so it can be shown in detail window
                int owner = rs.getInt("owner");

                String fullname = "";
                String phonenumber = "";
                String email = "";
                ResultSet rs2 = userDatabase.GetUser(owner);
                while (rs2.next()) {
                    fullname = rs2.getString("fullname");
                    phonenumber = rs2.getString("phonenumber");
                    email = rs2.getString("email");
                }

                final String sellerName = fullname; // CHANGED: new line - final copy needed to use inside ActionListener
                final String sellerPhone = phonenumber; // CHANGED: new line - final copy needed to use inside ActionListener
                final String sellerEmail = email; // CHANGED: new line - final copy needed to use inside ActionListener

                productContainer.add(new JLabel("nimi:")); // CHANGED: was "JLabel name = new JLabel("nimi"); productContainer.add(name)"
                productContainer.add(new JLabel(productName)); // CHANGED: was "JLabel nameString = new JLabel(...)"
                productContainer.add(new JLabel("hinta:")); // CHANGED: same pattern as above
                productContainer.add(new JLabel(productPrice)); // CHANGED: same pattern as above
                productContainer.add(new JLabel("sijainti:")); // CHANGED: same pattern as above
                productContainer.add(new JLabel(productLocation)); // CHANGED: same pattern as above
                productContainer.add(new JLabel("myyjä:")); // CHANGED: same pattern as above
                productContainer.add(new JLabel(sellerName)); // CHANGED: was "new JLabel(fullname)" which wouldn't compile inside listener

                JButton more = new JButton("näytä lisää");
                more.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame details = new JFrame(productName);
                        details.setSize(600, 400);
                        JPanel innerPanel = new JPanel(new GridLayout(0, 2, 8, 8)); // CHANGED: was "new JPanel()" - now 2 columns with spacing
                        innerPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12)); // CHANGED: new line - adds padding inside the detail window

                        innerPanel.add(new JLabel("Nimi:")); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel(productName)); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel("Hinta:")); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel(productPrice)); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel("Sijainti:")); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel(productLocation)); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel("Kategoria:")); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel(productCategory)); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel("Kuvaus:")); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel(productDescription)); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel("Myyjä:")); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel(sellerName)); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel("Puhelin:")); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel(sellerPhone)); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel("Sähköposti:")); // CHANGED: detail window was completely empty (was a TODO)
                        innerPanel.add(new JLabel(sellerEmail)); // CHANGED: detail window was completely empty (was a TODO)

                        details.add(innerPanel);
                        details.setVisible(true);
                    }
                });

                productContainer.add(more);
                productsPanel.add(productContainer); // CHANGED: was "this.add(productContainer)" - now adds to productsPanel instead
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        revalidate(); // CHANGED: new line - required so the UI actually updates after adding/removing components
        repaint(); // CHANGED: new line - required so the UI actually redraws after adding/removing components
    }
}
