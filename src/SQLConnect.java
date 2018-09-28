import java.sql.*;

public class SQLConnect {
	
	private Connection connection = null;

	public SQLConnect(String url, String username, String password) throws SQLException {
		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			// this.connect(url, username, password);
			connection = DriverManager.getConnection(url);
			String sql = "";
			PreparedStatement pre = null;
			sql += " SELECT table_name, column_name, data_type ";
			sql += " FROM INFORMATION_SCHEMA.COLUMNS ";
			sql += " WHERE table_name = ? ";
			try {
				pre = connection.prepareStatement(sql);
				pre.setString(1, "ECERT_REQUEST_FORM");
				ResultSet rs = pre.executeQuery();
				while(rs.next()) {
					System.out.println(rs.getString("DATA_TYPE") + " " + rs.getString("COLUMN_NAME"));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection connect(String url, String username, String password) throws SQLException {
		return setConnection(DriverManager.getConnection(url, username, password));
	}

	public Connection getConnection() {
		return connection;
	}

	public Connection setConnection(Connection connection) {
		this.connection = connection;
		return connection;
	}

}
