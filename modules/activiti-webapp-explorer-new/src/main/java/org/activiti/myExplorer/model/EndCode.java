package org.activiti.myExplorer.model;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public enum EndCode {

	NO("0"), YES("1");

	private final String code;

	private EndCode(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

	@Override
	public String toString() {
		return code;
	}
}
