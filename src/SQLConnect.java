import java.sql.*;

public class SQLConnect {
	
	private Connection connection = null;

	public SQLConnect(String url, String username, String password) throws SQLException {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			this.connect(url, username, password);
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
