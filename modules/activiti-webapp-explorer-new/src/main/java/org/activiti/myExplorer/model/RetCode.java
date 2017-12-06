package org.activiti.myExplorer.model;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public enum RetCode {

	ERROR("0"), SUCCESS("1"), EXCEPTION("2");

	private final String code;

	private RetCode(String code) {
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
