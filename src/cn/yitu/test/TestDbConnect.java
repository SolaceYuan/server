package cn.yitu.test;

import static org.junit.Assert.*;

import java.sql.ResultSet;

import org.junit.Test;

import cn.yitu.db.dbConnect;

public class TestDbConnect {

	@Test
	public void testInitDB() {
		dbConnect db=new dbConnect();
		db.connectDB();
		int rs=db.executeUpdate("insert into user(userId,pwd,userName) values('002','123456','aoteman')");
		if(rs!=0) {
			System.out.println("数据库操作成功"+rs+"条");
		}
		
	}

}
