package org.activiti.account.statics;

import java.util.HashMap;
import java.util.Map;

public enum RoleStatus {

	a("ADMIN", "管理员"), u("USER", "用户");

	private final String id;

	private final String text;

	private RoleStatus(String id, String text) {
		this.id = id;
		this.text = text;
	}

	public String text() {
		return text;
	}

	public String id() {
		return id;
	}

	private static final Map<String, RoleStatus> map = new HashMap<>(4);

	static {
		RoleStatus[] roleStatuses = RoleStatus.values();
		for (RoleStatus e : roleStatuses) {
			map.put(e.id(), e);
		}
	}

	public static RoleStatus forId(String id) {
		return map.get(id == null ? id : id.toUpperCase());
	}
}
