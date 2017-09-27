package org.activiti.engine.impl.cmd;

import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;

public class WithdrawTaskCmd extends NeedsActiveTaskCmd<Void> {
	private static final long serialVersionUID = 1L;

	/**
	 * 目标任务的Id
	 */
	private String toTaskId;
	/**
	 * 参数
	 */
	protected Map variables;

	public WithdrawTaskCmd(String _taskId, String _toTaskId, Map _variables) {
		super(_taskId);
		this.toTaskId = _toTaskId;
		this.variables = _variables;
	}

	@Override
	protected Void execute(CommandContext commandContext, TaskEntity task) {

		if (variables != null) {
			task.setExecutionVariables(variables);
		}
		ExecutionEntity execution = task.getExecution();
		// 流程定义id
		String procDefId = execution.getProcessDefinitionId();
		// 获取服务
		RepositoryServiceImpl repositoryService = (RepositoryServiceImpl) execution.getEngineServices()
				.getRepositoryService();
		// 获取流程定义的所有节点
		ProcessDefinitionImpl processDefinitionImpl = (ProcessDefinitionImpl) repositoryService
				.getDeployedProcessDefinition(procDefId);
		// 获取需要提交的节点

		/* 从ACT_HI_TASKINST中找到以toTaskKey为主键的taskInst */
		HistoricTaskInstance historicTaskInstance = execution.getEngineServices().getHistoryService()
				.createHistoricTaskInstanceQuery().taskId(toTaskId).singleResult();
		if (historicTaskInstance == null) {
			throw new ActivitiException("Task " + toTaskId + " is null!");
		}
		ActivityImpl toActivityImpl = processDefinitionImpl.findActivity(historicTaskInstance.getTaskDefinitionKey());
		task.fireEvent("complete");
		Context.getCommandContext().getTaskEntityManager().deleteTask(task, TaskEntity.JUMP_REASON_TURNBACK, false);
		execution.removeTask(task);// 执行规划的线
		execution.executeActivity(toActivityImpl);
		return null;
	}
}
