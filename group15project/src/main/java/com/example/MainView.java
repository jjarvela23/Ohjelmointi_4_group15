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

    String[] CateroryList = {"Elektroniikka", "y", "z" };
    String[] AreaList = {"Pohjanmaa", "y", "z" };

    UserDatabase userDatabase = new UserDatabase();

    public MainView(Runnable goToLogin, Runnable SellProduct, Runnable goToUser, UserView userView) {
        setLayout(new FlowLayout());

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();

        JSplitPane splitPane = new JSplitPane(SwingConstants.HORIZONTAL, p1, p2);

        label = new JLabel();
        label.setText("testing");

        Area = new JComboBox<>(AreaList);
        Category = new JComboBox<>(CateroryList);

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
                    JLabel price = new JLabel("price");
                    JLabel location = new JLabel("location");
                    JLabel ownerLabel = new JLabel("owner");
                    productContainer.add(name);
                    productContainer.add(new JLabel(rs.getString("name")));
                    productContainer.add(price);
                    productContainer.add(new JLabel(rs.getString("price")));
                    productContainer.add(location);
                    productContainer.add(new JLabel(rs.getString("location")));
                    int owner = rs.getInt("owner");
                    ResultSet rs2 = userDatabase.GetUser(owner);
                    String fullname = "";
                    while (rs2.next()) {
                        fullname = rs2.getString("fullname");
                    }
                    productContainer.add(ownerLabel);
                    productContainer.add(new JLabel(fullname));
                    JButton more = new JButton("näytä lisää");
                    //TODO
                    productContainer.add(more);
                    
                    this.add(productContainer);
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
            
        }
    }
}
