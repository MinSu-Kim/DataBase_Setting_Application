package database_setting.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLJdbcUtil {

	private static Connection conn;

	public static Connection getConnection(String propPath) throws SQLException {
		try (InputStream is = ClassLoader.getSystemResourceAsStream(propPath)) {
			Properties properties = new Properties();
			properties.load(is);
			conn = DriverManager.getConnection(properties.getProperty("url"), properties);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public static void close() {
		if (conn!=null) {
			try {
				conn.close();
				conn=null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
