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
import javax.swing.*;

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
        
        final CheckboxGroup sim = new CheckboxGroup();
        
        Checkbox dirty = new Checkbox("Dirty read", sim, true);
        Checkbox unrep = new Checkbox("Unrepeatable read", sim, false);
        Checkbox phant = new Checkbox("Phantom read", sim, false);
        Checkbox deadl = new Checkbox("Deadlock", sim, false);
        
        final Choice numChoice = new Choice();
        for(int i=1;i<=20;i++){
            numChoice.add(""+i+"");
        }
        Label numChoiceLabel = new Label();
        numChoiceLabel.setText("Threads: ");
        
        final Choice simNumChoice = new Choice();
        for (int i=1; i<=20;i++){
            simNumChoice.add(""+i+"");
        }
        Label simNumChoiceLabel = new Label();
        simNumChoiceLabel.setText("Simulaties: ");
        
        Button submit = new Button();
        submit.setLabel("Start de simulatie");
        
        submit.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                switch(sim.getSelectedCheckbox().getLabel()){
                    case "Dirty read": 
                    	DirtyReadSim dirtyRead = new DirtyReadSim();
                        dirtyRead.startSim(numChoice.getSelectedIndex()+1, simNumChoice.getSelectedIndex()+1);
                        break;
                    case "Unrepeatable read": 
                        UnrepeatableReadSim unrepeatRead = new UnrepeatableReadSim();
                        unrepeatRead.startSim(numChoice.getSelectedIndex()+1, simNumChoice.getSelectedIndex()+1);
                        break;
                    case "Phantom read": 
                        PhantomReadSim phantRead = new PhantomReadSim();
                        phantRead.startSim(numChoice.getSelectedIndex()+1, simNumChoice.getSelectedIndex()+1);
                        break;
                    case "Deadlock": 
                        DeadlockSim deadLock = new DeadlockSim();
                        deadLock.startSim(numChoice.getSelectedIndex()+1, simNumChoice.getSelectedIndex()+1);
                        break;
                }
            } 
        });
        
        p.setLayout(new GridLayout(5,1));
        
        p.add(dirty);
        p.add(unrep);
        p.add(phant);
        p.add(deadl);
        p.add(numChoiceLabel);
        p.add(numChoice);
        p.add(simNumChoiceLabel);
        p.add(simNumChoice);
        p.add(submit);
        
        p1.add(p);
        frm.add(p1, BorderLayout.NORTH);
    }
}
