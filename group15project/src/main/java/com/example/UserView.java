package com.example;

import java.awt.FlowLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserView extends JPanel {


    JLabel title;
    JLabel username;
    JLabel password;
    JLabel fullname;
    JLabel phonenumber;
    JLabel email;

    JButton backButton;
    JButton editAccount;
    JButton deleteAccount;

    String usernameString = "ee";
    String passwordString;
    String fullnameString;
    String phonenumberString;
    String emailString = "eee";

    UserDatabase userDatabase = new UserDatabase();

    public UserView(Runnable BackToMain)  {
        //view should look like the users. each user has their own database of products.
        setLayout(new FlowLayout());
        //adding values to strings from the user database.

        title = new JLabel("Oma tili");
        username = new JLabel(usernameString);
        password = new JLabel(passwordString);
        fullname = new JLabel(fullnameString);
        phonenumber = new JLabel(phonenumberString);
        email = new JLabel(emailString);

        add(title);
        add(username);
        add(password);
        add(fullname);
        add(phonenumber);
        add(email);   

        email.setText("aaa");
    }

    public void setUser() {
        try {
            ResultSet rs = userDatabase.GetUser(Main.CurrentUser);
            while (rs.next()) {
                username.setText(rs.getString("username"));
                usernameString = rs.getString("username");
                passwordString = rs.getString("password");
                fullnameString = rs.getString("fullname");
                phonenumberString = rs.getString("phonenumber");
                emailString = rs.getString("email");
                //username.setText(usernameString);
                password.setText(passwordString);
                fullname.setText(fullnameString);
                phonenumber.setText(phonenumberString);
                email.setText(emailString);
                revalidate();
                repaint();
                Main.Update();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
