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
        Frame frm = new Frame("Select Simulation"); // Create the frame for the form
        frm.setSize(350,200); // Set the size of the frame
        frm.setVisible(true); // Define if the frame should be visible
        frm.addWindowListener(new WindowAdapter(){ // Make sure the window can be closed by the cross button
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        
        Panel p = new Panel(); // Create the panel in which the form will be placed
        Panel p1 = new Panel(); // Create the panel in which the previous panel will be placed to give a little padding in the frame
        
        final CheckboxGroup sim = new CheckboxGroup(); // Create the group for the radio boxes
        
        // Add the radio buttons to the group
        Checkbox dirty = new Checkbox("Dirty read", sim, true);
        Checkbox unrep = new Checkbox("Unrepeatable read", sim, false);
        Checkbox phant = new Checkbox("Phantom read", sim, false);
        Checkbox deadl = new Checkbox("Deadlock", sim, false);
        
        // Create the select box for the Deadlock thread selection
        final Choice numChoice = new Choice();
        for(int i=1;i<=20;i++){
            numChoice.add(""+i+"");
        }
        Label numChoiceLabel = new Label();
        numChoiceLabel.setText("Deadlock threads: ");
        
        // Create the submit button
        Button submit = new Button();
        submit.setLabel("Start de simulatie");
        
        // Add an onClick listener to the submit button
        submit.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                switch(sim.getSelectedCheckbox().getLabel()){
                    case "Dirty read": 
                    	DirtyReadSim dirtyRead = new DirtyReadSim();
                        dirtyRead.startSim();
                        break;
                    case "Unrepeatable read": 
                        UnrepeatableReadSim unrepeatRead = new UnrepeatableReadSim();
                        unrepeatRead.startSim();
                        break;
                    case "Phantom read": 
                        PhantomReadSim phantRead = new PhantomReadSim();
                        phantRead.startSim();
                        break;
                    case "Deadlock": 
                        DeadlockSim deadLock = new DeadlockSim();
                        deadLock.startSim(numChoice.getSelectedIndex()+1);
                        break;
                }
            } 
        });
        
        // Create the form layout
        p.setLayout(new GridLayout(5,1));
        
        // Add all elements to the form
        p.add(dirty);
        p.add(unrep);
        p.add(phant);
        p.add(deadl);
        p.add(numChoiceLabel);
        p.add(numChoice);
        p.add(submit);
        
        // Add the form to the padding panel
        p1.add(p);
        // Add the whole form to the frame
        frm.add(p1, BorderLayout.NORTH);
    }
}
