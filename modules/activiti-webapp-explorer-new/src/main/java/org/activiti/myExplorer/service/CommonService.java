package org.activiti.myExplorer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
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
	private StandaloneProcessEngineConfiguration processEngineConfiguration;

	public Deployment getDeployment(String businessId) {
		MyBusinessModel mbm_ = new MyBusinessModel();
		mbm_.setBusinessId(businessId);
		MyBusinessModel myBusinessModel = myBusinessModelService.selectOne(mbm_);
		if (myBusinessModel != null && myBusinessModel.getActReModel() != null) {
			List<Deployment> deploymentC = processEngineConfiguration.getRepositoryService().createDeploymentQuery()
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
		Collection<String> collection = new LinkedHashSet<>();
		for (IdentityLink identityLink : identityLinkC) {
			if (identityLink.getGroupId() != null) {
				collection.add(identityLink.getGroupId());
			}
			if (identityLink.getUserId() != null) {
				collection.add(identityLink.getUserId());
			}
		}
		String[] ret = new String[collection.size()];
		return collection.toArray(ret);
	}

	public ProcessInstReturn justStart(String businessId, String dealRole, String dealPerson, String dataStr) {
		JSONObject jsonObj = JSON.parseObject(dataStr);
		JSONObject formData = null;
		if (jsonObj != null) {
			formData = jsonObj.getJSONObject("form_data");
		}
		ActReProcdef actReProcdef = getActReProcdef(businessId);
		ProcessInstance pi = processEngineConfiguration.getRuntimeService()
				.startProcessInstanceById(actReProcdef.getId());
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		if (pi != null) {
			Collection<Execution> executionC = processEngineConfiguration.getRuntimeService().createExecutionQuery()
					.processInstanceId(pi.getId()).list();
			Collection<ExecutionReturn> executionReturnC = new ArrayList<>();
			if (executionC.size() > 0) {
				for (Execution e : executionC) {
					Task task = processEngineConfiguration.getTaskService().createTaskQuery().executionId(e.getId())
							.singleResult();
					Collection<IdentityLink> identityLinkC = processEngineConfiguration.getTaskService()
							.getIdentityLinksForTask(task.getId());
					String[] actRole = getActRole(identityLinkC);
					ExecutionReturn executionReturn = new ExecutionReturn(e);
					executionReturn.setTaskId(task.getId());
					executionReturn.setActName(task.getName());
					executionReturn.setActRole(actRole);
					executionReturn.setIsEnd(EndCode.no);
					executionReturnC.add(executionReturn);
					if (formData != null) {
						for (Map.Entry<String, Object> ee : formData.entrySet()) {
							processEngineConfiguration.getTaskService().setVariable(task.getId(), ee.getKey(),
									ee.getValue());
						}
					}
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
		} else {
			processInstReturn.setIsEnd(EndCode.yes);
			processInstReturn.setRetCode(RetCode.exception);
			processInstReturn.setRetVal("流程无法启动");
		}
		return processInstReturn;
	}

	public ProcessInstReturn flowOneStep(String exeId, String dealRole, String dealPerson, String dataStr) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		try {
			JSONObject jsonObj = JSON.parseObject(dataStr);
			JSONObject formData = null;
			if (jsonObj != null) {
				formData = jsonObj.getJSONObject("form_data");
			}
			Execution execution = processEngineConfiguration.getRuntimeService().createExecutionQuery()
					.executionId(exeId).singleResult();
			if (execution != null) {
				Task task_ = processEngineConfiguration.getTaskService().createTaskQuery()
						.executionId(execution.getId()).singleResult();
				if (task_ != null && dealPerson != null) {
					if (task_.getAssignee() == null) {
						processEngineConfiguration.getTaskService().claim(task_.getId(), dealPerson);
					} else {
						processEngineConfiguration.getTaskService().claim(task_.getId(), task_.getAssignee());
					}
					if (formData != null) {
						for (Map.Entry<String, Object> ee : formData.entrySet()) {
							processEngineConfiguration.getTaskService().setVariable(task_.getId(), ee.getKey(),
									ee.getValue());
						}
					}
					processEngineConfiguration.getTaskService().complete(task_.getId());
				}
				Collection<Execution> executionC = processEngineConfiguration.getRuntimeService().createExecutionQuery()
						.processInstanceId(execution.getProcessInstanceId()).list();
				Collection<ExecutionReturn> executionReturnC = new ArrayList<>();
				if (executionC.size() > 0) {
					for (Execution e : executionC) {
						Task task = processEngineConfiguration.getTaskService().createTaskQuery().executionId(e.getId())
								.singleResult();
						Collection<IdentityLink> identityLinkC = processEngineConfiguration.getTaskService()
								.getIdentityLinksForTask(task.getId());
						String[] actRole = getActRole(identityLinkC);
						ExecutionReturn executionReturn = new ExecutionReturn(e);
						executionReturn.setTaskId(task.getId());
						executionReturn.setActName(task.getName());
						executionReturn.setActRole(actRole);
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
		Execution execution = processEngineConfiguration.getRuntimeService().createExecutionQuery().executionId(exeId)
				.singleResult();
		processEngineConfiguration.getRuntimeService().suspendProcessInstanceById(execution.getProcessInstanceId());

		Collection<Execution> executionC = processEngineConfiguration.getRuntimeService().createExecutionQuery()
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
		Execution execution = processEngineConfiguration.getRuntimeService().createExecutionQuery().executionId(exeId)
				.singleResult();
		processEngineConfiguration.getRuntimeService().activateProcessInstanceById(execution.getProcessInstanceId());

		Collection<Execution> executionC = processEngineConfiguration.getRuntimeService().createExecutionQuery()
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
		Execution execution = processEngineConfiguration.getRuntimeService().createExecutionQuery().executionId(exeId)
				.singleResult();
		processEngineConfiguration.getRuntimeService().deleteProcessInstance(execution.getProcessInstanceId(), "终止流程");

		Collection<Execution> executionC = processEngineConfiguration.getRuntimeService().createExecutionQuery()
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
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Task task = processEngineConfiguration.getTaskService().createTaskQuery().executionId(exeId).singleResult();
		if (task != null && task.getId() != null) {
			if (task.getAssignee() == null || dealPerson.equals(task.getAssignee())) {
				if (task.getAssignee() == null) {
					processEngineConfiguration.getTaskService().claim(task.getId(), dealPerson);
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

	public ProcessInstReturn unreceipt(String exeId, String dealPerson) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Task task = processEngineConfiguration.getTaskService().createTaskQuery().executionId(exeId).singleResult();
		if (task != null && task.getId() != null) {
			if (task.getAssignee() == null || dealPerson.equals(task.getAssignee())) {
				if (dealPerson.equals(task.getAssignee())) {
					processEngineConfiguration.getTaskService().unclaim(task.getId());
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
}
