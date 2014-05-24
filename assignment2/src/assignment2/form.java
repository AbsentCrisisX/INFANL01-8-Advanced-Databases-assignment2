/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package assignment2;

import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Roronoa
 */
public class form {
    public static void main(String[] args) {
        Frame frm = new Frame("Form frame");
        Label lbl = new Label("Please select the simulation and the amount of threads:");
        frm.add(lbl);
        frm.setSize(350,200);
        frm.setVisible(true);
        frm.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        
        Panel p = new Panel();
        Panel p1 = new Panel();
        Label jFirstName = new Label("First Name");
        TextField lFirstName = new TextField(20);
        p.setLayout(new GridLayout(1,1));
        p.add(jFirstName);
        p.add(lFirstName);
        p1.add(p);
        frm.add(p1, BorderLayout.NORTH);
    }
}
