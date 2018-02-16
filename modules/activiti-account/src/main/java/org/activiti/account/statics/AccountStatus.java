package org.activiti.account.statics;

import java.util.HashMap;
import java.util.Map;

public enum AccountStatus {

	s("SLEEP", "休眠"), a("AWAKE", "活跃");

	private final String id;

	private final String text;

	private AccountStatus(String id, String text) {
		this.id = id;
		this.text = text;
	}

	public String text() {
		return text;
	}

	public String id() {
		return id;
	}

	private static final Map<String, AccountStatus> map = new HashMap<>(4);

	static {
		AccountStatus[] accountStatuses = AccountStatus.values();
		for (AccountStatus e : accountStatuses) {
			map.put(e.id(), e);
		}
	}

	public static AccountStatus forId(String id) {
		return map.get(id == null ? id : id.toUpperCase());
	}
}
