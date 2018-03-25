package cn.yitu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.yitu.db.dbConnect;
import cn.yitu.entity.ErrorCode;
import cn.yitu.entity.RamName;
import cn.yitu.serverUI.ServerManager;
import net.sf.json.JSONObject;

public class dbAction {
	String sql = null;
	dbConnect db;
	int isOnly = 1;
	private boolean isExit;

	/**
	 * 初始化数据库连接
	 */
	public void initDbAction() {
		this.db = new dbConnect();
		this.db.connectDB();
	}

	/**
	 * 注册信息输入到数据库
	 * 
	 * @param register
	 * @return
	 */
	public JSONObject register(JSONObject register) {
		String id = register.getString("id");
		int exit = this.isOnly(id);
		String userName = RamName.getRamName();
		JSONObject userInfo = new JSONObject();
		if (exit == 0) {// id唯一
			String pw = register.getString("pw");
			// System.out.println("注册信息正在注入数据库。。。id=" + id + ",pw=" + pw);
			//ServerManager.textArea.append("注册信息正在注入数据库。。。id=" + id + ",pw=" + pw + "username=" + userName + "\r\n");
			sql = "insert into user(userId,pwd,userName) values('" + id + "','" + pw + "','" + userName + "')";
			int rs = db.executeUpdate(sql);

			// db.closeDB();
			if (rs != 0) {
				userInfo.put("isOk", 101);
				userInfo.put("userName", userName);
				return userInfo;// 注册成功
			} else {
				userInfo.put("isOk", 102);
				return userInfo;// 注册失败
			}
		} else {
			userInfo.put("isOk", 103);
			return userInfo;// id已存在
		}

	}

