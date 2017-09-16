package org.activiti.myExplorer.model;

import java.io.Serializable;

import org.activiti.engine.runtime.Execution;

public class ExecutionReturn implements Serializable {

	public ExecutionReturn() {

	}

	public ExecutionReturn(Execution execution) {

		this.exeId = execution.getId();

		this.actId = execution.getActivityId();

	}

	private static final long serialVersionUID = 1L;

	private String exeId;

	private String actId;

	private String actName;

	private String[] actRole;

	private EndCode isEnd;

	public String getExeId() {
		return exeId;
	}

	public void setExeId(String exeId) {
		this.exeId = exeId;
	}

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String[] getActRole() {
		return actRole;
	}

	public void setActRole(String[] actRole) {
		this.actRole = actRole;
	}

	public EndCode getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(EndCode isEnd) {
		this.isEnd = isEnd;
	}

}
