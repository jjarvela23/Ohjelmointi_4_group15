package com.example;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JFrame {

    //global variable for the user who is logged in.
    public static int CurrentUser = 0;

    private static JFrame frame;

    public static void main(String[] args) {
        
        //reset the user when program starts
        CurrentUser = 0;

        frame = new JFrame("myApp");
        CardLayout cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //creating all views, and adding them to the panel. Each view has a runnable-parameter for switching views.
        MainView mainView = new MainView(() -> cardLayout.show(panel, "loginView"), () -> cardLayout.show(panel, "sellView"), () -> cardLayout.show(panel, "userView"));
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

    public static void Update() {
        frame.revalidate();
        frame.repaint();
    }
}