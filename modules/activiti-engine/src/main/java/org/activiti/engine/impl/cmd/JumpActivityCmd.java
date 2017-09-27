package org.activiti.engine.impl.cmd;

import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;

public class JumpActivityCmd extends NeedsActiveTaskCmd<Void> {
	private static final long serialVersionUID = 1L;

	/**
	 * 目标任务的定义Id
	 */
	private String toActivityKey;
	/**
	 * 参数
	 */
	protected Map variables;

	public JumpActivityCmd(String _taskId, String _toTaskKey, Map _variables) {
		super(_taskId);
		this.toActivityKey = _toTaskKey;
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
		ActivityImpl toActivityImpl = processDefinitionImpl.findActivity(toActivityKey);

		if (toActivityImpl == null) {
			throw new ActivitiException(toActivityKey + " to ActivityImpl is null!");
		}
		task.fireEvent("complete");
		Context.getCommandContext().getTaskEntityManager().deleteTask(task, TaskEntity.JUMP_REASON_JUMP, false);
		execution.removeTask(task);// 执行规划的线
		execution.executeActivity(toActivityImpl);
		return null;
	}
}
