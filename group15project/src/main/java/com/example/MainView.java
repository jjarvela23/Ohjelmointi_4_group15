package com.example;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class MainView extends JPanel{

    JButton UserButton;
    JButton SellButton;
    JButton SearchButton;
    JLabel label;
    JTextField searchBar;
    JComboBox<String> Category;
    JComboBox<String> Area;

    String[] CateroryList = {"Elektroniikka", "y", "z" };
    String[] AreaList = {"Pohjanmaa", "y", "z" };

    public MainView(Runnable goToLogin, Runnable SellProduct, Runnable goToUser) {
        setLayout(new FlowLayout());

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
        

        add(label);
        add(Area);
        add(Category);
        add(UserButton);
        add(SellButton);
        add(SearchButton);
    }
}
