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
	final public int SIM_MAX = 4;
	final public int TH_MAX = 4;

	public void startSim() {
		// for each thread start a thread
		for (int i = 1; i <= 1; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						// make a new database object of mysql
						// set autocommit off to make transactions
						// set the transaction to read uncommitted

						DatabaseMy db = new DatabaseMy();
						db.conn.setAutoCommit(false);
						db.conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
						PreparedStatement tranA;
						PreparedStatement tranB;

						for (int j = 1; j <= 1; j++) {

							for (int pid = 1; pid <= 1; pid++) {

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

								// sql queries to read the amount. amount in
								// mutations and a insert query
								String sqlReadAmount = "SELECT st_amount FROM " + db.DB_NAME + ".stocks WHERE p_id='" + pid + "' LIMIT 1;";
								String sqlInsertMutation = "UPDATE stocks SET st_amount='"+rnnb+"' WHERE p_id= '"+ pid + "';";

								System.out.println(sqlInsertMutation);
								int readA = 0;
								int readB = 0;
								
								ResultSet rs = db.stmt.executeQuery(sqlReadAmount);
								while (rs.next()) {
									readA = rs.getInt("st_amount");
									System.out.println("Read A: " + readA);
								}

								tranA = db.conn.prepareStatement(sqlInsertMutation);
								tranA.executeUpdate();

								rs = db.stmt.executeQuery(sqlReadAmount);
								while (rs.next()) {
									readB = rs.getInt("st_amount");
									System.out.println("Read B: " + readB);
								}
								System.out.println();
								if(readA != readB){
									System.out.println("DIRTY READ FOUND! READ A: " + readA + " & READ B: "+ readB);
								}

								//db.conn.commit();
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
			}, "Thread number: " + i).start();

		}

	}
}
