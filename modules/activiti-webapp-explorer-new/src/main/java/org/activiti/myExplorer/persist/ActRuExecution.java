package org.activiti.myExplorer.persist;

import java.io.Serializable;

import org.activiti.engine.runtime.Execution;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class ActRuExecution implements Serializable {

	public ActRuExecution() {

	}

	public ActRuExecution(Execution execution) {

		this.activityId = execution.getActivityId();

		this.description = execution.getDescription();

		this.ended = execution.isEnded();

		this.id = execution.getId();

		this.name = execution.getName();

		this.parentId = execution.getParentId();

		this.processInstanceId = execution.getProcessInstanceId();

		this.superExecutionId = execution.getSuperExecutionId();

		this.suspended = execution.isSuspended();

		this.tenantId = execution.getTenantId();

	}

	private static final long serialVersionUID = 1L;

	private String id;

	private Boolean suspended;

	private Boolean ended;

	private String activityId;

	private String processInstanceId;

	private String parentId;

	private String superExecutionId;

	private String tenantId;

	private String name;

	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getSuspended() {
		return suspended;
	}

	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}

	public Boolean getEnded() {
		return ended;
	}

	public void setEnded(Boolean ended) {
		this.ended = ended;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getSuperExecutionId() {
		return superExecutionId;
	}

	public void setSuperExecutionId(String superExecutionId) {
		this.superExecutionId = superExecutionId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
