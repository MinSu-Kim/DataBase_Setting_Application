package database_setting.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database_setting.jdbc.ConnectionProvider;
import database_setting.jdbc.MyDataSource;

public class ExportService {

	public void service(String dirPath) {
		checkBackupDir(dirPath);
		List<String> tables = getTables();
		File sqlDir = new File(dirPath);
		File sqlFile = null;
		for(String tblName : tables) {
			sqlFile = new File(sqlDir, tblName);
			exportData("select * from "+ tblName, sqlFile.getAbsolutePath().replace("\\", "/"));
		}		
	}

	private List<String> getTables() {
		List<String> list = new ArrayList<>();
		try (Connection con = ConnectionProvider.getConnection("db.properties");
				Statement stmt = con.createStatement()){
			stmt.execute("use " + MyDataSource.getInstance("db.properties").getProperties().getProperty("dbname"));
			try(ResultSet rs = stmt.executeQuery("show tables");){
				while(rs.next()) {
					list.add(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	private void exportData(String sql, String exportPath){
		StringBuilder sb = new StringBuilder();
		try(Connection con = ConnectionProvider.getConnection("db.properties");
				Statement stmt = con.createStatement();	){
			stmt.execute("use " + MyDataSource.getInstance("db.properties").getProperties().getProperty("dbname"));

			try(ResultSet rs = stmt.executeQuery(sql)){
				int colCnt = rs.getMetaData().getColumnCount();// 컬럼의 개수
				while (rs.next()) {
					for (int i = 1; i <= colCnt; i++) {
						sb.append(rs.getObject(i) + ",");
					}
					sb.replace(sb.length() - 1, sb.length(), ""); // 마지막 라인의 comma 제거
					sb.append("\n");
				}
				backupFileWrite(sb.toString(), exportPath);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void checkBackupDir(String dirPath) {
		File backupDir=new File(dirPath);
		
		if(backupDir.exists()) {
			for(File file : backupDir.listFiles()) {
				file.delete();
				System.out.printf("%s Delete Success! %n", file.getName());
			}
		} else {
			backupDir.mkdir();
			System.out.printf("%s make dir Success! %n", dirPath);
		}
	}
	
	private void backupFileWrite(String str, String exportPath) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		try(OutputStreamWriter dos = new OutputStreamWriter(new FileOutputStream(exportPath),"UTF-8")){
			dos.write(str);
		} 	
	}

}
