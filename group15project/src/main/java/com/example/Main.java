package com.example;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

//starts the 
public class Main extends JFrame {

    //global variable for the user who is logged in.
    public static int CurrentUser = 0;

    //TODO add more 
    //Global array that contains categories and locations
    public static String[] CateroryList = {"kategoria", "elektroniikka", "z", "y", "x" };
    public static String[] LocationList = {"sijainti", "pohjanmaa", "z", "y", "x" };

    public static void main(String[] args) {
        
        //reset the user when program starts
        CurrentUser = 0;

        //create frame
        JFrame frame = new JFrame("myApp");
        //cardlayout for switching between views
        CardLayout cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);
        //program shuts when frame is closed.
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //creating all views, and adding them to the panel. Each view has a runnable-parameter for switching views.
        //views have parameters, for navigation. Some views have a reference to another view, which is used to update the panel components.
        UserView userView = new UserView(() -> cardLayout.show(panel, "mainView"));
        MainView mainView = new MainView(() -> cardLayout.show(panel, "loginView"), () -> cardLayout.show(panel, "sellView"), () -> cardLayout.show(panel, "userView"), userView);
        LoginView loginView = new LoginView(() -> cardLayout.show(panel, "mainView"), () -> cardLayout.show(panel, "registerView"), userView);
        SellView sellView = new SellView(() -> cardLayout.show(panel, "mainView"), mainView);
        RegisterView registerView = new RegisterView(() -> cardLayout.show(panel, "loginView"));
        //add views to the main panel
        panel.add(mainView, "mainView");
        panel.add(loginView, "loginView");
        panel.add(userView, "userView");
        panel.add(sellView, "sellView");
        panel.add(registerView, "registerView");

        //this adds the existing products in the database to the main view.
        mainView.SetProducts(false);

        //add panel to the frame
        frame.add(panel);
        //set frame to size, user cannot alter
        frame.setSize(1150, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}