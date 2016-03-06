package com.vista;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBhelper
{
	String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // 加载JDBC驱动
	String dbURL = "jdbc:sqlserver://localhost:1433; DatabaseName=test"; // 连接服务器和数据库test
	String userName = "sa"; // 默认用户名
	String userPwd = "8j8jkl"; // 密码
	Connection dbConn;
	PreparedStatement pst = null;

	public void Connection()
	{
		try
		{
			Class.forName(driverName);
			dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
			System.out.println("Connection Successful!");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void insert(CrawlResult result)
	{

		String sql = "insert into Crawl values(?,?,?,?,?) ";

		try
		{
			pst = dbConn.prepareStatement(sql);

			pst.setString(1, result.getTitle());
			pst.setString(2, result.getUrl());
			pst.setString(3, result.getCategory());
			pst.setString(4, result.getProbility() + "");
			pst.setString(5, result.getContent());
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close()
	{
		try
		{
			dbConn.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
