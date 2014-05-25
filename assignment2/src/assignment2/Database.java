package assignment2;

import java.sql.*;

public class Database {
	final String DB_NAME = "OpdrachtTwee";
	final String DB_URL = "jdbc:postgresql://localhost:5432/" + DB_NAME;

	// Database accountinstellingen
	final String USER = "postgres";
	final String PASS = "1234";
	Connection conn = null;
	Statement stmt = null;

	public Database() {
		printAllProducts();
	}
	
	public void printAllProducts(){
		try {
			Class.forName("org.postgresql.Driver");

			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt = conn.createStatement();
			String sql;
			sql = "SELECT p_id, p_name FROM products;";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				// Retrieve by column name

				int productId = rs.getInt("p_id");
				String productName = rs.getString("p_name");

				// Display values
				System.out.println("ID:" + productId);
				System.out.println("NAME: " + productName);

			}

			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {

			se.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

}
