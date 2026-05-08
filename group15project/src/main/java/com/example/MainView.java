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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MainView extends JPanel {

    //list of components
    JButton UserButton;
    JButton SellButton;
    JButton SearchButton;
    JLabel label;
    JTextField searchBar;
    JComboBox<String> Category;
    JComboBox<String> Location;

    JPanel p1;

    JPanel productsPanel = new JPanel(new FlowLayout());
    

    //database reference
    UserDatabase userDatabase = new UserDatabase();

    public MainView(Runnable goToLogin, Runnable SellProduct, Runnable goToUser, UserView userView) {
        //set panel layout
        setLayout(new FlowLayout());
        //height changes how far you can scroll
        productsPanel.setPreferredSize(new Dimension(1000, 2000));

        //panel at the top of the screen
        JPanel p1 = new JPanel();

        label = new JLabel();
        label.setText("Käytettyjen tavaroiden kauppapaikka");

        //searchbar component
        searchBar = new JTextField(40);
        //location and category as a dropdown menu
        Location = new JComboBox<>(Main.LocationList);
        Category = new JComboBox<>(Main.CateroryList);

        // make an else-if that checks if the user has logged in. Button sends to a different screen.
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
                    JOptionPane.showMessageDialog(null, "Et voi myydä ennen kuin kirjaudut sisään", "varoitus", JOptionPane.INFORMATION_MESSAGE);
                    goToLogin.run();
                }
            }
        });

        //calls setProducts when clicked
        SearchButton = new JButton("hae");
        SearchButton.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                SetProducts(true);
            }
        });

        p1.add(label);
        p1.add(searchBar);
        p1.add(Location);
        p1.add(Category);
        p1.add(SearchButton);
        p1.add(SellButton);
        p1.add(UserButton);

        this.add(p1);
        //turn productsPanel scrollable
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setPreferredSize(new Dimension(1000, 600));
        this.add(scrollPane);
    }

    //create a list of containers and add them to the main panel. Parameter is used when searching. When true, it reads the searchbar and category/location comboboxes.
    public void SetProducts(boolean Searching) {
        //remove old products
        productsPanel.removeAll();
        try {
            ResultSet rs = null;
            //different resultset when searching.
            if (Searching) {
                //same database method can be used for different combinations of searches.
                String searchName = "";
                String searchLocation = "";
                String searchCategory = ""; 
                if (!searchBar.getText().isEmpty()) {
                    searchName = searchBar.getText();
                } 
                if (!Location.getSelectedItem().toString().equals("sijainti")) {
                    searchLocation = (String) Location.getSelectedItem();
                }
                if (!Category.getSelectedItem().toString().equals("kategoria")) {
                    searchCategory = (String) Category.getSelectedItem();
                }
                rs = userDatabase.GetSpecificProducts(searchName, searchLocation, searchCategory);
            }
            else {
                //regular resultset
                rs = userDatabase.GetAllProducts();
            }
            //now the product information are added to their individual components
            while (rs.next()) {
                //creating the container. Each row has two columns
                JPanel productContainer = new JPanel(new GridLayout(0, 2, 4, 4));
                productContainer.setBorder(BorderFactory.createLineBorder(Color.black));

                String productName = rs.getString("name");
                String productPrice = rs.getString("price");
                String productLocation = rs.getString("location");
                String productDescription = rs.getString("description");
                String productCategory = rs.getString("category");
                int owner = rs.getInt("owner");

                String fullname = "";
                String phonenumber = "";
                String email = "";
                ResultSet rs2 = userDatabase.GetUser(owner);
                //also gets the sellers information from a different table.
                while (rs2.next()) {
                    fullname = rs2.getString("fullname");
                    phonenumber = rs2.getString("phonenumber");
                    email = rs2.getString("email");
                }

                final String sellerName = fullname;
                final String sellerPhone = phonenumber;
                final String sellerEmail = email;

                //add labels and product information
                productContainer.add(new JLabel("nimi:"));
                productContainer.add(new JLabel(productName));
                productContainer.add(new JLabel("hinta:"));
                productContainer.add(new JLabel(productPrice));
                productContainer.add(new JLabel("sijainti:"));
                productContainer.add(new JLabel(productLocation));
                productContainer.add(new JLabel("myyjä:"));
                productContainer.add(new JLabel(sellerName));

                //button that opens another window with more information to save space on the main view.
                JButton more = new JButton("näytä lisää");
                more.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //creating a new frame
                        JFrame details = new JFrame(productName);
                        details.setSize(600, 400);
                        JPanel innerPanel = new JPanel(new GridLayout(0, 2, 8, 8));
                        innerPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

                        innerPanel.add(new JLabel("Nimi:"));
                        innerPanel.add(new JLabel(productName));
                        innerPanel.add(new JLabel("Hinta:"));
                        innerPanel.add(new JLabel(productPrice));
                        innerPanel.add(new JLabel("Sijainti:"));
                        innerPanel.add(new JLabel(productLocation));
                        innerPanel.add(new JLabel("Kategoria:"));
                        innerPanel.add(new JLabel(productCategory));
                        innerPanel.add(new JLabel("Kuvaus:"));
                        innerPanel.add(new JLabel(productDescription));
                        innerPanel.add(new JLabel("Myyjä:"));
                        innerPanel.add(new JLabel(sellerName));
                        innerPanel.add(new JLabel("Puhelin:"));
                        innerPanel.add(new JLabel(sellerPhone));
                        innerPanel.add(new JLabel("Sähköposti:"));
                        innerPanel.add(new JLabel(sellerEmail));

                        //add components to frame, center the window and set it visible.
                        details.add(innerPanel);
                        details.setLocationRelativeTo(null);
                        details.setVisible(true);
                    }
                });
                //add button to panel
                productContainer.add(more);
                productsPanel.add(productContainer);
            }
        } catch (SQLException ex) {
            System.out.println("Failed to add products to main");
        }
        revalidate();
        repaint();
    }
}
