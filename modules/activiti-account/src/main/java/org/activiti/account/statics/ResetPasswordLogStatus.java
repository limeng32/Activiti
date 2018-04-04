package org.activiti.account.statics;

import java.util.HashMap;
import java.util.Map;

public enum ResetPasswordLogStatus {

	i("INSERT", "新增"), u("UPDATE", "更新");

	private final String id;

	private final String text;

	private ResetPasswordLogStatus(String id, String text) {
		this.id = id;
		this.text = text;
	}

	public String text() {
		return text;
	}

	public String id() {
		return id;
	}

	private static final Map<String, ResetPasswordLogStatus> map = new HashMap<>(4);

	static {
		ResetPasswordLogStatus[] enums = ResetPasswordLogStatus.values();
		for (ResetPasswordLogStatus e : enums) {
			map.put(e.id(), e);
		}
	}

	public static ResetPasswordLogStatus forId(String id) {
		return map.get(id == null ? id : id.toUpperCase());
	}

}
