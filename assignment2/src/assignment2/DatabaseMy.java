package assignment2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseMy {
	final String DB_NAME = "opdrachttwee";
	final String DB_URL = "jdbc:mysql://localhost/" + DB_NAME;

	// Database accountinstellingen
	final String USER = "rootjava";
	final String PASS = "1234";
	public Connection conn = null;
	public Statement stmt = null;

	public DatabaseMy() {
		try {
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connecting to database failed...");
			e.printStackTrace();

		}
		// printAllProducts();
		// exampleTransaction();
	}

}
