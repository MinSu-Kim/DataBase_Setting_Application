package database_setting.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import database_setting.jdbc.MySQLJdbcUtil;

public class ExportService extends AbstractService{

	@Override
	public void service(String...propFile) {
		checkBackupDir();
		List<String> tables = getTables();
		for(String tblName : tables) {
			exportData(propFile[0], "select * from "+ tblName, getFilePath(tblName,"BackupFiles"));
		}		
	}

	@Override
	public List<String> getTables() {
		return Arrays.asList("product","sale","saledetail", "salefull");
	}

	private void exportData(String propFile, String sql, String exportPath){
		StringBuilder sb = new StringBuilder();
		try(Connection con = MySQLJdbcUtil.getConnection(propFile);	Statement stmt = con.createStatement();	ResultSet rs = stmt.executeQuery(sql)){
			int colCnt = rs.getMetaData().getColumnCount();// 컬럼의 개수
			while (rs.next()) {
				for (int i = 1; i <= colCnt; i++) {
					sb.append(rs.getObject(i) + ",");
				}
				sb.replace(sb.length() - 1, sb.length(), ""); // 마지막 라인의 comma 제거
				sb.append("\n");
			}
			backupFileWrite(sb.toString(), exportPath);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void checkBackupDir() {
		File backupDir=new File(System.getProperty("user.dir")+ "\\BackupFiles\\");
		
		if(backupDir.exists()) {
			for(File file : backupDir.listFiles()) {
				file.delete();
				System.out.printf("%s Delete Success! %n", file.getName());
			}
		} else {
			backupDir.mkdir();
			System.out.printf("%s make dir Success! %n", System.getProperty("user.dir")+ "\\BackupFiles\\");
		}
	}
	
	private void backupFileWrite(String str, String exportPath) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		try(OutputStreamWriter dos = new OutputStreamWriter(new FileOutputStream(exportPath),"UTF-8")){
			dos.write(str);
		} 	
	}

}
