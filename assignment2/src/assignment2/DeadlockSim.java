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
    	simNum = 1;
    }
    
    public void startSim(int threads){
        this.thNum = threads;
        
        // for each thread start a thread
        for(int i=1;i<=thNum;i++){
            new Thread(new Runnable() { 
                @Override 
                public void run() {
                	// for each simulation make a simulation
                    for(int j=1;j<=simNum;j++){
                    	// get the postgresql databse connection
                        Database db = new Database();
                        
                        try {
                        	// with autocommit we can make transactions
							db.conn.setAutoCommit(false);
							// set the transaction to uncommitted
							db.conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
							
							// sql queries for lock table and a insert table
							String lockQuery = "LOCK mutations IN ACCESS EXCLUSIVE MODE;";
							String insertQuery = " INSERT INTO mutations (mutation, description, p_id) VALUES (0 , 'deadlock', 1);";
							
							// execute the queries
							db.stmt.executeQuery(lockQuery);
							db.stmt.executeQuery(insertQuery);
							
							
							db.conn.commit();
							
						db.conn.close();
						System.out.println("Database closed...");
						} catch (SQLException e) {
							
						}
                        
                        
                    }
                } 
            }, "Thread "+i).start(); 
        }
    }
}
