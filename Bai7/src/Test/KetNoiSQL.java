package Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class KetNoiSQL {
	private String user = "sa";
	private String password = "123456";
	private String url = "jdbc:sqlserver://localhost:1433;databaseName=UploadFileServletDB";
	private Connection conn;

	public KetNoiSQL() throws SQLException, ClassNotFoundException {
		DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
		// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		conn = DriverManager.getConnection(url, user, password);
	}

	public String Check() throws SQLException {
		String x = "";
		Statement statement = conn.createStatement();
		String sql = String.format("select first_name,last_name from contacts");
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			String a = resultSet.getString(1);
			String b = resultSet.getString(2);
			x = "Firstname: " + a + "<br> Lastname: " + b;
		}

		return x;
	}

	public boolean addMembers(String x, String y, InputStream z) {

		try {

			String add = "INSERT INTO contacts (first_name, last_name, photo) values (?, ?, ?)";
			// Statement statement = conn.createStatement();
			// String sql = String.format(add);
			
			PreparedStatement statement = conn.prepareStatement(add);
			statement.setString(1,x);
			statement.setString(2,y);
			statement.setBlob(3,z);
			
			
			statement.executeUpdate(); 

		} catch (SQLException e) {
			//return false;
			e.printStackTrace();
		}
		return true;
	}
}
