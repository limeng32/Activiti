package org.activiti.account.exception;

public class ActivitiAccountException extends Exception {

	private static final long serialVersionUID = 1L;

	public ActivitiAccountException(String message) {
		super(message);
	}

	public ActivitiAccountException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
