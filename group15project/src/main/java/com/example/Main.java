package com.example;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JFrame {


    public static void main(String[] args) {
        
        JFrame frame = new JFrame("myApp");
        CardLayout cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);
        //UserView userView = new UserView()
        MainView mainView = new MainView(() -> cardLayout.show(panel, "loginView"));
        LoginView loginView = new LoginView(() -> cardLayout.show(panel, "mainView"));
        panel.add(mainView, "mainView");
        panel.add(loginView, "loginView");

        frame.add(panel);
        //set frame to screen resolution, preferable
        frame.setSize(1980, 1080);
        frame.setVisible(true);
    }
}