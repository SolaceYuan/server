package cn.yitu.tcp.customer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.List;

import cn.yitu.db.dbAction;
import cn.yitu.entity.ErrorCode;
import cn.yitu.entity.User;
import cn.yitu.serverUI.ServerManager;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
/**
 * 接收客户端的信息并判断，数据处理交给dbAction，返回处理后的数据并发送到客户端
 * @author Solace
 *
 */
public class Client implements Runnable {
	private final String encoder = "UTF-8";

	private User user;
	private Socket socket;
	private List<User> users = null;

	public Client(Socket socket, List<User> users) {
		this.socket = socket;
		this.users = users;
	}

	private InputStream inputStream = null;
	private OutputStream outputStream = null;

	private final String end = "end";

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream() {
		try {
			this.outputStream = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			inputStream = socket.getInputStream();
			setOutputStream();
			outputStream = getOutputStream();

			User user = new User(socket, this);
//			for (int i = 1; i <= users.size(); i++) {
//				System.out.println("11111111111111111" + users.get(i).getPhone() + user.getPhone());
//				if (users.get(i).getPhone() == user.getPhone()) {
//					System.out.println("11111111111111111" + users.get(i).getPhone() + user.getPhone());
//				} else {
//					users.add(user);
//				}
//			}
			users.add(user);

			// 启动消息发送类
			Sender sender = new Sender(user, users);
			Thread thread = new Thread(sender);
			thread.start();

			byte[] bytes = new byte[1024];
			Integer length = null;
			String inString = null;
			while ((length = inputStream.read(bytes)) != -1) {
				inString = new String(bytes, 0, length, encoder);
				try {
					// if (user.isOnLine() == false) {
					// this.close();
					// break;
					// }
					// if (end.equals(inString)) {
					// this.close();
					// break;
					// }
					JSONObject reJson = JSONObject.fromObject(inString);
					int code = reJson.getInt("code");
					if (code == 001) {// 注册操作
						dbAction register = new dbAction();
						register.initDbAction();
						JSONObject isSucces = register.register(reJson);
						this.send(isSucces.toString());
						this.close();
						break;
					}
					if (code == 002) {// 登录操作

						dbAction login = new dbAction();
						login.initDbAction();
						JSONObject isLogin = login.login(reJson);
						this.send(isLogin.toString());
						this.close();
						break;
					}
					if (code == 003) {
						// 修改昵称
						dbAction saveInfo = new dbAction();
						saveInfo.initDbAction();
						JSONObject isOk = saveInfo.saveName(reJson);
						this.send(isOk.toString());
						this.close();
						break;
					}
					if (code == 004) {
						// 查询好友
						dbAction myFriends = new dbAction();
						myFriends.initDbAction();
						JSONObject jsonObject = myFriends.myFriends(reJson);
						this.send(jsonObject.toString());
						this.close();
						break;
					}
					if (code == 005) {
						// 查询消息
						dbAction messages = new dbAction();
						messages.initDbAction();
						JSONObject jsonObject = messages.getMessage(reJson);
						System.out.println(jsonObject.toString());
						this.send(jsonObject.toString());
						this.close();
						break;
					}
					if (code == 007) {
						// 搜索ID
						String id = reJson.getString("userId");
						JSONObject jsonObject = new JSONObject();
						dbAction myFriends = new dbAction();
						myFriends.initDbAction();
						boolean isExit = myFriends.isExit(id);
						if (isExit) {
							jsonObject.put("isOk", 104);
						} else {
							jsonObject.put("isOk", 103);
						}
						this.send(jsonObject.toString());
						this.close();
						break;
					}
					if (code == 006) {
						// 申请添加好友
						dbAction add = new dbAction();
						add.initDbAction();
						JSONObject jsonObject = add.addFriends(reJson);
						System.out.println(jsonObject.toString());
						this.send(jsonObject.toString());
						this.close();
						break;
					}
					if (code == 108) {
						// 是否同意好友申请
						dbAction isAgree = new dbAction();
						isAgree.initDbAction();
						JSONObject jsonObject = isAgree.isAgree(reJson);
						System.out.println(jsonObject.toString());
						this.send(jsonObject.toString());
						this.close();
						break;
					}
					if (code == 111) {// 共享位置操作
						System.out.println("接收到信息了 " + reJson.toString());
						Double lat = reJson.getDouble("lat");
						Double lng = reJson.getDouble("lng");
						String userName = reJson.getString("userName");
						String phone = reJson.getString("phone");
						user.setUserName(userName);
						user.setPhone(phone);
						user.setLat(lat);
						user.setLng(lng);
					}
					if (code == 444) {// 停止共享
						users.remove(user);
						this.close();
						break;
					}
				} catch (JSONException e) {
					JSONObject jso = (ErrorCode.e_201).toJson();
					this.send(jso.toString());
				} catch (Exception e) {
					user.setOnLine(false);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void close() {
		try {
			users.remove(user);
			inputStream.close();
			outputStream.close();
			socket.close();
			System.out.println("socket已关闭");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void openOS() {
		this.setOutputStream();
		System.out.println("开启流");
	}

	public void closeOS() {
		try {
			System.out.println(socket.isOutputShutdown());
			socket.shutdownOutput();
			System.out.println("关闭流");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(String data) throws UnsupportedEncodingException, IOException {
		outputStream.write(data.getBytes(encoder));
		outputStream.flush();
		// outputStream.close();
		ServerManager.textArea.append("data:" + data + "已发送。。。\r\n");
		System.out.println("data:" + data);
	}

}
