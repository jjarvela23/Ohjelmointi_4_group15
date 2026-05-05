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



public class MainView extends JPanel{

    JButton UserButton;
    JButton SellButton;
    JButton SearchButton;
    JLabel label;
    JTextField searchBar;
    JComboBox<String> Category;
    JComboBox<String> Area;

    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();


    UserDatabase userDatabase = new UserDatabase();

    public MainView(Runnable goToLogin, Runnable SellProduct, Runnable goToUser, UserView userView) {
        setLayout(new FlowLayout());

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();

        JSplitPane splitPane = new JSplitPane(SwingConstants.HORIZONTAL, p1, p2);

        label = new JLabel();
        label.setText("testing");

        searchBar = new JTextField(40);

        Area = new JComboBox<>(Main.LocationList);
        Category = new JComboBox<>(Main.CateroryList);

        //create a new panel for individual products.
        JPanel product = new JPanel();
        product.setLayout(new GridLayout());
        JLabel prolabel = new JLabel("tuote");
        JButton probutton = new JButton("nappi");
        product.add(prolabel);
        product.add(probutton);   

        // make an else-if that checks if the user has logged in. button name changes, and the button sends to a different screen.
        UserButton = new JButton("käyttäjä");
        UserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Main.CurrentUser > 0) {
                    userView.SetUserProducts();
                    goToUser.run();
                }
                else {
                    goToLogin.run();
                }
            }
        });
        
        SellButton = new JButton("myy");
        SellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Main.CurrentUser > 0) {
                    SellProduct.run();
                }
                else {
                    goToLogin.run();
                }
            }
        });

        SearchButton = new JButton("hae");
        //TODO search functionality

        p1.add(label);
        p1.add(searchBar);
        p1.add(Area);
        p1.add(Category);
        p1.add(UserButton);
        p1.add(SellButton);
        p1.add(SearchButton); 

        this.add(splitPane);
        }


        //create a list of containers and add them to the main panel.
        public void SetProducts() {
            
            try {
                ResultSet rs = userDatabase.GetAllProducts();
                while (rs.next()) {
                    JPanel productContainer = new JPanel(new GridLayout());
                    productContainer.setBorder(BorderFactory.createLineBorder(Color.black));
                    JLabel name = new JLabel("nimi");
                    JLabel price = new JLabel("hinta");
                    JLabel location = new JLabel("sijainti");
                    JLabel ownerLabel = new JLabel("myyjä");

                    String productName = rs.getString("name");
                    JLabel nameString = new JLabel(rs.getString("name"));
                    JLabel priceString = new JLabel(rs.getString("price"));
                    JLabel locationString = new JLabel(rs.getString("location"));
                    
                    productContainer.add(name);
                    productContainer.add(nameString);
                    productContainer.add(price);
                    productContainer.add(priceString);
                    productContainer.add(location);
                    productContainer.add(locationString);
                    int owner = rs.getInt("owner");
                    ResultSet rs2 = userDatabase.GetUser(owner);
                    String fullname = "";
                    String phonenumber = "";
                    String email = "";
                    while (rs2.next()) {
                        fullname = rs2.getString("fullname");
                        phonenumber = rs2.getString("phonenumber");
                        email = rs2.getString("email");
                    }
                    productContainer.add(ownerLabel);
                    productContainer.add(new JLabel(fullname));
                    JButton more = new JButton("näytä lisää");
                    //TODO
                    more.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //in here create a new frame and add all product details as components.
                            JFrame details = new JFrame(productName);
                            details.setSize(600, 400);
                            JPanel innerPanel = new JPanel();
                            //TODO add to inner panel
                            details.add(innerPanel);
                            details.setVisible(true);
                        }
                    });
                    productContainer.add(more);
                    
                    this.add(productContainer);
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
            
        }
    }
}
