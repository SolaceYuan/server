package cn.yitu.entity;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

//错误代码
public enum ErrorCode {
	s_200(200, "成功"),
	e_201(201, "JSON格式错误"),
	e_202(202,"手机号已注册");

	private int errorCode;
	private String message;

	public int getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	private ErrorCode(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public JSONObject toJson(JSON data) {
		JSONObject retJson = new JSONObject();
		retJson.put("errorCode", errorCode);
		retJson.put("message", message);
		if (data != null) {
			retJson.put("data", data);
		}
		return retJson;
	}

	public JSONObject toJson() {
		return this.toJson(null);
	}
}
