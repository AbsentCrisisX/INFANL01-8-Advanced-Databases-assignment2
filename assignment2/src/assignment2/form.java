/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package assignment2;

import java.awt.*;
import java.awt.event.*;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;

/**
 *
 * @author Roronoa
 */
public class form {
    public static void main(String[] args) {
        Frame frm = new Frame("Select Simulation and threads");
        frm.setSize(350,200);
        frm.setVisible(true);
        frm.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        
        Panel p = new Panel();
        Panel p1 = new Panel();
        
        CheckboxGroup sim = new CheckboxGroup();
        
        Checkbox dirty = new Checkbox("Dirty read", sim, true);
        /*dirty.addActionListener(new ActionListener() {
   
            @Override
            public void actionPerformed(ActionEvent arg0) {
            // TODO Auto-generated method stub
            Database db = new Database();
    
            }
        });*/
        Checkbox unrep = new Checkbox("Unrepeatable read", sim, false);
        Checkbox phant = new Checkbox("Phantom read", sim, false);
        Checkbox deadl = new Checkbox("Deadlock", sim, false);
        
        Choice numChoice = new Choice();
        for(int i=1;i<=20;i++){
            numChoice.add(""+i+"");
        }
        
        Button submit = new Button();
        submit.setLabel("Start de simulatie");
        
        p.setLayout(new GridLayout(3,1));
        
        p.add(dirty);
        p.add(unrep);
        p.add(phant);
        p.add(deadl);
        p.add(numChoice);
        p.add(submit);
        
        p1.add(p);
        frm.add(p1, BorderLayout.NORTH);
    }
}
