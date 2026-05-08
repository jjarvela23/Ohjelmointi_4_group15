package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

//view contains the user information, as well as the products they own/are selling. Only available after logging in.
public class UserView extends JPanel {

    JLabel title;
    JLabel usernameLabel;
    JLabel fullnameLabel;
    JLabel phonenumberLabel;
    JLabel emailLabel;

    JButton backButton;
    JButton deleteUserButton;

    JPanel topPanel = new JPanel();
    JPanel userPanel = new JPanel();
    
    JPanel productsPanel = new JPanel(new FlowLayout());

    UserDatabase userDatabase = new UserDatabase();

    public UserView(Runnable BackToMain) {
        //view should look like the users. each user has their own database of products.
        setLayout(new FlowLayout());
        //set panel sizes
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(1000, 200));
        //panel with user information
        userPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(10, 10, 10, 10)));
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        //height changes how far you can scroll
        productsPanel.setPreferredSize(new Dimension(1000, 2000));

        title = new JLabel("Oma tili");
        usernameLabel = new JLabel();
        fullnameLabel = new JLabel();
        phonenumberLabel = new JLabel();
        emailLabel = new JLabel();

        //return to main 
        backButton = new JButton("etusivu");
        backButton.addActionListener(e -> BackToMain.run());

        //button that deletes the user from the database
        deleteUserButton = new JButton("poista tili");
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //confirmation dialog
                int confirmResult = JOptionPane.showConfirmDialog(null, "Haluatko varmasti poistaa tilisi?", "Varoitus", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    try {
                        if (userDatabase.DeleteUser(Main.CurrentUser)) {
                            JOptionPane.showMessageDialog(null, "Käyttäjä poistettu. Palataan etusivulle.", "onnistui", JOptionPane.INFORMATION_MESSAGE);
                            //set the current user back to 0.
                            Main.CurrentUser = 0;
                            BackToMain.run();
                        }
                    } catch (SQLException e1) {
                        System.out.println("failed to delete user");
                    }
                }    
            }
        });

        //button to edit user information
        JButton editUserButton = new JButton("muokkaa");
            editUserButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EditUser(Main.CurrentUser);
                }
            });

        //add rows to user panel.
        userPanel.add(createRow(new JLabel("käyttäjänimi:"), usernameLabel));
        userPanel.add(createRow(new JLabel("nimi:"), fullnameLabel));
        userPanel.add(createRow(new JLabel("puhelinnumero:"), phonenumberLabel));
        userPanel.add(createRow(new JLabel("sähköposti:"), emailLabel));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow.add(editUserButton, BorderLayout.WEST);
        buttonRow.add(deleteUserButton, BorderLayout.EAST);

        userPanel.add(buttonRow);
        JPanel box = new JPanel(new FlowLayout());
        box.add(new JLabel("oma tili"));
        box.add(backButton);
        //add userpanel to top panel
        topPanel.add(box, BorderLayout.EAST);
        topPanel.add(userPanel, BorderLayout.WEST);

        //convert productpanel to a scrollable panel
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setPreferredSize(new Dimension(1000, 435));
        //add the two panels to the main one
        this.add(topPanel);
        this.add(scrollPane);
    }

    //sets the user information, based on the user who has logged in
    public void setUser() {
        try {
            ResultSet rs = userDatabase.GetUser(Main.CurrentUser);
            while (rs.next()) {
                usernameLabel.setText(rs.getString("username"));
                fullnameLabel.setText(rs.getString("fullname"));
                phonenumberLabel.setText(rs.getString("phonenumber"));
                emailLabel.setText(rs.getString("email"));
            }
        } catch (SQLException ex) {
            System.out.println("Failure to set user in UserView");
        }
    }

    //method to create label + textfield rows
    private JPanel createRow(JLabel label, JLabel text) {
            JPanel row = new JPanel(new BorderLayout(10,0));
            row.add(label, BorderLayout.CENTER);
            row.add(text, BorderLayout.EAST);
            return row;
        }

    //sets the products, based on the user who has logged in.
    public void SetUserProducts() {
        //remove previous products
        productsPanel.removeAll();
        try {
            ResultSet rs = userDatabase.GetProductsFromUser(Main.CurrentUser);
            //get product information from database
            while (rs.next()) {
                JPanel productContainer = new JPanel(new GridLayout(0, 2, 4, 4));
                productContainer.setBorder(BorderFactory.createLineBorder(Color.black));
                //add information to labels
                productContainer.add(new JLabel("nimi:"));
                productContainer.add(new JLabel(rs.getString("name")));
                productContainer.add(new JLabel("hinta:"));
                productContainer.add(new JLabel(rs.getString("price")));
                productContainer.add(new JLabel("sijainti:"));
                productContainer.add(new JLabel(rs.getString("location")));
                int id = rs.getInt("ID");

                //button for deleting a product
                JButton deleteProduct = new JButton("poista");
                deleteProduct.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //confirmation dialog
                        int confirmResult = JOptionPane.showConfirmDialog(null, "Haluatko varmasti poistaa tuotteen?", "Varoitus", JOptionPane.YES_NO_OPTION);
                        if (confirmResult == JOptionPane.YES_OPTION) {
                            try {
                                if (userDatabase.deleteProduct(id)) {
                                    JOptionPane.showMessageDialog(null, "tuote poistettu.", "onnistui", JOptionPane.INFORMATION_MESSAGE);
                                    SetUserProducts();
                                    //TODO find a way to call SetProducts(false) on main. Otherwise main view won't update.
                                }
                            } catch (SQLException e1) {
                                System.out.println("failed to delete user");
                            }
                        }    
                    }
                });

                //button for editing a product. calls another method
                JButton editButton = new JButton("muokkaa");
                editButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        EditProduct(id);
                    }
                });
                //add to container
                productContainer.add(editButton);
                productContainer.add(deleteProduct);
                productsPanel.add(productContainer);
            }
        } catch (SQLException ex) {
            System.out.println("Failure to add products to user view");
        }
        revalidate();
        repaint();
    }

    //method that creates a new window for editing user information
    private void EditUser(int id) {
        //creating a frame
        JFrame editFrame = new JFrame();
        editFrame.setSize(600,400);
        JPanel innerPanel = new JPanel(new GridLayout(0,2,8,8));
        innerPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        ResultSet rs;
        JLabel username = new JLabel();
        JTextField fullname = new JTextField(32);
        JTextField phonenumber = new JTextField(32);
        JTextField email = new JTextField(32);
        //get old information from database and add to textfields.
        try {
            rs = userDatabase.GetUser(id);
            while (rs.next()) {
                username.setText(rs.getString("username"));
                fullname.setText(rs.getString("fullname"));
                phonenumber.setText(rs.getString("phonenumber"));
                email.setText(rs.getString("email"));
            }          
        } catch (SQLException e) {
            System.out.println("failed to receive user");
        }
        //add components
        innerPanel.add(new JLabel("käyttäjänimi"));
        innerPanel.add(username);
        innerPanel.add(new JLabel("nimi"));
        innerPanel.add(fullname);
        innerPanel.add(new JLabel("puhelinnumero"));
        innerPanel.add(phonenumber);
        innerPanel.add(new JLabel("email"));
        innerPanel.add(email);

        //button for confirmation
        JButton confirm = new JButton("hyväksy");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fullname.getText().isEmpty() || phonenumber.getText().isEmpty() || email.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "yksi tai useampi kenttä oli tyhjä ", "virhe", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String full = fullname.getText();
                    String phone = phonenumber.getText();
                    String mail = email.getText();
                    try {
                        if (userDatabase.UpdateUser(id, full, phone, mail)) {
                            JOptionPane.showMessageDialog(null, "käyttäjä päivitetty", "onnistui", JOptionPane.INFORMATION_MESSAGE);
                            //updates user information in this view
                            setUser();
                            //TODO call mainview to update new information
                            editFrame.dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "käyttäjän päivitys epäonnistui", "virhe", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        System.out.println("user edit went wrong");
                    }
                }
            }
        });

        //button to cancel editing
        JButton back = new JButton("peru");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editFrame.dispose();
            }
        });
        //add buttons
        innerPanel.add(confirm);
        innerPanel.add(back);
        //add panel to frame, center the frame and set it visible
        editFrame.add(innerPanel);
        editFrame.setLocationRelativeTo(null);
        editFrame.setVisible(true);
    }

    //method that creates a new window for editing product information
    private void EditProduct(int id) {
        //create a frame
        JFrame editFrame = new JFrame();
        editFrame.setSize(600,400);
        JPanel innerPanel = new JPanel(new GridLayout(0,2,8,8));
        innerPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        ResultSet rs1;
        JTextField name = new JTextField(32);
        JTextField price = new JTextField(32);
        JComboBox location = new JComboBox<>(Main.LocationList);
        JTextField description = new JTextField(32);
        JComboBox category = new JComboBox<>(Main.CateroryList);
        //get old information from database and add to textfields.
        try {
            rs1 = userDatabase.GetProductById(id);
            while (rs1.next()) {
                name.setText(rs1.getString("name"));
                price.setText(rs1.getString("price"));
                description.setText(rs1.getString("description"));
            }          
        } catch (SQLException e) {
            System.out.println("failed to receive product");
        }
        //add to panel
        innerPanel.add(new JLabel("nimi"));
        innerPanel.add(name);
        innerPanel.add(new JLabel("hinta"));
        innerPanel.add(price);
        innerPanel.add(new JLabel("kuvaus"));
        innerPanel.add(description);
        innerPanel.add(new JLabel("kategoria"));
        innerPanel.add(category);
        innerPanel.add(new JLabel("sijainti"));
        innerPanel.add(location);

        //confirmation button
        JButton confirm = new JButton("hyväksy");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLocation = (String) location.getSelectedItem();
                String selectedCategory = (String) category.getSelectedItem();
                if (name.getText().isEmpty() || price.getText().isEmpty() || selectedLocation.equals("sijainti") || description.getText().isEmpty() || selectedCategory.equals("kategoria")) {
                    JOptionPane.showMessageDialog(null, "yksi tai useampi kenttä oli tyhjä (tai valitse kategoria ja sijainti) ", "virhe", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String nam = name.getText();
                    String pri = price.getText();
                    String loca = selectedLocation;
                    String desc = description.getText();
                    String cate = selectedCategory;
                    //update the database
                    try {
                        if (userDatabase.updateProductById(id, nam, pri, loca, desc, cate)) {
                            JOptionPane.showMessageDialog(null, "Tuote päivitetty", "onnistui", JOptionPane.INFORMATION_MESSAGE);
                            //update this view to reflect changes
                            SetUserProducts();
                            //TODO call mainview
                            editFrame.dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Tuotteen päivitys epäonnistui", "virhe", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        System.out.println("product edit went wrong");
                    }
                }
            }
        });

        //cancel button
        JButton back = new JButton("peru");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editFrame.dispose();
            }
        });

        //add buttons to panel
        innerPanel.add(confirm);
        innerPanel.add(back);

        //add panel to frame, center the frame and set visible
        editFrame.add(innerPanel);
        editFrame.setLocationRelativeTo(null);
        editFrame.setVisible(true);
    }
}
