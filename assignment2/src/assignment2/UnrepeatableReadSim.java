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
import java.util.Random;

/**
 *
 * @author ACX & Nabelz
 */
public class UnrepeatableReadSim {
    
    public void UnrepeatableReadSim(){
        
    }
    
    public void startSim(){ // The method in which all of the simulation settings are stored
        try{
            // Create an instance of the database class and make a connection
            Database db = new Database();
            db.conn.setAutoCommit(false);
            db.conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            ResultSet rs;
            
            // Prepare the select query that will be used in this simulation
            final String selectQuery = "SELECT m_id FROM mutations WHERE p_id = 1";
            
            // Execute the select query for the first time to create a resultset to compare with
            rs = db.stmt.executeQuery(selectQuery);
            System.out.println(selectQuery);
            while (rs.next()) {
                int mId = rs.getInt("m_id");
                System.out.println(mId + "\t");
            }
            System.out.println("---------------\t\t");
            
            // Start the first insert/update query thread
            new Thread(new Runnable() { 
                @Override 
                public void run() {
                    Database db = new Database();
                    // Generate a random number for mutation purposes 
                    Random rn = new Random();
                    String descr = null;
                    PreparedStatement transactionA;
                    
                    // Start the for loop, to run two different queries
                    for(int i = 1; i<=3;i++){
                        int rnnb = rn.nextInt(10); // Get the random mutation number
                        int np = rn.nextInt(2); // Get a random number
                        switch (np) { // Determine if the random number wil create a positive or a negative mutation
                         case 0:
                          int rnnbTemp = rnnb;
                          rnnb = (rnnbTemp - rnnb) - rnnb;
                          descr = "Sold to customer";
                          break;
                         case 1:
                          descr = "Supplied by supplier";
                          break;
                        }
                        
                        // Prepare 3 of the 4 queries that will be used
                        String sqlReadAmount = "SELECT st_amount FROM stocks WHERE p_id = 1 LIMIT 1;";
 			String sqlTotalAmount = "SELECT SUM(mutation) AS total FROM mutations WHERE p_id = 1";
 			String sqlInsertMutation = "INSERT INTO mutations (mutation, description, p_id) VALUES (" + rnnb + ",'" + descr + "', 1)";
                        
                        // prepare the values for determining the total stock
        		int stAmount = 0;
 			int totalAmount = 0;
                        
                        try{
                            transactionA = db.conn.prepareStatement(sqlInsertMutation);
                            transactionA.executeUpdate(); // Execute the insert into mutations query
                            ResultSet rsRA = db.stmt.executeQuery(sqlReadAmount); // Get the results of the stock retriever
                        
                            while (rsRA.next()) {
                                    stAmount = rsRA.getInt("st_amount"); // Save the result in a variable
                                    System.out.println("Total amount before:" + stAmount);
                            }
                            ResultSet rsTA = db.stmt.executeQuery(sqlTotalAmount); // Get the results of the total mutations that have been made
                            while (rsTA.next()) {
                                 totalAmount = rsTA.getInt("total"); // Save the result in a variable
                            }

                            String sqlUpdateAmount = "UPDATE stocks SET st_amount = '" + totalAmount
                                            + "' WHERE p_id = 1"; // prepare the query for updating the stock
                            transactionA = db.conn.prepareStatement(sqlUpdateAmount);
                            transactionA.executeUpdate(); // Run the stock update
                        } catch (SQLException e){
                            e.printStackTrace();
                        }
                        
                    }
                } 
            }, "Insert thread").start(); // Finish defining the thread and start the thread

            // Start the second insert/update query thread
            new Thread(new Runnable() { 
                @Override 
                public void run() {
                    Database db = new Database();
                    // Generate a random number for mutation purposes 
                    Random rn = new Random();
                    String descr = null;
                    PreparedStatement transactionA;
                    
                    // Start the for loop, to run two different queries
                    for(int i = 1; i<=3;i++){
                        int rnnb = rn.nextInt(10); // Get the random mutation number
                        int np = rn.nextInt(2); // Get a random number
                        switch (np) { // Determine if the random number wil create a positive or a negative mutation
                         case 0:
                          int rnnbTemp = rnnb;
                          rnnb = (rnnbTemp - rnnb) - rnnb;
                          descr = "Sold to customer";
                          break;
                         case 1:
                          descr = "Supplied by supplier";
                          break;
                        }
                        
                        // Prepare 3 of the 4 queries that will be used
                        String sqlReadAmount = "SELECT st_amount FROM stocks WHERE p_id = 1 LIMIT 1;";
 			String sqlTotalAmount = "SELECT SUM(mutation) AS total FROM mutations WHERE p_id = 1";
 			String sqlInsertMutation = "INSERT INTO mutations (mutation, description, p_id) VALUES (" + rnnb + ",'" + descr + "', 1)";
                        
                        // prepare the values for determining the total stock
        		int stAmount = 0;
 			int totalAmount = 0;
                        
                        try{
                            transactionA = db.conn.prepareStatement(sqlInsertMutation);
                            transactionA.executeUpdate(); // Execute the insert into mutations query
                            ResultSet rsRA = db.stmt.executeQuery(sqlReadAmount); // Get the results of the stock retriever
                        
                            while (rsRA.next()) {
                                    stAmount = rsRA.getInt("st_amount"); // Save the result in a variable
                                    System.out.println("Total amount before:" + stAmount);
                            }
                            ResultSet rsTA = db.stmt.executeQuery(sqlTotalAmount); // Get the results of the total mutations that have been made
                            while (rsTA.next()) {
                                 totalAmount = rsTA.getInt("total"); // Save the result in a variable
                            }

                            String sqlUpdateAmount = "UPDATE stocks SET st_amount = '" + totalAmount
                                            + "' WHERE p_id = 1"; // prepare the query for updating the stock
                            transactionA = db.conn.prepareStatement(sqlUpdateAmount);
                            transactionA.executeUpdate(); // Run the stock update
                        } catch (SQLException e){
                            e.printStackTrace();
                        }
                        
                    }
                } 
            }, "Insert thread").start(); // Finish defining the thread and start the thread
            
            // Run the last Select Query
            rs = db.stmt.executeQuery(selectQuery);
            System.out.println(selectQuery);
            while (rs.next()) {
                int mId = rs.getInt("m_id");
                System.out.println(mId + "\t");
            }
            System.out.println("---------------\t\t");
            
            db.conn.commit(); // Commit the queries
            db.stmt.close();
            db.conn.close(); // Close the connection to the database
        } catch(SQLException e){
            e.printStackTrace();
        } finally {
            System.out.println("Database connection closed...");
        }
    }
}