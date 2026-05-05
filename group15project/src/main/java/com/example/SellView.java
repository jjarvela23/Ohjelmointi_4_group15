package com.example;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SellView extends JPanel {

    JLabel topHeader;
    JLabel nameLabel;
    JLabel priceLabel;
    JLabel locationLabel;
    JLabel DescriptionLabel;
    JLabel categoryLabel;

    //TODO turn location and category to comboboxes
    JTextField name;
    JTextField price;
    JTextField location;
    JTextArea description;
    JTextField category;

    JButton ConfirmButton;
    JButton BackButton;

    UserDatabase userDatabase = new UserDatabase();
    
    public SellView(Runnable BackToMain, MainView mainView) {
        //set layout 
        setLayout(new FlowLayout());

        //all text fields for the user input. Description is rectangular.
        name = new JTextField(32);
        price = new JTextField(32);
        location = new JTextField(32);
        description = new JTextArea(20, 20);
        category = new JTextField(32);
        
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
                //check that no field is empty.
                if (name.getText().isEmpty() || price.getText().isEmpty() || location.getText().isEmpty() || description.getText().isEmpty() || category.getText().isEmpty()) { 
                    JOptionPane.showMessageDialog(null, "yksi tai useampi kenttä oli tyhjä", "virhe", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    //get inputs.
                    String nam = name.getText();
                    String pri = price.getText();
                    String loca = location.getText();
                    String desc = description.getText();
                    String cate = category.getText();
                    
                    try {         
                        //add product to database. Method returns true or false.              
                        if (userDatabase.AddProduct(nam, pri, loca, desc, cate)) {
                            JOptionPane.showMessageDialog(null, "Uusi tuote lisätty", "onnistui", JOptionPane.INFORMATION_MESSAGE); 
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Tuotteen lisäys epäonnistui", "virhe", JOptionPane.INFORMATION_MESSAGE);             
                        }
                        
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        System.out.println("product add went wrong");
                    }
                }
            }
        });

        //add components to the panel
        add(BackButton);
        add(ConfirmButton);
        add(topHeader);
        add(nameLabel);
        add(name);
        add(priceLabel);
        add(price);
        add(DescriptionLabel);
        add(description);
        add(categoryLabel);
        add(category);
        add(locationLabel);
        add(location);

    }
}
