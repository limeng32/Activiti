package org.activiti.myExplorer.model;

public enum EndCode {

	no("0"), yes("1");

	private final String code;

	private EndCode(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

	public String toString() {
		return code;
	}
}
