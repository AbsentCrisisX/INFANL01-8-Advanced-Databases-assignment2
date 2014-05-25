/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package assignment2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

/**
 * 
 * @author ACX
 */
public class DirtyReadSim {
	public int simNum = 0;
	public int thNum = 0;

	public DirtyReadSim() {

	}

	public void startSim(int threads, int sims) {
		this.simNum = sims;
		this.thNum = threads;

		for (int i = 1; i <= thNum; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Database db = new Database();
						db.conn.setAutoCommit(false);
						db.conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
						PreparedStatement transactionSql;

						for (int j = 1; j <= simNum; j++) {

							for (int pid = 1; pid <= 4; pid++) {

								for (int i = 0; i <= 3; i++) {
									Random rn = new Random();
									int rnnb = rn.nextInt(10);
									int np = rn.nextInt(2);
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
									/*
									 * String sql =
									 * "insert into mutations (mutation, description, p_id) values ('"
									 * + rnnb + "', '"+ descr +"', 1);";
									 * String sql2 =
									 * "update stocks  SET st_amount=";
									 */
									System.out.println("rnnb: " + rnnb);
									String sql = "insert into mutations (mutation, description, p_id) values ("
											+ rnnb
											+ ", '"
											+ descr
											+ "', "
											+ pid
											+ "); update stocks  SET st_amount=((select st_amount from stocks where p_id="
											+ pid + ") + (select mutation from mutations where p_id=" + pid
											+ " order by m_id desc limit 1)) where p_id=" + pid + ";";
									transactionSql = db.conn.prepareStatement(sql);
									transactionSql.executeUpdate();
								}
							}
						}
						db.conn.commit();

						db.stmt.close();
						db.conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						System.out.println("Database connection closed...");
					}
				}
			}, "Thread " + i).start();

		}

	}
}
