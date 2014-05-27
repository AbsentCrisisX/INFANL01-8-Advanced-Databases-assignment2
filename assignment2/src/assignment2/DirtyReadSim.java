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
public class DirtyReadSim {
	public int simNum = 0;
	public int thNum = 0;

	public DirtyReadSim() {
		this.thNum = 4;
		this.simNum = 4;
	}

	public void startSim() {
		// for each thread start a thread
		for (int i = 1; i <= thNum; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						// make a new database object of mysql
						DatabaseMy db = new DatabaseMy();
						// set autocommit off to make transactions
						db.conn.setAutoCommit(false);
						// set the transaction to read uncommitted
						db.conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
						PreparedStatement transactionA;

						for (int j = 1; j <= simNum; j++) {

							for (int pid = 1; pid <= 1; pid++) {

								Random rn = new Random();
								// rnnb gets a random number between 0 and 10
								int rnnb = rn.nextInt(10);
								// np means negative of positve number
								int np = rn.nextInt(2);
								String descr = null;
								switch (np) {
									case 0:
										// when np is zero make it a negative
										// number and describe it as a sold to
										// customer
										int rnnbTemp = rnnb;
										rnnb = (rnnbTemp - rnnb) - rnnb;
										descr = "Sold to customer";
										break;
									case 1:
										// else make it a supplied by a supplier
										// description
										descr = "Supplied by supplier";
										break;
								}

								// sql queries to read the ammount. amount in
								// mutations and a insert query
								String sqlReadAmount = "SELECT st_amount FROM " + db.DB_NAME + ".stocks WHERE p_id='"
										+ pid + "' LIMIT 1;";
								String sqlTotalAmount = "SELECT SUM(mutation) FROM " + db.DB_NAME
										+ ".mutations WHERE p_id = " + pid + "";
								String sqlInsertMutation = "INSERT INTO mutations (mutation, description, p_id) VALUES ('"
										+ rnnb + "','" + descr + "', '" + pid + "')";

								int stAmount = 0;
								int totalAmount = 0;

								transactionA = db.conn.prepareStatement(sqlInsertMutation);
								transactionA.executeUpdate();
								ResultSet rsRA = db.stmt.executeQuery(sqlReadAmount);

								// display the total amount before the dirty
								// read
								while (rsRA.next()) {
									stAmount = rsRA.getInt("st_amount");
									System.out.println("Total amount before:" + stAmount);
								}

								// make a query to update the stock
								String sqlUpdateAmount = "UPDATE stocks SET st_amount = '" + totalAmount
										+ "' WHERE p_id = " + pid + "";

								// execute the query
								transactionA = db.conn.prepareStatement(sqlUpdateAmount);
								transactionA.executeUpdate();

								// get the amount of mutations and see that it is a different number
								// this simulated the dirty read
								ResultSet rsTA = db.stmt.executeQuery(sqlTotalAmount);
								while (rsTA.next()) {
									totalAmount = rsTA.getInt("SUM(mutation)");
									System.out.println("Total in mutations:" + totalAmount);
								}
								System.out.println();

								db.conn.rollback();
							}
						}

						db.stmt.close();
						db.conn.close();
					} catch (SQLException e) {

						e.printStackTrace();
					} finally {
						System.out.println("Database connection closed...");
					}
				}
			}, "Thread " + i).start();

		}

	}
}
