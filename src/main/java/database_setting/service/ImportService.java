package database_setting.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import database_setting.jdbc.LogUtil;
import database_setting.jdbc.MySQLJdbcUtil;

public class ImportService {
	
	public void service(String propFile) {	
		try (Connection con = MySQLJdbcUtil.getConnection(propFile);
				Statement stmt = con.createStatement()) {
			stmt.addBatch("SET FOREIGN_KEY_CHECKS = 0");
			List<String> tables = Arrays.asList("product", "sale", "saledetail", "salefull");
			String sql = null;
			for (String tableName : tables) {
				sql = String.format("LOAD DATA LOCAL INFILE '%s' IGNORE INTO TABLE %s character set 'UTF8' fields TERMINATED by ','",getFilePath(tableName), tableName);
				stmt.addBatch(sql);
				LogUtil.prnLog(sql);
			}
			stmt.addBatch("SET FOREIGN_KEY_CHECKS = 1");
			int[] res = stmt.executeBatch();
			LogUtil.prnLog(Arrays.toString(res));
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	private String getFilePath(String tableName) {
		String importPath = System.getProperty("user.dir")+ "\\DataFiles\\";
		return String.format("%s%s.txt", importPath, tableName).replace("\\", "/");
	}
	
}
