package com.example;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main extends JFrame {

    JButton UserButton;
    JButton SellButton;
    JLabel label;
    JPanel MainPanel;

    public Main() {
        super("MainScreenFrame");

        setSize(1980, 1080);

        label = new JLabel();
        label.setText("testing");
        MainPanel = new JPanel(new FlowLayout());

        MainPanel.add(label);

        this.getContentPane().add(MainPanel);
    }


    public static void main(String[] args) {
        Main myprogram = new Main();
        myprogram.setVisible(true);
    }
}