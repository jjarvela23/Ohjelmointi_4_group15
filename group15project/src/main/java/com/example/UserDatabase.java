package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//SQLITE database for all database related things. Contains two tables: Users and Products.
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
            System.out.println("Failed to create tables");
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
                        " DESCRIPTION VARCHAR(30)," +
                        " CATEGORY TEXT," +
                        " OWNER INT," + 
                        " FOREIGN KEY (OWNER) REFERENCES Users(ID))";
        statement.execute(query);
        statement.close();
    }

    //get user from id
    public ResultSet GetUser(int id) throws SQLException {
        statement = c.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM Users WHERE ID = "+ id + ";");
        return rs;
    }

    //adds a new user to database
    public int AddUser(String username, String password, String fullname, String phonenumber, String email) throws SQLException {
            statement = c.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Users;");
            //checks if username exists
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

    //update specific user information
    public boolean UpdateUser(int id, String fullname, String phonenumber, String email) throws SQLException {
        statement = c.createStatement();
        String query = "UPDATE Users SET " +
        "FULLNAME = '" + fullname + "', " + 
        "PHONENUMBER = '" + phonenumber + "', " + 
        "EMAIL = '" + email + "' " + 
        "WHERE ID = '" + id + "';";
        statement.executeUpdate(query);
        return true;
    }

    //login checks if given password matches the one in the database. Returns the id belonging to the username
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

    //deletes the specific user
    public boolean DeleteUser(int userID) throws SQLException {
        statement = c.createStatement();
        String query = "DELETE FROM Users WHERE ID = " + userID + ";";
        statement.executeUpdate(query);
        statement.close();
        return true;
    }

    //adds a new product to the database
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

    //returns all products that a specific user owns
    public ResultSet GetProductsFromUser(int userid) throws SQLException {
        statement = c.createStatement();
        String query = "SELECT * FROM Products WHERE OWNER ='" + userid + "';";
        ResultSet rs = statement.executeQuery(query);
        return rs;
    }

    //returns all products
    public ResultSet GetAllProducts() throws SQLException {
        statement = c.createStatement();
        String query = "SELECT * FROM Products";
        ResultSet rs = statement.executeQuery(query);
        return rs;
    }

    //returns a product with a specific id
    public ResultSet GetProductById(int id) throws SQLException {
        statement = c.createStatement();
        String query = "SELECT * FROM Products WHERE ID ='" + id + "';";
        ResultSet rs = statement.executeQuery(query);
        return rs;
    }

    //update a products with a specific id
    public boolean updateProductById(int id, String name, String price, String location, String description, String category) throws SQLException {
        statement = c.createStatement();
        String query = "UPDATE Products SET " +
        "NAME = '" + name + "', " + 
        "PRICE = '" + price + "', " + 
        "LOCATION = '" + location + "', " + 
        "DESCRIPTION = '" + description + "', " + 
        "CATEGORY = '" + category + "' " + 
        "WHERE ID = '" + id + "';";
        statement.executeUpdate(query);
        return true;
    }

    //for searching. Else-if statements for combinations.
    public ResultSet GetSpecificProducts(String name, String location, String category) throws SQLException {
        statement = c.createStatement();
        String query = "";
        //different query based on parameters. many categories go above since its else-if
        if (!name.isEmpty() && !location.isEmpty() && !category.isEmpty()) {
            query =  "SELECT * FROM Products WHERE NAME LIKE '%" + name + "%' AND LOCATION ='" + location + "' AND CATEGORY ='" + category + "';" ;
        }
        else if (!name.isEmpty() && !location.isEmpty()) {
            query =  "SELECT * FROM Products WHERE NAME LIKE '%" + name + "%' AND LOCATION ='" + location + "';";
        }
        else if (!name.isEmpty() && !category.isEmpty()) {
            query =  "SELECT * FROM Products WHERE NAME LIKE '%" + name + "%' AND CATEGORY ='" + category + "';";
        }
        else if (!location.isEmpty() && !category.isEmpty()) {
            query =  "SELECT * FROM Products WHERE LOCATION ='" + location + "' AND CATEGORY ='" + category + "';";
        }
        else if (!name.isEmpty()) {
            query =  "SELECT * FROM Products WHERE NAME LIKE '%" + name + "%';";
        }
        else if (!location.isEmpty()) {
            query =  "SELECT * FROM Products WHERE LOCATION ='" + location + "';";
        }
        else if (!category.isEmpty()) {
            query =  "SELECT * FROM Products WHERE CATEGORY ='" + category + "';";
        }
        else {
            query = "SELECT * FROM Products";
        }
        ResultSet rs = statement.executeQuery(query);
        return rs;
    }

    //delete a product
    public boolean deleteProduct(int productID) throws SQLException {
        statement = c.createStatement();
        String query = "DELETE FROM Products WHERE ID = " + productID + ";";
        statement.executeUpdate(query);
        statement.close();
        return true;
    }
}
