/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package assignment2;

/**
 *
 * @author ACX
 */
public class DirtyReadSim {
    public int simNum = 0;
    public int thNum = 0;
    
    public void DirtyReadSim(){
        
    }
    
    public void startSim(int threads, int sims){
        this.simNum = sims;
        this.thNum = threads;
        
        for(int i=1;i<=thNum;i++){
            new Thread(new Runnable() { 
                @Override 
                public void run() { 
                    for(int j=1;j<=simNum;j++){
                        //hier de simulatie logica
                        System.out.println("Simulation " +j);
                    }
                } 
            }, "Thread "+i).start(); 
        }
    }
}
