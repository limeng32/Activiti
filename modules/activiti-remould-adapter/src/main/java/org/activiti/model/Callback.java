package org.activiti.model;

public class Callback {

	public Callback() {

	}

	public Callback(boolean flag, String status, String message) {
		this.flag = flag;
		this.status = status;
		this.message = message;
	}

	public Callback(boolean flag, String status, String message, Object data) {
		this(flag, status, message);
		this.data = data;
	}

	public Callback(boolean flag) {
		this.flag = flag;
		if (flag) {
			this.status = "success";
			this.message = "您已操作成功";
		}
	}

	public Callback(boolean isSuccess, Object data) {
		this(isSuccess);
		this.data = data;
	}

	private boolean flag;

	private String status;

	private String message;

	private Object data;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}