/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package assignment2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ACX
 */
public class DeadlockSim {
    public int simNum = 0;
    public int thNum = 0;
    
    public DeadlockSim(){
        
    }
    
    public void startSim(int threads, int sims){
        this.simNum = sims;
        this.thNum = threads;
        
        for(int i=1;i<=thNum;i++){
            new Thread(new Runnable() { 
                @Override 
                public void run() { 
                    for(int j=1;j<=simNum;j++){
                        Database db = new Database();
                        
                        try {
							db.conn.setAutoCommit(false);
							PreparedStatement preparedA = null;
							db.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
							
							String lockQuery = "LOCK mutations IN ACCESS EXCLUSIVE MODE;";
							String insertQuery = " INSERT INTO mutations (mutation, description, p_id) VALUES (0 , 'deadlock', 1);";
							
							//preparedA = db.conn.prepareStatement(lockQuery);
							//preparedA.executeUpdate(lockQuery);
							ResultSet a = db.stmt.executeQuery(lockQuery);
							ResultSet b = db.stmt.executeQuery(insertQuery);
							//preparedA = db.conn.prepareStatement(insertQuery);
							//preparedA.executeUpdate(insertQuery);
							
							db.conn.commit();
							
						db.conn.close();
						System.out.println("Database closed...");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
                        
                        
                    }
                } 
            }, "Thread "+i).start(); 
        }
    }
}
