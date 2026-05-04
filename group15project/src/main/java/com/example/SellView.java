package com.example;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SellView extends JPanel {

    JLabel topHeader;
    JLabel nameLabel;
    JLabel priceLabel;
    JLabel locationLabel;
    JLabel DescriptionLabel;
    JLabel categoryLabel;
    

    JTextField name;
    JTextField price;
    JTextField location;
    JTextField description;
    JTextField category;

    JButton ConfirmButton;
    JButton BackButton;

    UserDatabase userDatabase = new UserDatabase();
    
    public SellView(Runnable BackToMain, MainView mainView) {
        setLayout(new FlowLayout());

        name = new JTextField(32);
        price = new JTextField(32);
        location = new JTextField(32);
        description = new JTextField(32);
        category = new JTextField(32);
        
        topHeader = new JLabel("Myydään");
        nameLabel = new JLabel("nimi");
        priceLabel = new JLabel("hinta");
        locationLabel = new JLabel("sijainti");
        DescriptionLabel = new JLabel("kuvaus");
        categoryLabel = new JLabel("kategoria");

        BackButton = new JButton("takaisin");
        BackButton.addActionListener(e -> BackToMain.run());

        ConfirmButton = new JButton("hyväksy");
        ConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO catch empty
                String nam = name.getText();
                String pri = price.getText();
                String loca = location.getText();
                String desc = description.getText();
                String cate = category.getText();

                try {
                    
                    if (userDatabase.AddProduct(nam, pri, loca, desc, cate)) {
                        JOptionPane.showMessageDialog(null, "Uusi tuote lisätty", "onnistui", JOptionPane.INFORMATION_MESSAGE); 
                        System.out.println("toimii");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Tuotteen lisäys epäonnistui", "virhe", JOptionPane.INFORMATION_MESSAGE);     
                        System.out.println("ei toimi");           
                    }
                    
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("product add went wrong");
                }
            }
        });

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
