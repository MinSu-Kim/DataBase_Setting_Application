package database_setting.service;

import java.util.List;

public abstract class AbstractService {
	
	protected String getFilePath(String tableName, String dirPath) {
		String importPath = System.getProperty("user.dir")+ "\\"+dirPath+"\\";
		return String.format("%s%s.txt", importPath, tableName).replace("\\", "/");
	}
	
	protected abstract void service(String...propFile);
	
	protected abstract List<String> getTables();
}
