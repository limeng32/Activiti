package org.activiti.myExplorer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.myExplorer.model.EndCode;
import org.activiti.myExplorer.model.ExecutionReturn;
import org.activiti.myExplorer.model.ProcessInstReturn;
import org.activiti.myExplorer.model.RetCode;
import org.activiti.myExplorer.persist.ActReProcdef;
import org.activiti.myExplorer.persist.MyBusinessModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
@Service
public class CommonService {

	@Autowired
	private ActReProcdefService actReProcdefService;

	@Autowired
	private MyBusinessModelService myBusinessModelService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	public static final String END = "end";

	public Deployment getDeployment(String businessId) {
		MyBusinessModel mbm = new MyBusinessModel();
		mbm.setBusinessId(businessId);
		MyBusinessModel myBusinessModel = myBusinessModelService.selectOne(mbm);
		if (myBusinessModel != null && myBusinessModel.getActReModel() != null) {
			List<Deployment> deploymentC = repositoryService.createDeploymentQuery()
					.deploymentName(myBusinessModel.getActReModel().getName()).orderByDeploymentId().desc()
					.listPage(0, 1);
			if (!deploymentC.isEmpty()) {
				return deploymentC.get(0);
			}
		}
		return null;
	}

	public ActReProcdef getActReProcdef(String businessId) {
		Deployment deployment = getDeployment(businessId);
		if (deployment != null && deployment.getId() != null) {
			ActReProcdef arp = new ActReProcdef();
			arp.setDeploymentId(deployment.getId());
			return actReProcdefService.selectOne(arp);
		}
		return null;
	}

	public String[] getActRole(Collection<IdentityLink> identityLinkC) {
		Collection<String> collection = getActRoleAsMap(identityLinkC);
		String[] ret = new String[collection.size()];
		return collection.toArray(ret);
	}

	public Set<String> getActRoleAsMap(Collection<IdentityLink> identityLinkC) {
		Set<String> collection = new LinkedHashSet<>();
		for (IdentityLink identityLink : identityLinkC) {
			if (identityLink.getGroupId() != null) {
				collection.add(identityLink.getGroupId());
			}
		}
		return collection;
	}

	public ProcessInstReturn justStart(String businessId, String dealRole, String dealPerson, String dataStr) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		JSONObject jsonObj = JSON.parseObject(dataStr);
		JSONObject formData = null;
		if (jsonObj != null) {
			formData = jsonObj.getJSONObject("form_data");
		}
		ProcessInstance pi = justStartService(businessId, dealRole, dealPerson, dataStr);
		if (pi != null) {
			loadProcessInstReturn(processInstReturn, pi, formData);
		} else {
			processInstReturn.setIsEnd(EndCode.YES);
			processInstReturn.setRetCode(RetCode.EXCEPTION);
			processInstReturn.setRetVal("无法找到对应业务 " + businessId + " 的流程");
		}

