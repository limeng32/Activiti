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
		MyBusinessModel mbm_ = new MyBusinessModel();
		mbm_.setBusinessId(businessId);
		MyBusinessModel myBusinessModel = myBusinessModelService.selectOne(mbm_);
		if (myBusinessModel != null && myBusinessModel.getActReModel() != null) {
			List<Deployment> deploymentC = repositoryService.createDeploymentQuery()
					.deploymentName(myBusinessModel.getActReModel().getName()).orderByDeploymentId().desc()
					.listPage(0, 1);
			if (deploymentC.size() > 0) {
				Deployment deployment = deploymentC.get(0);
				return deployment;
			}
		}
		return null;
	}

	public ActReProcdef getActReProcdef(String businessId) {
		Deployment deployment = getDeployment(businessId);
		if (deployment != null && deployment.getId() != null) {
			ActReProcdef arp_ = new ActReProcdef();
			arp_.setDeploymentId(deployment.getId());
			ActReProcdef actReProcdef = actReProcdefService.selectOne(arp_);
			return actReProcdef;
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
			loadProcessInstReturn(processInstReturn, pi.getId(), formData);
		} else {
			processInstReturn.setIsEnd(EndCode.yes);
			processInstReturn.setRetCode(RetCode.exception);
			processInstReturn.setRetVal("无法找到对应业务 " + businessId + " 的流程");
		}

		return processInstReturn;
	}

	public ProcessInstance justStartService(String businessId, String dealRole, String dealPerson, String dataStr) {
		ActReProcdef actReProcdef = getActReProcdef(businessId);
		if (actReProcdef == null) {
			return null;
		}
		ProcessInstance pi = runtimeService.startProcessInstanceById(actReProcdef.getId());
		return pi;
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
					processInstReturn.setIsEnd(EndCode.no);
					processInstReturn.setRetCode(RetCode.exception);
					processInstReturn.setRetVal("角色 " + dealRole + " 没有权限流转这个环节");
					return processInstReturn;
				}
			}
			if (execution != null) {
				Task task_ = taskService.createTaskQuery().executionId(execution.getId()).singleResult();
				if (task_ != null && dealPerson != null) {
					if (task_.getAssignee() == null) {
						taskService.claim(task_.getId(), dealPerson);
					} else {
						taskService.claim(task_.getId(), task_.getAssignee());
					}
					if (formData != null) {
						for (Map.Entry<String, Object> ee : formData.entrySet()) {
							taskService.setVariable(task_.getId(), ee.getKey(), ee.getValue());
						}
					}
					taskService.complete(task_.getId());
				}
				loadProcessInstReturn(processInstReturn, execution.getProcessInstanceId(), null);
			} else {
				processInstReturn.setIsEnd(EndCode.yes);
				processInstReturn.setRetCode(RetCode.exception);
				processInstReturn.setRetVal("流程无法流转到下一步");
			}
		} catch (ActivitiException e) {
			processInstReturn.setRetCode(RetCode.exception);
			processInstReturn.setRetVal(e.getMessage());
		}
		return processInstReturn;
	}

	public ProcessInstReturn start(String businessId, String dealRole, String dealPerson, String dataStr) {
		ProcessInstReturn processInstReturn = justStart(businessId, dealRole, dealPerson, dataStr);
		if (RetCode.success.equals(processInstReturn.getRetCode()) && EndCode.no.equals(processInstReturn.getIsEnd())) {
			if (processInstReturn.getExecutionReturn() != null && processInstReturn.getExecutionReturn().size() > 0) {
				ExecutionReturn[] executionReturns = processInstReturn.getExecutionReturn()
						.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
				String exeId = executionReturns[0].getExeId();
				processInstReturn = flowOneStep(exeId, dealRole, dealPerson, dataStr);
			}
		}
		return processInstReturn;
	}

	public ProcessInstReturn suspend(String exeId) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Execution execution = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
		try {
			runtimeService.suspendProcessInstanceById(execution.getProcessInstanceId());
		} catch (ActivitiObjectNotFoundException | NullPointerException e) {
			processInstReturn.setRetCode(RetCode.exception);
			processInstReturn.setRetVal("指定工作项不存在");
			return processInstReturn;
		} catch (ActivitiException e) {
			/* 不做特别处理，即允许对已经挂起的工作项进行再次挂起 */
		}
		Collection<Execution> executionC = runtimeService.createExecutionQuery()
				.processInstanceId(execution.getProcessInstanceId()).list();
		Collection<ExecutionReturn> executionReturnC = new ArrayList<>();
		if (executionC.size() > 0) {
			for (Execution e : executionC) {
				ExecutionReturn executionReturn = new ExecutionReturn(e);
				executionReturn.setIsEnd(EndCode.no);
				executionReturnC.add(executionReturn);
			}
			processInstReturn.setExecutionReturn(executionReturnC);
			processInstReturn.setIsEnd(EndCode.no);
			processInstReturn.setRetCode(RetCode.success);
			processInstReturn.setRetVal("1");
		} else {
			processInstReturn.setIsEnd(EndCode.yes);
			processInstReturn.setRetCode(RetCode.success);
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
			processInstReturn.setRetCode(RetCode.exception);
			processInstReturn.setRetVal("指定工作项不存在");
			return processInstReturn;
		} catch (ActivitiException e) {
			/* 不做特别处理，即允许对已经激活的工作项进行再次激活 */
		}
		Collection<Execution> executionC = runtimeService.createExecutionQuery()
				.processInstanceId(execution.getProcessInstanceId()).list();
		Collection<ExecutionReturn> executionReturnC = new ArrayList<>();
		if (executionC.size() > 0) {
			for (Execution e : executionC) {
				ExecutionReturn executionReturn = new ExecutionReturn(e);
				executionReturn.setIsEnd(EndCode.no);
				executionReturnC.add(executionReturn);
			}
			processInstReturn.setExecutionReturn(executionReturnC);
			processInstReturn.setIsEnd(EndCode.no);
			processInstReturn.setRetCode(RetCode.success);
			processInstReturn.setRetVal("1");
		} else {
			processInstReturn.setIsEnd(EndCode.yes);
			processInstReturn.setRetCode(RetCode.success);
			processInstReturn.setRetVal("1");
		}
		return processInstReturn;
	}

	public ProcessInstReturn terminate(String exeId) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Execution execution = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
		if (execution == null) {
			processInstReturn.setIsEnd(EndCode.yes);
			processInstReturn.setRetCode(RetCode.exception);
			processInstReturn.setRetVal("无法找到执行id为 " + exeId + " 的流程");
			return processInstReturn;
		}
		runtimeService.deleteProcessInstance(execution.getProcessInstanceId(), "终止流程");

		Collection<Execution> executionC = runtimeService.createExecutionQuery()
				.processInstanceId(execution.getProcessInstanceId()).list();
		Collection<ExecutionReturn> executionReturnC = new ArrayList<>();
		if (executionC.size() > 0) {
			for (Execution e : executionC) {
				ExecutionReturn executionReturn = new ExecutionReturn(e);
				executionReturn.setIsEnd(EndCode.no);
				executionReturnC.add(executionReturn);
			}
			processInstReturn.setExecutionReturn(executionReturnC);
			processInstReturn.setIsEnd(EndCode.no);
			processInstReturn.setRetCode(RetCode.success);
			processInstReturn.setRetVal("1");
		} else {
			processInstReturn.setIsEnd(EndCode.yes);
			processInstReturn.setRetCode(RetCode.success);
			processInstReturn.setRetVal("1");
		}
		return processInstReturn;
	}

	public ProcessInstReturn receipt(String exeId, String dealPerson) {
		Task task = taskService.createTaskQuery().executionId(exeId).singleResult();
		Execution execution = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		if (execution == null) {
			processInstReturn.setRetCode(RetCode.exception);
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
						processInstReturn.setRetCode(RetCode.exception);
						processInstReturn.setRetVal("指定工作项不存在");
						return processInstReturn;
					} catch (ActivitiException e) {
						processInstReturn.setRetCode(RetCode.exception);
						processInstReturn.setRetVal("无法认领一个已经挂起的工作项");
						return processInstReturn;
					}
				}
				processInstReturn.setRetCode(RetCode.success);
				processInstReturn.setRetVal("1");
			} else {
				processInstReturn.setRetCode(RetCode.exception);
				processInstReturn.setRetVal("任务已经被认领，无法再次认领");
			}
		} else {
			processInstReturn.setRetCode(RetCode.exception);
			processInstReturn.setRetVal("无法找到exeId为 " + exeId + " 的任务");
		}

		return processInstReturn;
	}

	private ProcessInstReturn makeProcessInstReturn(Execution execution, Task task) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Collection<Execution> executionC = runtimeService.createExecutionQuery()
				.processInstanceId(execution.getProcessInstanceId()).list();
		Collection<ExecutionReturn> executionReturnC = new ArrayList<>();
		if (executionC.size() > 0) {
			for (Execution e : executionC) {
				ExecutionReturn executionReturn = new ExecutionReturn(e);
				Collection<IdentityLink> identityLinkC = taskService.getIdentityLinksForTask(task.getId());
				String[] actRole = getActRole(identityLinkC);
				executionReturn.setTaskId(task.getId());
				executionReturn.setActName(task.getName());
				executionReturn.setActRole(actRole);
				executionReturn.setIsEnd(EndCode.no);
				executionReturnC.add(executionReturn);
			}
			processInstReturn.setExecutionReturn(executionReturnC);
			processInstReturn.setIsEnd(EndCode.no);
		} else {
			processInstReturn.setIsEnd(EndCode.yes);
		}
		return processInstReturn;
	}

	public ProcessInstReturn unreceipt(String exeId, String dealPerson) {
		Task task = taskService.createTaskQuery().executionId(exeId).singleResult();
		Execution execution = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		if (execution == null) {
			processInstReturn.setRetCode(RetCode.exception);
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
						processInstReturn.setRetCode(RetCode.exception);
						processInstReturn.setRetVal("指定工作项不存在");
						return processInstReturn;
					} catch (ActivitiException e) {
						processInstReturn.setRetCode(RetCode.exception);
						processInstReturn.setRetVal("无法取消认领一个已经挂起的工作项");
						return processInstReturn;
					}
				}
				processInstReturn.setRetCode(RetCode.success);
				processInstReturn.setRetVal("1");
			} else {
				processInstReturn.setRetCode(RetCode.exception);
				processInstReturn.setRetVal("无法取消，因为任务并非由" + dealPerson + "认领");
			}
		} else {
			processInstReturn.setRetCode(RetCode.exception);
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
	private Task getDoneTaskByIdIteration(String exeId) {
		Task task = taskService.createTaskQuery().executionId(exeId).singleResult();
		if (task != null) {
			return task;
		} else {
			exeId = runtimeService.createExecutionQuery().parentId(exeId).singleResult().getId();
			if (exeId == null) {
				return null;
			} else {
				return getDoneTaskByIdIteration(exeId);
			}
		}
	}

	public ProcessInstReturn withdraw(String taskId) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		String exeId = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult().getExecutionId();
		Task task = getDoneTaskByIdIteration(exeId);
		if (task != null && task.getId() != null) {
			/* 判断taskId是否是task的上一环节 */
			List<HistoricTaskInstance> htic = historyService.createHistoricTaskInstanceQuery().executionId(exeId)
					.orderByTaskId().asc().list();
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
					loadProcessInstReturn(processInstReturn, task.getProcessInstanceId(), null);
				} else {
					processInstReturn.setRetCode(RetCode.exception);
					processInstReturn.setRetVal("任务已经被认领，无法撤回");
				}
			} else {
				processInstReturn.setRetCode(RetCode.exception);
				processInstReturn.setRetVal("无法撤回到id为 " + taskId + " 的任务");
			}
		} else {
			processInstReturn.setRetCode(RetCode.exception);
			processInstReturn.setRetVal("无法找到exeId为 " + exeId + " 的任务");
		}
		return processInstReturn;
	}

	private void loadProcessInstReturn(ProcessInstReturn processInstReturn, String pid, JSONObject formData) {
		Collection<Execution> executionC = runtimeService.createExecutionQuery().processInstanceId(pid).list();
		Collection<ExecutionReturn> executionReturnC = new ArrayList<>();
		boolean softEnd = true;
		if (executionC.size() > 0) {
			for (Execution e : executionC) {
				Task taskE = taskService.createTaskQuery().executionId(e.getId()).singleResult();

				if (taskE != null) {
					ExecutionReturn executionReturn = new ExecutionReturn(e);
					Collection<IdentityLink> identityLinkCE = taskService.getIdentityLinksForTask(taskE.getId());
					String[] actRole = getActRole(identityLinkCE);
					executionReturn.setTaskId(taskE.getId());
					executionReturn.setActName(taskE.getName());
					executionReturn.setActRole(actRole);
					if (formData != null) {
						for (Map.Entry<String, Object> ee : formData.entrySet()) {
							taskService.setVariable(taskE.getId(), ee.getKey(), ee.getValue());
						}
					}
					if (!END.equals(taskE.getDescription())) {
						executionReturn.setIsEnd(EndCode.no);
						softEnd = false;
					} else {
						executionReturn.setIsEnd(EndCode.yes);
					}
					executionReturnC.add(executionReturn);
				}
			}
			processInstReturn.setExecutionReturn(executionReturnC);
			if (softEnd) {
				processInstReturn.setIsEnd(EndCode.yes);
			} else {
				processInstReturn.setIsEnd(EndCode.no);
			}
			processInstReturn.setRetCode(RetCode.success);
			processInstReturn.setRetVal("1");
		} else {
			processInstReturn.setIsEnd(EndCode.yes);
			processInstReturn.setRetCode(RetCode.success);
			processInstReturn.setRetVal("1");
		}
	}

	public ProcessInstReturn message(String exeId, String message) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Execution execution_ = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
		try {
			runtimeService.messageEventReceived(message, exeId);
		} catch (ActivitiObjectNotFoundException e) {
			processInstReturn.setRetCode(RetCode.exception);
			processInstReturn.setRetVal("找不到exeId为 " + exeId + " 的执行对象");
			return processInstReturn;
		} catch (ActivitiException e2) {
			processInstReturn.setRetCode(RetCode.exception);
			processInstReturn.setRetVal("exeId为 " + exeId + " 的执行对象找不到名为 " + message + " 的消息");
			return processInstReturn;
		}
		Execution execution = runtimeService.createExecutionQuery().executionId(execution_.getParentId())
				.singleResult();
		if (execution == null) {
			processInstReturn.setIsEnd(EndCode.yes);
			processInstReturn.setRetCode(RetCode.success);
			processInstReturn.setRetVal("1");
		} else {
			loadProcessInstReturn(processInstReturn, execution.getProcessInstanceId(), null);
		}
		return processInstReturn;
	}
}
