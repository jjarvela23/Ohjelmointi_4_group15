package com.example;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class MainView extends JPanel{

    JButton UserButton;
    JButton SellButton;
    JLabel label;
    JTextField searchBar;
    JComboBox<String> Category;
    JComboBox<String> Area;

    String[] CateroryList = {"x", "y", "z" };
    String[] AreaList = {"x", "y", "z" };

    public MainView(Runnable goToLogin) {
        setLayout(new FlowLayout());

        label = new JLabel();
        label.setText("testing");

        Area = new JComboBox<>(CateroryList);

        // make an else-if that checks if the user has logged in. button name changes, and the button sends to a different screen.
        JButton UserButton = new JButton("user");
        UserButton.addActionListener(e -> goToLogin.run());
        
        add(label);
        add(Area);
        add(UserButton);
    }

    
}
