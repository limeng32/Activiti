package org.activiti.myExplorer.model;

public enum RetCode {

	error("0"), success("1"), exception("2");

	private final String code;

	private RetCode(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

	public String toString() {
		return code;
	}
}
