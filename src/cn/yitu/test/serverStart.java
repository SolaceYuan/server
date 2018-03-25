package cn.yitu.test;


import cn.yitu.db.dbConnect;
import cn.yitu.tcp.ServiceTcp;

public class serverStart {
	
	public serverStart() {
		
	}
	public static void main(String[] args) {
//		serverStart op=new serverStart();
//		op.openDataBase();
//		ServiceTcp serviceTcp = new ServiceTcp(8080);
//		serviceTcp.start();
		
	}
	
	public void openDataBase() {
		dbConnect openDb=new dbConnect();
		openDb.connectDB();
	}
}
