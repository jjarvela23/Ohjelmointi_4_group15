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
            if (c == null) {
                c = DriverManager.getConnection("jdbc:sqlite:userDatabase.db");
            }
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
            InitializeUsers();
            InitializeProducts();
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //create table Users.
    public void InitializeUsers() throws SQLException {
        
        statement = c.createStatement();

        String query = "CREATE TABLE IF NOT EXISTS Users " +
                        "(ID INTEGER PRIMARY KEY," +
                        " USERNAME TEXT NOT NULL," +
                        " PASSWORD TEXT NOT NULL," +
                        " FULLNAME TEXT, " +
                        " PHONENUMBER TEXT," +
                        " EMAIL TEXT)";
        statement.execute(query);
        statement.close();
    }

    //Create table Products. Has a foreign key that references the user that owns that product.
    public void InitializeProducts() throws SQLException {
        statement = c.createStatement();

        String query = "CREATE TABLE IF NOT EXISTS Products " +
                        "(ID INTEGER PRIMARY KEY," +
                        " NAME TEXT NOT NULL," +
                        " PRICE TEXT NOT NULL," + 
                        " LOCATION TEXT NOT NULL," + 
                        " DESCRIPTION TEXT," +
                        " CATEGORY TEXT," +
                        " OWNER INT," + 
                        " FOREIGN KEY (OWNER) REFERENCES Users(ID))";
        statement.execute(query);
        statement.close();
    }

    public ResultSet GetUser(int id) throws SQLException {
        statement = c.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM Users WHERE ID = "+ id + ";");
        return rs;
    }

        

    public int AddUser(String username, String password, String fullname, String phonenumber, String email) throws SQLException {
            statement = c.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Users;");
            while (rs.next()) {
                if (rs.getString("username").equals(username)) {
                    return 2;
                }
            }
            String newUser = "INSERT INTO Users (USERNAME,PASSWORD,FULLNAME,PHONENUMBER,EMAIL) VALUES ('" +
            username + "'," + "'" + 
            password + "'," + "'" + 
            fullname + "'," + "'" + 
            phonenumber + "'," + "'" + 
            email + 
            "');";
            statement.executeUpdate(newUser);
            statement.close();
            return 1;
    }

    public int login(String username, String password) throws SQLException {
        statement = c.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM Users WHERE USERNAME ='" + username + "';");
        while (rs.next()) {
            int id = rs.getInt("id");
            String actualPassword = rs.getString("password");
            if (actualPassword.equals(password)) {
                statement.close();
                return id;
            }
            else {
                statement.close();
                return 0;
            }
            
        }
        statement.close();
        return 0;  
    }

    public boolean AddProduct(String name, String price, String location, String description, String category) throws SQLException {
        statement = c.createStatement();
        int owner = Main.CurrentUser;
        String query = "INSERT INTO Products (NAME,PRICE,LOCATION,DESCRIPTION,CATEGORY,OWNER) VALUES ('" + 
        name + "'," + "'" + 
        price + "'," + "'" + 
        location + "'," + "'" + 
        description + "'," + "'" + 
        category + "'," + "'" + 
        owner +
        "');";
        statement.executeUpdate(query);
        statement.close();
        return true;
    }

    //TODO for userView
    public ResultSet GetProductsFromUser(int userid) throws SQLException {
        statement = c.createStatement();
        String query = "SELECT * FROM Products WHERE OWNER ='" + userid + "';";
        ResultSet rs = statement.executeQuery(query);
        return rs;
    }

    //TODO for mainView
    public ResultSet GetAllProducts() throws SQLException {
        statement = c.createStatement();
        String query = "SELECT * FROM Products";
        ResultSet rs = statement.executeQuery(query);
        return rs;
    }
}
