package database_setting;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database_setting.jdbc.ConnectionProvider;
import database_setting.jdbc.LogUtil;

public class DBConnectionTest {
	static DataSource ds;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println();
		LogUtil.prnLog("Start DBConnectionTest");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println();
		LogUtil.prnLog("End DBConnectionTest");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println();
	}

	@Test
	public void testConnectionProvider() throws SQLException {
		try(Connection con = ConnectionProvider.getConnection("db.properties")){
			LogUtil.prnLog(con.toString());
			Assert.assertNotNull(con);
		}
	}
	
}
