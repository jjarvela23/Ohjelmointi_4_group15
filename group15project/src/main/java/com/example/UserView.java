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
        userPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(10, 10, 10, 10)));
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        productsPanel.setPreferredSize(new Dimension(1000, 435));

        title = new JLabel("Oma tili");
        usernameLabel = new JLabel();
        fullnameLabel = new JLabel();
        phonenumberLabel = new JLabel();
        emailLabel = new JLabel();

        backButton = new JButton("etusivu");
        backButton.addActionListener(e -> BackToMain.run());

        deleteUserButton = new JButton("poista tili");
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmResult = JOptionPane.showConfirmDialog(null, "Haluatko varmasti poistaa tilisi?", "Varoitus", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    try {
                        if (userDatabase.DeleteUser(Main.CurrentUser)) {
                            JOptionPane.showMessageDialog(null, "Käyttäjä poistettu. Palataan etusivulle.", "onnistui", JOptionPane.INFORMATION_MESSAGE);
                            Main.CurrentUser = 0;
                            BackToMain.run();
                        }
                    } catch (SQLException e1) {
                        System.out.println("failed to delete user");
                    }
                }    
            }
        });

        userPanel.add(createRow(new JLabel("käyttäjänimi:"), usernameLabel));
        userPanel.add(createRow(new JLabel("nimi:"), fullnameLabel));
        userPanel.add(createRow(new JLabel("puhelinnumero:"), phonenumberLabel));
        userPanel.add(createRow(new JLabel("sähköposti:"), emailLabel));
        userPanel.add(deleteUserButton);
        JPanel box = new JPanel(new FlowLayout());
        box.add(new JLabel("oma tili"));
        box.add(backButton);
        topPanel.add(box, BorderLayout.EAST);
        topPanel.add(userPanel, BorderLayout.WEST);

        //convert productpanel to a scrollable panel
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setPreferredSize(new Dimension(1000, 435));
        this.add(topPanel);
        this.add(scrollPane);
    }

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

    private JPanel createRow(JLabel label, JLabel text) {
            JPanel row = new JPanel(new BorderLayout(10,0));
            row.add(label, BorderLayout.CENTER);
            row.add(text, BorderLayout.EAST);
            return row;
        }

    public void SetUserProducts() {
        productsPanel.removeAll();
        try {
            ResultSet rs = userDatabase.GetProductsFromUser(Main.CurrentUser);
            while (rs.next()) {
                JPanel productContainer = new JPanel(new GridLayout(0, 2, 4, 4));
                productContainer.setBorder(BorderFactory.createLineBorder(Color.black));

                productContainer.add(new JLabel("nimi:"));
                productContainer.add(new JLabel(rs.getString("name")));
                productContainer.add(new JLabel("hinta:"));
                productContainer.add(new JLabel(rs.getString("price")));
                productContainer.add(new JLabel("sijainti:"));
                productContainer.add(new JLabel(rs.getString("location")));
                int id = rs.getInt("ID");

                JButton deleteProduct = new JButton("poista");
                deleteProduct.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int confirmResult = JOptionPane.showConfirmDialog(null, "Haluatko varmasti poistaa tuotteen?", "Varoitus", JOptionPane.YES_NO_OPTION);
                        if (confirmResult == JOptionPane.YES_OPTION) {
                            try {
                                if (userDatabase.deleteProduct(id)) {
                                    JOptionPane.showMessageDialog(null, "tuote poistettu.", "onnistui", JOptionPane.INFORMATION_MESSAGE);
                                    SetUserProducts();
                                    //TODO find a way to call SetProducts(false) on main.
                                }
                            } catch (SQLException e1) {
                                System.out.println("failed to delete user");
                            }
                        }    
                    }
                });

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

    private void EditProduct(int id) {
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
                    
                    try {
                        if (userDatabase.updateProductById(id, nam, pri, loca, desc, cate)) {
                            JOptionPane.showMessageDialog(null, "Tuote päivitetty", "onnistui", JOptionPane.INFORMATION_MESSAGE);
                            SetUserProducts();
                            //TODO call mainview
                            editFrame.dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Tuotteen päivitys epäonnistui", "virhe", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        System.out.println("product edit went wrong");
                    }
                }
            }
        });

        JButton back = new JButton("peru");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editFrame.dispose();
            }
        });

        innerPanel.add(confirm);
        innerPanel.add(back);

        editFrame.add(innerPanel);
        editFrame.setLocationRelativeTo(null);
        editFrame.setVisible(true);
    }
}
