package cn.yitu.tcp.customer;

import java.util.List;

import cn.yitu.entity.ErrorCode;
import cn.yitu.entity.User;
import cn.yitu.serverUI.ServerManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Sender implements Runnable {
	private User user;
	private List<User> users = null;

	public Sender(User user, List<User> users) {
		this.user = user;
		this.users = users;
	}

	@Override
	public void run() {
		while (user.isOnLine()) {
			try {
				Thread.sleep(3 * 1000);// 3秒一次
				user.getClient().send(ret().toString()+"\n");
			} catch (Exception e) {
				user.setOnLine(false);
			}
		}
	}
	/**
	 * 打包共享数据
	 * @return jsonobject数据
	 */
	private JSONObject ret() {
		JSONObject jsonObject = null;
		JSONArray array = new JSONArray();
		for (User user : users) {
			if (user.getLat() != null && user.getLng() != null) {
				if (user.getUuid() != this.user.getUuid()) {
					JSONObject us = new JSONObject();
					us.put("uuid", user.getUuid());
					us.put("userName", user.getUserName());
					us.put("phone", user.getPhone());
					us.put("lat", user.getLat());
					us.put("lng", user.getLng());
					//us.put("end", "\n");
					array.add(us);
					//发送除自己以外的数据
					//ServerManager.textArea.append("共享位置中：用户id："+user.getUuid()+
							//",lat:"+user.getLat()+",lng:"+user.getLng()+"\r\n");
					
				}
			}
		}
		
		JSONObject serverLocation=new JSONObject();//113.685583,23.641187
		serverLocation.put("uuid", "250f2fca-5868-4579-b1cf-66546218cb79");
		serverLocation.put("userName", "solace0");
		serverLocation.put("phone", "18819461132");
		serverLocation.put("lat", 23.629669867621526);
		serverLocation.put("lng", 113.67667290581598);
		array.add(serverLocation);
		jsonObject = ErrorCode.s_200.toJson(array);
		//jsonObject.put("end", "\n");
		return jsonObject;
	}

}
