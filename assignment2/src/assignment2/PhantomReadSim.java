package assignment2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * 
 * @author ACX & Nabelz
 * 
 * PHANTOM READS: Data getting changed in current transaction by other transactions is called Phantom Reads. 
 * New rows can be added by other transactions, so you get different number of rows by firing same query in current transaction.
 * 
 */
public class PhantomReadSim {

	public void startSim() { // The method in which all of the simulation
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// make 2 new database connections
					// set autocommit off to make transactions
					// set the transaction to read committed (just in case)


					Database dbA = new Database();
					Database dbB = new Database();
					dbA.conn.setAutoCommit(false);
					dbB.conn.setAutoCommit(false);
					dbA.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
					dbB.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

					PreparedStatement tranA;
					PreparedStatement tranB;

					int rnnb = new Random().nextInt(20);
					int np = new Random().nextInt(2);

					String descr = null;
					switch (np) {
						case 0:
							int rnnbTemp = rnnb;
							rnnb = (rnnbTemp - rnnb) - rnnb;
							descr = "Sold to customer";
							break;
						case 1:
							descr = "Supplied by supplier";
							break;
					}

					String sqlReadAmount = "SELECT SUM(mutation) FROM mutations WHERE p_id='12';";
					String updateAmount = "INSERT INTO mutations (mutation, description, p_id) VALUES ('" + rnnb
							+ "', '" + descr + "', '12');";

					tranA = dbA.conn.prepareStatement(sqlReadAmount);
					ResultSet rs = tranA.executeQuery();
					
					int readA = 0;
					int readB = 0;

					while (rs.next()) {
						readA =  rs.getInt("SUM");
						System.out.println("First read: " +readA);
					}

					tranB = dbB.conn.prepareStatement(updateAmount);
					tranB.executeUpdate();
					dbB.conn.commit();

					//All the rows in the query have the same value before and after, 
					//but but by the update there is a row inserted 
					//This wil change the sum
					rs = tranA.executeQuery();
					while (rs.next()) {
						 readB = rs.getInt("SUM");
						System.out.println("Second read: " + readB);
					}
					
					if(readA != readB){
						System.out.println("Phantom read detected! The sum of the two reads are not the same.");
					}
					dbA.conn.commit();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}
