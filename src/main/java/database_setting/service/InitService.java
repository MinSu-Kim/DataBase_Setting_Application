package database_setting.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import database_setting.jdbc.ConnectionProvider;
import database_setting.jdbc.LogUtil;

public class InitService extends AbstractService{
	
	@Override
	public void service(String...propFile) {	
		createTableOrProcedureAndTrigger(propFile[0], "create_database.txt", false);
		createTableOrProcedureAndTrigger(propFile[0], "create_user_sql.txt", false);
		createTableOrProcedureAndTrigger(propFile[1], "create_table.txt", false);
		createTableOrProcedureAndTrigger(propFile[1], "create_procedure_trigger.txt", true);
	}
	
	private void createTableOrProcedureAndTrigger(String propFile, String execSqlFile, boolean isProcedure) {
		LogUtil.prnLog("createTableOrProcedureAndTrigger()");
		try (InputStream is = ClassLoader.getSystemResourceAsStream("sql/" + execSqlFile);
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));) {
			StringBuilder statement = new StringBuilder();
			
			for (String line; (line = br.readLine()) != null;) {
				if (line.startsWith("--"))
					continue;
				if (line.contains("--")) {
					statement.append(line.substring(0, line.lastIndexOf("-- "))+ "\r\n");
				} else {
					statement.append(line + "\r\n");
				}

				if (isProcedure) {
					if (line.endsWith("END;")||line.endsWith("end;")) {
						executeSQL(propFile, statement.toString());
						statement.setLength(0);
					}
				}else {
					if (line.endsWith(";")) {
						executeSQL(propFile,statement.toString());
						statement.setLength(0);
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void executeSQL(String propFile, String sql) {
		try (Connection con = ConnectionProvider.getConnection(propFile);
				PreparedStatement pstmt = con.prepareStatement(sql)) {
			LogUtil.prnLog(pstmt);
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected List<String> getTables() {
		throw new UnsupportedOperationException();
	}

}