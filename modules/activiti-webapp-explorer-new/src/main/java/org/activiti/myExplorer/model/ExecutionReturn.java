package org.activiti.myExplorer.model;

import java.io.Serializable;

import org.activiti.engine.runtime.Execution;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
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

	private String taskId;

	private String document;

	@JSONField(serialize = false)
	private boolean isSoftEnd;

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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public boolean isSoftEnd() {
		return isSoftEnd;
	}

	public void setSoftEnd(boolean isSoftEnd) {
		this.isSoftEnd = isSoftEnd;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

}
