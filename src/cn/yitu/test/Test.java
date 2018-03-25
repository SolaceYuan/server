package cn.yitu.test;

import cn.yitu.db.dbAction;
import cn.yitu.tcp.ServiceTcp;
import net.sf.json.JSONObject;

public class Test {
	//开启服务器和数据库
	public static void main(String[] args) {
//		ServiceTcp serviceTcp = new ServiceTcp(8080);
//		serviceTcp.start();
		dbAction openDb=new dbAction();
		openDb.initDbAction();
		JSONObject JSonObject = null;
		JSonObject.put("userId","18819461131");
		openDb.myFriends(JSonObject);
	}
} 