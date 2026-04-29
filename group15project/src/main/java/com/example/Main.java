package com.example;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main extends JFrame {

    JButton UserButton;
    JButton SellButton;
    JLabel label;
    JPanel MainPanel;
    JTextField searchBar;
    JComboBox<String> Category;
    JComboBox<String> Area;

    String[] CateroryList = {"x", "y", "z" };
    String[] AreaList = {"x", "y", "z" };

    public Main() {
        super("MainScreenFrame");

        setSize(1980, 1080);

        label = new JLabel();
        label.setText("testing");

        Area = new JComboBox<>(CateroryList);
        MainPanel = new JPanel(new FlowLayout());

        MainPanel.add(label);
        MainPanel.add(Area);

        this.getContentPane().add(MainPanel);
    }


    public static void main(String[] args) {
        Main myprogram = new Main();
        myprogram.setVisible(true);
        UserDatabase userDatabase = new UserDatabase();
    }
}