	public int isOnly(String id) {
		sql = "SELECT * FROM USER WHERE userId='" + id + "'";
		ResultSet rs = db.executeQuery(sql);
		try {
			rs.last();
			isOnly = rs.getRow();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("rows:" + isOnly);
		//ServerManager.textArea.append("rows:" + isOnly);
		return isOnly;
	}

	// 登录
	public JSONObject login(JSONObject loginInfo) {
		//ServerManager.textArea.append("002登录操作\r\n");
		String id = loginInfo.getString("id");
		String pw = loginInfo.getString("pw");
		String userName = null;
		// int rows=0;
		JSONObject isLogin = new JSONObject();
		String idSql = "SELECT * FROM USER WHERE userId='" + id + "'";
		String sql = "SELECT * FROM USER WHERE userId='" + id + "' AND pwd='" + pw + "'";
		ResultSet idRs = db.executeQuery(idSql);
		//ServerManager.textArea.append("数据已查询\r\n");

		try {
			idRs.last();
			if (idRs.getRow() == 1) {
				ResultSet rs = db.executeQuery(sql);
				rs.last();
				if (rs.getRow() == 1) {
					userName = rs.getString("userName");
					System.out.println("名为：" + userName + "正在进行登录");
					//ServerManager.textArea.append("名为：" + userName + "正在进行登录\r\n");
					isLogin.put("isOk", 1);// 用户名和密码均正确
					isLogin.put("userName", userName);

				} else {
					isLogin.put("isOk", 100);// 密码错误
					//ServerManager.textArea.append("密码错误\r\n");
					// System.out.println("密码错误");
				}
			} else {
				isLogin.put("isOk", 2);// 用户名不存在
				//ServerManager.textArea.append("用户名不存在\r\n");

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return isLogin;
	}

	public JSONObject saveName(JSONObject jsonObject) {
		ServerManager.textArea.append("003修改昵称\r\n");
		String id = jsonObject.getString("userId");
		String name = jsonObject.getString("userName");
		String sql = "UPDATE USER SET userName='" + name + "' WHERE userId='" + id + "'";
		JSONObject saveCode = new JSONObject();
		try {
			int rs = db.executeUpdate(sql);
			if (rs == 1) {
				saveCode.put("isOk", true);
			} else {
				saveCode.put("isOk", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			saveCode.put("isOk", false);
		}

		return saveCode;
	}

	// 查询好友
	public JSONObject myFriends(JSONObject jsonObject) {

		String userId = jsonObject.getString("id");
		String sql = "SELECT friends FROM USER WHERE userId='" + userId + "'";
		ResultSet rs = db.executeQuery(sql);
		JSONObject friends = new JSONObject();
		friends.put("friends", "");
		try {
			while (rs.next()) {
				//ServerManager.textArea.append("好友：" + rs.getString(1) + "\r\n");
				friends.put("friends", rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			friends.put("friends", "");
		}
		return friends;
	}

	// 查询消息
	public JSONObject getMessage(JSONObject jsonObject) {
		List<String> messageList = new ArrayList<String>();
		String recieve = jsonObject.getString("recieve");
		String sql = "select send from message where recieve='" + recieve + "' and isRead='false'";
		ResultSet rs = db.executeQuery(sql);
		JSONObject messages = new JSONObject();
		try {
			while (rs.next()) {
				//ServerManager.textArea.append("消息：" + rs.getString(1) + "\r\n");
				messageList.add(rs.getString(1));
			}
			messages.put("code", 101);
			messages.put("sends", messageList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		// String user[] = (String[])messageList.toArray(new
		// String[messageList.size()]);
		return messages;
	}

	// 好友申请
	public JSONObject addFriends(JSONObject jsonObject) {
		String recieveId = jsonObject.getString("recieveId");
		String sendId = jsonObject.getString("sendId");
		JSONObject isSu = new JSONObject();
		if (isExit(recieveId)) {// 存在则添加
			String sql = "insert into message(send,recieve,isRead) values('" + sendId + "','" + recieveId
					+ "','false')";
			int rs = db.executeUpdate(sql);
			if (rs != 0) {
				isSu.put("isOk", 101);// 申请好友成功
			} else {
				isSu.put("isOk", 102);// 申请失败
			}
		} else {
			isSu.put("isOk", 103);// 查无此人
		}
		return isSu;
	}

	// 查询是否存在此ID
	public boolean isExit(String id) {
		String sql = "SELECT * FROM USER WHERE userId='" + id + "'";
		ResultSet rs = db.executeQuery(sql);
		isExit = false;
		try {
			if (rs.next()) {
				isExit = true;
			} else {
				isExit = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isExit;
	}

	// 处理好友申请
	public JSONObject isAgree(JSONObject jsonObject) {
		boolean isA = jsonObject.getBoolean("isAgree");
		String reId = jsonObject.getString("recieveId");
		String seId = jsonObject.getString("sendId");
		JSONObject isOk = new JSONObject();
		isOk.put("code", 102);
		if (isA) {// 同意申请
			String sql = "SELECT friends FROM USER WHERE userId='" + reId + "'";
			ResultSet rs = db.executeQuery(sql);

			try {
				if (rs.next()) {
					String friends = rs.getString(1);
					System.out.println("friends:" + friends);
					if (friends == null || friends.length() <= 0) {
						String update = "UPDATE USER SET friends='" + seId + "' WHERE userId='" + reId + "'";

						try {
							int uRs = db.executeUpdate(update);
							if (uRs == 1) {
								isOk.put("isOk", true);
							} else {
								isOk.put("isOk", false);
							}
						} catch (Exception e) {
							e.printStackTrace();
							isOk.put("isOk", false);
						}
					} else {
						friends = friends + "," + seId;
						String update = "UPDATE USER SET friends='" + friends + "' WHERE userId='" + reId + "'";
						try {
							int uRs = db.executeUpdate(update);
							if (uRs == 1) {
								isOk.put("isOk", true);
							} else {
								isOk.put("isOk", false);
							}
						} catch (Exception e) {
							e.printStackTrace();
							isOk.put("isOk", false);
						}

					}

				}
			} catch (Exception e) {

			}
			// 申请方
			String sql2 = "SELECT friends FROM USER WHERE userId='" + seId + "'";
			ResultSet rs2 = db.executeQuery(sql2);
			try {
				if (rs2.next()) {
					String friends2 = rs2.getString(1);
					System.out.println("friends2:" + friends2);
					if (friends2 == null || friends2.length() <= 0) {
						String update2 = "UPDATE USER SET friends='" + reId + "' WHERE userId='" + seId + "'";

						try {
							db.executeUpdate(update2);
						} catch (Exception e) {
							e.printStackTrace();
							isOk.put("isOk", false);
						}
					} else {
						friends2 = friends2 + "," + reId;
						String update2 = "UPDATE USER SET friends='" + friends2 + "' WHERE userId='" + seId + "'";
						try {
							db.executeUpdate(update2);

						} catch (Exception e) {
							e.printStackTrace();
							isOk.put("isOk", false);
						}

					}

				}
			} catch (Exception e) {

			}
		}
		// 同意与否标记消息已读
		String isReadSql = "UPDATE message SET isRead='true' WHERE recieve='" + reId + "'";
		try {
			db.executeUpdate(isReadSql);
		} catch (Exception e) {
			e.printStackTrace();
			isOk.put("isOk", false);
		}

		return isOk;
	}

}
