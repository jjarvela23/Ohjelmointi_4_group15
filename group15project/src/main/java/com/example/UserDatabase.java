package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
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
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public void Initialize() throws SQLException {
        statement = c.createStatement();

        String query = "CREATE TABLE IF NOT EXISTS Users " +
                        "(ID INT PRIMARY KEY NOT NULL," +
                        " USERNAME TEXT NOT NULL," +
                        " PASSWORD TEXT NOT NULL)";
        statement.execute(query);

        String test = "INSERT INTO Users (ID,USERNAME,PASSWORD) VALUES (1, 'pekka', 'pouta');";
    }
}
