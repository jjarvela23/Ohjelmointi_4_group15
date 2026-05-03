package com.example;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JFrame {

    public static int CurrentUser = 0;

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("myApp");
        CardLayout cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //creating all views, and adding them to the panel. Each view has a runnable-parameter for switching views.
        MainView mainView = new MainView(() -> cardLayout.show(panel, "loginView"), () -> cardLayout.show(panel, "sellView"));
        LoginView loginView = new LoginView(() -> cardLayout.show(panel, "mainView"), () -> cardLayout.show(panel, "registerView"));
        UserView userView = new UserView(() -> cardLayout.show(panel, "mainView"));
        SellView sellView = new SellView(() -> cardLayout.show(panel, "mainView"));
        RegisterView registerView = new RegisterView(() -> cardLayout.show(panel, "loginView"));
        panel.add(mainView, "mainView");
        panel.add(loginView, "loginView");
        panel.add(userView, "userView");
        panel.add(sellView, "sellView");
        panel.add(registerView, "registerView");

        //add panel to the frame
        frame.add(panel);
        //set frame to screen resolution, preferable TODO
        frame.setSize(1980, 1080);
        frame.setVisible(true);
    }
}