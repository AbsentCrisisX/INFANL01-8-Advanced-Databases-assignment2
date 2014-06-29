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
 *         In REPEATABLE READ isolation levels Shared locks are acquired.
 *         This prevents data modification when other transaction is reading the
 *         rows and also prevents data read when other transaction are modifying
 *         the rows.
 * 
 */
public class UnrepeatableReadSim {

	public void startSim() { // The method in which all of the simulation
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

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

					String sqlReadAmount = "SELECT st_amount FROM stocks WHERE p_id='12';";
					String updateAmount = "UPDATE stocks SET st_amount='"+rnnb+"' WHERE p_id='12';";

					tranA = dbA.conn.prepareStatement(sqlReadAmount);
					ResultSet rs = tranA.executeQuery();
					
					int readA = 0;
					int readB = 0;

					while (rs.next()) {
						readA =  rs.getInt("st_amount");
						System.out.println("First read: " + readA);
					}

					tranB = dbB.conn.prepareStatement(updateAmount);
					tranB.executeUpdate();
					dbB.conn.commit();

					rs = tranA.executeQuery();
					while (rs.next()) {
						readB = rs.getInt("st_amount");
						System.out.println("Second read: " + readB);
					}
					
					if(readA != readB){
						System.out.println("Non-repeatable read detected! The data of the two reads are not the same.");
					}
					dbA.conn.commit();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

}