		return processInstReturn;
	}

	public ProcessInstance justStartService(String businessId, String dealRole, String dealPerson, String dataStr) {
		ActReProcdef actReProcdef = getActReProcdef(businessId);
		if (actReProcdef == null) {
			return null;
		}
		return runtimeService.startProcessInstanceById(actReProcdef.getId());
	}

	public ProcessInstReturn flowOneStep(String exeId, String dealRole, String dealPerson, String dataStr) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		try {
			JSONObject jsonObj = JSON.parseObject(dataStr);
			JSONObject formData = null;
			if (jsonObj != null) {
				formData = jsonObj.getJSONObject("form_data");
			}
			Execution execution = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
			Task task = taskService.createTaskQuery().executionId(exeId).singleResult();
			if (task != null) {
				Collection<IdentityLink> identityLinkC = taskService.getIdentityLinksForTask(task.getId());
				Set<String> actRoleSet = getActRoleAsMap(identityLinkC);
				if (!actRoleSet.contains(dealRole)) {
					processInstReturn.setIsEnd(EndCode.NO);
					processInstReturn.setRetCode(RetCode.EXCEPTION);
					processInstReturn.setRetVal("角色 " + dealRole + " 没有权限流转这个环节");
					return processInstReturn;
				}
			}
			if (execution != null) {
				Task task2 = taskService.createTaskQuery().executionId(execution.getId()).singleResult();
				if (task2 != null && dealPerson != null) {
					if (task2.getAssignee() == null) {
						taskService.claim(task2.getId(), dealPerson);
					} else {
						taskService.claim(task2.getId(), task2.getAssignee());
					}
					if (formData != null) {
						for (Map.Entry<String, Object> ee : formData.entrySet()) {
							taskService.setVariable(task2.getId(), ee.getKey(), ee.getValue());
						}
					}
					taskService.complete(task2.getId());
				}
				loadProcessInstReturn(processInstReturn, execution, null);
			} else {
				processInstReturn.setIsEnd(EndCode.YES);
				processInstReturn.setRetCode(RetCode.EXCEPTION);
				processInstReturn.setRetVal("流程无法流转到下一步");
			}
		} catch (ActivitiException e) {
			processInstReturn.setRetCode(RetCode.EXCEPTION);
			processInstReturn.setRetVal(e.getMessage());
		}
		return processInstReturn;
	}

	public ProcessInstReturn start(String businessId, String dealRole, String dealPerson, String dataStr) {
		ProcessInstReturn processInstReturn = justStart(businessId, dealRole, dealPerson, dataStr);
		if (RetCode.SUCCESS.equals(processInstReturn.getRetCode()) && EndCode.NO.equals(processInstReturn.getIsEnd())
				&& processInstReturn.getExecutionReturn() != null
				&& !processInstReturn.getExecutionReturn().isEmpty()) {
			ExecutionReturn[] executionReturns = processInstReturn.getExecutionReturn()
					.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
			String exeId = executionReturns[0].getExeId();
			processInstReturn = flowOneStep(exeId, dealRole, dealPerson, dataStr);
		}
		return processInstReturn;
	}

	public ProcessInstReturn suspend(String exeId) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Execution execution = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
		try {
			runtimeService.suspendProcessInstanceById(execution.getProcessInstanceId());
		} catch (ActivitiObjectNotFoundException | NullPointerException e) {
			processInstReturn.setRetCode(RetCode.EXCEPTION);
			processInstReturn.setRetVal("指定工作项不存在");
			return processInstReturn;
		} catch (ActivitiException e) {
			/* 不做特别处理，即允许对已经挂起的工作项进行再次挂起 */
		}
		Collection<Execution> executionC = runtimeService.createExecutionQuery()
				.processInstanceId(execution.getProcessInstanceId()).list();
		Collection<ExecutionReturn> executionReturnC = new ArrayList<>();
		if (!executionC.isEmpty()) {
			for (Execution e : executionC) {
				ExecutionReturn executionReturn = new ExecutionReturn(e);
				executionReturn.setIsEnd(EndCode.NO);
				executionReturnC.add(executionReturn);
			}
			processInstReturn.setExecutionReturn(executionReturnC);
			processInstReturn.setIsEnd(EndCode.NO);
			processInstReturn.setRetCode(RetCode.SUCCESS);
			processInstReturn.setRetVal("1");
		} else {
			processInstReturn.setIsEnd(EndCode.YES);
			processInstReturn.setRetCode(RetCode.SUCCESS);
			processInstReturn.setRetVal("1");
		}
		return processInstReturn;
	}

	public ProcessInstReturn activate(String exeId) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Execution execution = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
		try {
			runtimeService.activateProcessInstanceById(execution.getProcessInstanceId());
		} catch (ActivitiObjectNotFoundException | NullPointerException e) {
			processInstReturn.setRetCode(RetCode.EXCEPTION);
			processInstReturn.setRetVal("指定工作项不存在");
			return processInstReturn;
		} catch (ActivitiException e) {
			/* 不做特别处理，即允许对已经激活的工作项进行再次激活 */
		}
		Collection<Execution> executionC = runtimeService.createExecutionQuery()
				.processInstanceId(execution.getProcessInstanceId()).list();
		Collection<ExecutionReturn> executionReturnC = new ArrayList<>();
		if (!executionC.isEmpty()) {
			for (Execution e : executionC) {
				ExecutionReturn executionReturn = new ExecutionReturn(e);
				executionReturn.setIsEnd(EndCode.NO);
				executionReturnC.add(executionReturn);
			}
			processInstReturn.setExecutionReturn(executionReturnC);
			processInstReturn.setIsEnd(EndCode.NO);
			processInstReturn.setRetCode(RetCode.SUCCESS);
			processInstReturn.setRetVal("1");
		} else {
			processInstReturn.setIsEnd(EndCode.YES);
			processInstReturn.setRetCode(RetCode.SUCCESS);
			processInstReturn.setRetVal("1");
		}
		return processInstReturn;
	}

	public ProcessInstReturn terminate(String exeId) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Execution execution = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
		if (execution == null) {
			processInstReturn.setIsEnd(EndCode.YES);
			processInstReturn.setRetCode(RetCode.EXCEPTION);
			processInstReturn.setRetVal("无法找到执行id为 " + exeId + " 的流程");
			return processInstReturn;
		}
		runtimeService.deleteProcessInstance(execution.getProcessInstanceId(), "终止流程");

		Collection<Execution> executionC = runtimeService.createExecutionQuery()
				.processInstanceId(execution.getProcessInstanceId()).list();
		Collection<ExecutionReturn> executionReturnC = new ArrayList<>();
		if (!executionC.isEmpty()) {
			for (Execution e : executionC) {
				ExecutionReturn executionReturn = new ExecutionReturn(e);
				executionReturn.setIsEnd(EndCode.NO);
				executionReturnC.add(executionReturn);
			}
			processInstReturn.setExecutionReturn(executionReturnC);
			processInstReturn.setIsEnd(EndCode.NO);
			processInstReturn.setRetCode(RetCode.SUCCESS);
			processInstReturn.setRetVal("1");
		} else {
			processInstReturn.setIsEnd(EndCode.YES);
			processInstReturn.setRetCode(RetCode.SUCCESS);
			processInstReturn.setRetVal("1");
		}
		return processInstReturn;
	}

	public ProcessInstReturn receipt(String exeId, String dealPerson) {
		Task task = taskService.createTaskQuery().executionId(exeId).singleResult();
		Execution execution = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		if (execution == null) {
			processInstReturn.setRetCode(RetCode.EXCEPTION);
			processInstReturn.setRetVal("无法找到exeId为 " + exeId + " 的流程实例");
			return processInstReturn;
		}
		processInstReturn = makeProcessInstReturn(execution, task);
		if (task != null && task.getId() != null) {
			if (task.getAssignee() == null || dealPerson.equals(task.getAssignee())) {
				if (task.getAssignee() == null) {
					try {
						taskService.claim(task.getId(), dealPerson);
					} catch (ActivitiObjectNotFoundException | NullPointerException e) {
						processInstReturn.setRetCode(RetCode.EXCEPTION);
						processInstReturn.setRetVal("指定工作项不存在");
						return processInstReturn;
					} catch (ActivitiException e) {
						processInstReturn.setRetCode(RetCode.EXCEPTION);
						processInstReturn.setRetVal("无法认领一个已经挂起的工作项");
						return processInstReturn;
					}
				}
				processInstReturn.setRetCode(RetCode.SUCCESS);
				processInstReturn.setRetVal("1");
			} else {
				processInstReturn.setRetCode(RetCode.EXCEPTION);
				processInstReturn.setRetVal("任务已经被认领，无法再次认领");
			}
		} else {
			processInstReturn.setRetCode(RetCode.EXCEPTION);
			processInstReturn.setRetVal("无法找到exeId为 " + exeId + " 的任务");
		}

		return processInstReturn;
	}

	private ProcessInstReturn makeProcessInstReturn(Execution execution, Task task) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Collection<Execution> executionC = runtimeService.createExecutionQuery()
				.processInstanceId(execution.getProcessInstanceId()).list();
		Collection<ExecutionReturn> executionReturnC = new ArrayList<>();
		if (!executionC.isEmpty()) {
			for (Execution e : executionC) {
				ExecutionReturn executionReturn = new ExecutionReturn(e);
				Collection<IdentityLink> identityLinkC = taskService.getIdentityLinksForTask(task.getId());
				String[] actRole = getActRole(identityLinkC);
				executionReturn.setTaskId(task.getId());
				executionReturn.setActName(task.getName());
				executionReturn.setActRole(actRole);
				executionReturn.setIsEnd(EndCode.NO);
				executionReturnC.add(executionReturn);
			}
			processInstReturn.setExecutionReturn(executionReturnC);
			processInstReturn.setIsEnd(EndCode.NO);
		} else {
			processInstReturn.setIsEnd(EndCode.YES);
		}
		return processInstReturn;
	}

	public ProcessInstReturn unreceipt(String exeId, String dealPerson) {
		Task task = taskService.createTaskQuery().executionId(exeId).singleResult();
		Execution execution = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		if (execution == null) {
			processInstReturn.setRetCode(RetCode.EXCEPTION);
			processInstReturn.setRetVal("无法找到exeId为 " + exeId + " 的流程实例");
			return processInstReturn;
		}
		processInstReturn = makeProcessInstReturn(execution, task);
		if (task != null && task.getId() != null) {
			if (task.getAssignee() == null || dealPerson.equals(task.getAssignee())) {
				if (dealPerson.equals(task.getAssignee())) {
					try {
						taskService.unclaim(task.getId());
					} catch (ActivitiObjectNotFoundException | NullPointerException e) {
						processInstReturn.setRetCode(RetCode.EXCEPTION);
						processInstReturn.setRetVal("指定工作项不存在");
						return processInstReturn;
					} catch (ActivitiException e) {
						processInstReturn.setRetCode(RetCode.EXCEPTION);
						processInstReturn.setRetVal("无法取消认领一个已经挂起的工作项");
						return processInstReturn;
					}
				}
				processInstReturn.setRetCode(RetCode.SUCCESS);
				processInstReturn.setRetVal("1");
			} else {
				processInstReturn.setRetCode(RetCode.EXCEPTION);
				processInstReturn.setRetVal("无法取消，因为任务并非由" + dealPerson + "认领");
			}
		} else {
			processInstReturn.setRetCode(RetCode.EXCEPTION);
			processInstReturn.setRetVal("无法找到exeId为 " + exeId + " 的任务");
		}
		return processInstReturn;
	}

	public ProcessInstReturn jump(String exeId, String activityKey) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Task task = taskService.createTaskQuery().executionId(exeId).singleResult();
		taskService.jump(task.getId(), activityKey, null);
		return processInstReturn;
	}

	/*
	 * 通过一个exeId寻找对应的task对象，如果不存在则在这个exeId对应的执行变量execution的子执行变量中寻找。
	 * 重复这一过程，直到找到一个对象或者返回null。
	 */
	private Task getDoneTaskByExeIdIteration(String exeId) {
		Task task = taskService.createTaskQuery().executionId(exeId).singleResult();
		if (task != null) {
			return task;
		} else {
			Execution execution = runtimeService.createExecutionQuery().parentId(exeId).singleResult();
			if (execution == null || execution.getId() == null) {
				return null;
			} else {
				return getDoneTaskByExeIdIteration(execution.getId());
			}
		}
	}

	public ProcessInstReturn withdraw(String taskId) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId)
				.singleResult();
		if (historicTaskInstance != null && historicTaskInstance.getExecutionId() != null) {
			String exeId = historicTaskInstance.getExecutionId();
			Task task = getDoneTaskByExeIdIteration(exeId);
			if (task != null && task.getId() != null && task.getExecutionId() != null) {
				/* 判断taskId是否是task的上一环节 */
				List<HistoricTaskInstance> htic = historyService.createHistoricTaskInstanceQuery()
						.executionId(historicTaskInstance.getExecutionId()).orderByTaskId().asc().list();
				HistoricTaskInstance lastTask = null;
				for (int i = 1; i < htic.size(); i++) {
					if (task.getId().equals(htic.get(i).getId())) {
						lastTask = htic.get(i - 1);
						break;
					}
				}
				if ((lastTask == null)
						|| (lastTask != null && lastTask.getId() != null && lastTask.getId().equals(taskId))) {
					if (task.getAssignee() == null) {
						taskService.withdraw(task.getId(), taskId, null);
						Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId())
								.singleResult();
						loadProcessInstReturn(processInstReturn, execution, null);
					} else {
						processInstReturn.setRetCode(RetCode.EXCEPTION);
						processInstReturn.setRetVal("任务已经被认领，无法撤回");
					}
				} else {
					processInstReturn.setRetCode(RetCode.EXCEPTION);
					processInstReturn.setRetVal("无法撤回到id为 " + taskId + " 的任务");
				}
			} else {
				processInstReturn.setRetCode(RetCode.EXCEPTION);
				processInstReturn.setRetVal("无法找到taskId为 " + taskId + " 的任务");
			}
		} else {
			processInstReturn.setRetCode(RetCode.EXCEPTION);
			processInstReturn.setRetVal("无法找到taskId为 " + taskId + " 的历史记录，或找到的历史记录中 exeId 不存在");
		}
		return processInstReturn;
	}

	/*
	 * 通过一个processInstanceId寻找对应的execution对象，
	 * 如果不存在则在这个processInstanceId对应的执行变量execution的子执行变量中寻找。
	 * 重复这一过程，直到找到一个对象或者返回null。
	 */
	private Collection<Execution> getExecutionsByProcessInstanceIdIteration(Execution execution) {
		Collection<Execution> executionC = runtimeService.createExecutionQuery().executionId(execution.getId()).list();
		if (!executionC.isEmpty()) {
			return executionC;
		} else {
			Execution execution_ = runtimeService.createExecutionQuery().executionId(execution.getProcessInstanceId())
					.singleResult();
			if (execution_ == null || execution_.getId() == null) {
				return null;
			} else {
				return getExecutionsByProcessInstanceIdIteration(execution_);
			}
		}
	}

	private void dealTasksByExecutionIteration(ProcessInstReturn processInstReturn, Collection<Execution> executionC,
			Collection<ExecutionReturn> executionReturnC, JSONObject formData) {
		if ((executionC != null) && (!executionC.isEmpty())) {
			for (Execution e1 : executionC) {
				Task taskE1 = taskService.createTaskQuery().executionId(e1.getId()).singleResult();

				if (taskE1 != null) {
					ExecutionReturn executionReturn = new ExecutionReturn(e1);
					Collection<IdentityLink> identityLinkCE = taskService.getIdentityLinksForTask(taskE1.getId());
					String[] actRole = getActRole(identityLinkCE);
					executionReturn.setTaskId(taskE1.getId());
					executionReturn.setActName(taskE1.getName());
					executionReturn.setActRole(actRole);
					if (formData != null) {
						for (Map.Entry<String, Object> ee : formData.entrySet()) {
							taskService.setVariable(taskE1.getId(), ee.getKey(), ee.getValue());
						}
					}
					if (END.equals(taskE1.getDescription())) {
						executionReturn.setSoftEnd(true);
						executionReturn.setIsEnd(EndCode.YES);
					} else {
						executionReturn.setIsEnd(EndCode.NO);
					}
					executionReturnC.add(executionReturn);
				} else {
					Collection<Execution> executionC1 = runtimeService.createExecutionQuery().parentId(e1.getId())
							.list();
					if ((executionC1 != null) && (!executionC1.isEmpty())) {
						dealTasksByExecutionIteration(processInstReturn, executionC1, executionReturnC, formData);
					}
				}
			}
			processInstReturn.setExecutionReturn(executionReturnC);
			boolean softEnd = false;
			for (ExecutionReturn executionReturn : executionReturnC) {
				if (executionReturn.isSoftEnd()) {
					softEnd = true;
				} else {
					softEnd = false;
					break;
				}
			}
			if (softEnd) {
				processInstReturn.setIsEnd(EndCode.YES);
			} else {
				processInstReturn.setIsEnd(EndCode.NO);
			}
			processInstReturn.setRetCode(RetCode.SUCCESS);
			processInstReturn.setRetVal("1");
		} else {
			processInstReturn.setIsEnd(EndCode.YES);
			processInstReturn.setRetCode(RetCode.SUCCESS);
			processInstReturn.setRetVal("1");
		}
	}

	private void loadProcessInstReturn(ProcessInstReturn processInstReturn, Execution execution, JSONObject formData) {
		Collection<ExecutionReturn> executionReturnC = new ArrayList<>();
		Collection<Execution> executionC = getExecutionsByProcessInstanceIdIteration(execution);
		dealTasksByExecutionIteration(processInstReturn, executionC, executionReturnC, formData);
	}

	public ProcessInstReturn message(String exeId, String message) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Execution execution_ = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
		try {
			runtimeService.messageEventReceived(message, exeId);
		} catch (ActivitiObjectNotFoundException e) {
			processInstReturn.setRetCode(RetCode.EXCEPTION);
			processInstReturn.setRetVal("找不到exeId为 " + exeId + " 的执行对象");
			return processInstReturn;
		} catch (ActivitiException e2) {
			processInstReturn.setRetCode(RetCode.EXCEPTION);
			processInstReturn.setRetVal("exeId为 " + exeId + " 的执行对象找不到名为 " + message + " 的消息");
			return processInstReturn;
		}
		Execution execution = runtimeService.createExecutionQuery().executionId(execution_.getParentId())
				.singleResult();
		if (execution == null) {
			processInstReturn.setIsEnd(EndCode.YES);
			processInstReturn.setRetCode(RetCode.SUCCESS);
			processInstReturn.setRetVal("1");
		} else {
			loadProcessInstReturn(processInstReturn, execution, null);
		}
		return processInstReturn;
	}
}
