package database_setting.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import database_setting.jdbc.ConnectionProvider;
import database_setting.jdbc.LogUtil;

public class ImportService extends AbstractService{
	
	@Override
	public void service(String...propFile) {	
		try (Connection con = ConnectionProvider.getConnection(propFile[0]);
				Statement stmt = con.createStatement()) {
			stmt.addBatch("SET FOREIGN_KEY_CHECKS = 0");
			List<String> tables = getTables();
			String sql = null;
			for (String tableName : tables) {
				sql = String.format("LOAD DATA LOCAL INFILE '%s' IGNORE INTO TABLE %s character set 'UTF8' fields TERMINATED by ','",getFilePath(tableName, "DataFiles"), tableName);
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

	@Override
	public List<String> getTables() {
		return Arrays.asList("product", "sale", "salefull");
	}
	
}
