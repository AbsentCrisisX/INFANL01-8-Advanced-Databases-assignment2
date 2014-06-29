package assignment2;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.util.PSQLException;

/**
 * 
 * @author ACX & Nabelz
 * 
 * 
 * 
 */
public class DeadlockSim {
	final public int SIM_MAX = 1;
	public int totalThreads = 0;

	public void startSim(int threads) {
		this.totalThreads = threads;

		// for each thread start a thread
		for (int i = 1; i <= totalThreads; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// for each simulation make a simulation
					for (int j = 1; j <= SIM_MAX; j++) {
						// get the postgresql database connection
						Database db = new Database();

						try {
							// with autocommit we can make transactions
							db.conn.setAutoCommit(false);
							
							// sql queries for lock table and a insert table
							String lockQuery = "LOCK mutations IN ACCESS EXCLUSIVE MODE;";
							String insertQuery = " INSERT INTO mutations (mutation, description, p_id) VALUES (0 , 'deadlock', 1);";

							// execute the queries
							db.stmt.executeUpdate(lockQuery);
							try{
							db.stmt.executeQuery(insertQuery);
							}catch(PSQLException e){
								//e.printStackTrace();
								System.out.println("A lock in the database has been found");
							}
							
							db.conn.commit();

							db.conn.close();
							System.out.println("Database closed...");
						} catch (SQLException e) {
							e.printStackTrace();
						}

					}
				}
			}, "Thread nr: " + i).start();
		}
	}
}
