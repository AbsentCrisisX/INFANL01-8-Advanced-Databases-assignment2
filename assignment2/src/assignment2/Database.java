package assignment2;

import java.sql.*;

public class Database {

	public Database() {

		final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

		// Database accountinstellingen
		final String USER = "postgres";
		final String PASS = "1234";
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");

			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt = conn.createStatement();
			String sql;
			sql = "SELECT p_id, products FROM products";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				// Retrieve by column name
				/*
				 * int id = rs.getInt("id");
				 * int age = rs.getInt("age");
				 * String first = rs.getString("first");
				 * String last = rs.getString("last");
				 */
				// Display values
				/*
				 * System.out.print("ID: " + id);
				 * System.out.print(", Age: " + age);
				 * System.out.print(", First: " + first);
				 * System.out.println(", Last: " + last);
				 */
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
