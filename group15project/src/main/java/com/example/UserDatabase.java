package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public final class UserDatabase {

    Connection c = null;
    Statement statement = null;


    public UserDatabase() {
        
        try {
            c = DriverManager.getConnection("jdbc:sqlite:userDatabase.db");
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("database created");

        try {
            Initialize();
            String m = AddUser("jukka", "777");
            System.out.println(m);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public void Initialize() throws SQLException {
        statement = c.createStatement();

        String query = "CREATE TABLE IF NOT EXISTS Users " +
                        "(ID INTEGER PRIMARY KEY," +
                        " USERNAME TEXT NOT NULL," +
                        " PASSWORD TEXT NOT NULL)";
        statement.execute(query);
    }

    public void GetUser() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM Users;");

        while (rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("password");

            System.out.println(username);
            System.out.println(password);
        }
    }

    public String AddUser(String username, String password) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM Users;");
            while (rs.next()) {
                if (rs.getString("username").equals(username)) {
                    return "Username was taken";
                }
            }
            System.out.println("here");
            String newUser = "INSERT INTO Users (USERNAME,PASSWORD) VALUES ('" + username + "', '" + password + "');";
            statement.executeUpdate(newUser);
            return "user added";
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int login(String username, String password) {
        return 0;
    }
}
