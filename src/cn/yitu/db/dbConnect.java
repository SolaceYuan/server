package cn.yitu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import cn.yitu.serverUI.ServerManager;

public class dbConnect {
	
	public static final String DRIVER="com.mysql.jdbc.Driver";
	public static final String USER="root";
	public static final String PW="";
	public static final String URL = "jdbc:mysql://localhost:3306/test";
	
	public static dbConnect per=null;
	public Connection conn=null;
	public Statement stmt=null;
	
	public dbConnect() {
		
	}

	//初始化数据库连接
	public void initDB(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static dbConnect createInstance() {
		if(per==null) {
			per=new dbConnect();
			per.initDB();
		}
		return per;
	}
	
	public void connectDB() {
		//ServerManager.textArea.append("正在连接数据库。。。\r\n");
		try {
			conn=DriverManager.getConnection(URL,USER,PW);
			stmt=conn.createStatement();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//ServerManager.textArea.append("已成功连接数据库。。。\r\n");
		//System.out.println("已成功连接数据库。。。");
	}
	/**
	 * 关闭数据库连接
	 */
	public void closeDB() {
		//ServerManager.textArea.append("正在关闭数据库连接。。。\r\n");
		//System.out.println("正在关闭数据库连接。。。");
		try {
			stmt.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//ServerManager.textArea.append("已成功关闭数据库连接。。。\r\n");
	}
	/**
	 * 数据库查询操作
	 * @param sql
	 * @return
	 */
	public ResultSet executeQuery(String sql) {
		ResultSet rs=null;
		try {
			rs=stmt.executeQuery(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return rs;
		
	}
	/**
	 * 执行数据库修改操作
	 * @param sql
	 * @return 返回成功执行条数
	 */
	public int executeUpdate(String sql) {
		int ret=0;
		try {
			ret=stmt.executeUpdate(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ret;
	}
	
}




















