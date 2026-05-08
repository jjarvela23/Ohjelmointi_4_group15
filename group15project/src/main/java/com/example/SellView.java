package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

//view for selling new products. Only available after logging in.
public class SellView extends JPanel {

    JLabel topHeader;
    JLabel nameLabel;
    JLabel priceLabel;
    JLabel locationLabel;
    JLabel DescriptionLabel;
    JLabel categoryLabel;

    JTextField name;
    JTextField price;
    JTextField description;
    JComboBox<String> location;
    JComboBox<String> category;

    JButton ConfirmButton;
    JButton BackButton;

    Dimension d = new Dimension(0, 15);

    UserDatabase userDatabase = new UserDatabase();

    public SellView(Runnable BackToMain, MainView mainView) {
        //set layout 
        setLayout(new FlowLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setSize(new Dimension(800,600));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(10, 10, 10, 10)));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //all text fields for the user input. Description is rectangular.
        name = new JTextField(32);
        price = new JTextField(32);
        location = new JComboBox<>(Main.LocationList);
        category = new JComboBox<>(Main.CateroryList);
        description = new JTextField(32);

        //labels which go next to text fields.
        topHeader = new JLabel("Myydään");
        nameLabel = new JLabel("nimi");
        priceLabel = new JLabel("hinta");
        locationLabel = new JLabel("sijainti");
        DescriptionLabel = new JLabel("kuvaus");
        categoryLabel = new JLabel("kategoria");

        //return to main view
        BackButton = new JButton("takaisin");
        BackButton.addActionListener(e -> BackToMain.run());

        //confirm the selling.
        ConfirmButton = new JButton("hyväksy");
        ConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLocation = (String) location.getSelectedItem();
                String selectedCategory = (String) category.getSelectedItem();

                //check that no field is empty.
                if (name.getText().isEmpty() || price.getText().isEmpty() || selectedLocation.equals("sijainti") || description.getText().isEmpty() || selectedCategory.equals("kategoria")) {
                    JOptionPane.showMessageDialog(null, "yksi tai useampi kenttä oli tyhjä (tai valitse kategoria ja sijainti) ", "virhe", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    //get inputs.
                    String nam = name.getText();
                    String pri = price.getText();
                    String loca = selectedLocation;
                    String desc = description.getText();
                    String cate = selectedCategory;

                    try {
                        //add product to database. Method returns true or false.              
                        if (userDatabase.AddProduct(nam, pri, loca, desc, cate)) {
                            JOptionPane.showMessageDialog(null, "Uusi tuote lisätty", "onnistui", JOptionPane.INFORMATION_MESSAGE);
                            mainView.SetProducts(false);
                            name.setText("");
                            price.setText("");
                            description.setText("");
                            location.setSelectedIndex(0);
                            category.setSelectedIndex(0);
                            BackToMain.run();

                        } else {
                            JOptionPane.showMessageDialog(null, "Tuotteen lisäys epäonnistui", "virhe", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (SQLException ex) {
                        System.out.println("product add went wrong");
                    }
                }
            }
        });

        //add components to the panel
        JPanel topRow = new JPanel(new BorderLayout(200,0));
        topRow.add(Box.createRigidArea(d), BorderLayout.WEST);
        topRow.add(topHeader, BorderLayout.CENTER);
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.add(ConfirmButton, BorderLayout.WEST);
        buttonRow.add(BackButton, BorderLayout.EAST);
        mainPanel.add(topRow);
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(createRow(nameLabel, name));
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(createRow(priceLabel, price));
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(createRow(DescriptionLabel, description));
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(createRow2(categoryLabel, category));
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(createRow2(locationLabel, location));
        mainPanel.add(Box.createRigidArea(d));
        mainPanel.add(buttonRow);

        this.add(mainPanel);

    }

    //method to create label + textfield rows
    private JPanel createRow(JLabel label, JTextField text) {
            JPanel row = new JPanel(new BorderLayout(10,0));
            row.add(label, BorderLayout.CENTER);
            row.add(text, BorderLayout.EAST);
            return row;
        }
    //same but for combobox
    private JPanel createRow2(JLabel label, JComboBox text) {
            JPanel row = new JPanel(new BorderLayout(10,0));
            row.add(label, BorderLayout.CENTER);
            row.add(text, BorderLayout.EAST);
            return row;
        }
}
