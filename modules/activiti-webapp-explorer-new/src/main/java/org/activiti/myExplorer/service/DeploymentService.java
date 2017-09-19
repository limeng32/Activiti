package org.activiti.myExplorer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

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
public class DeploymentService {

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
		JSONObject jsonObj = JSON.parseObject(dataStr);
		JSONObject formData = null;
		if (jsonObj != null) {
			formData = jsonObj.getJSONObject("form_data");
		}
		Execution execution = processEngineConfiguration.getRuntimeService().createExecutionQuery().executionId(exeId)
				.singleResult();
		if (execution != null) {
			Task task_ = processEngineConfiguration.getTaskService().createTaskQuery().executionId(execution.getId())
					.singleResult();
			if (task_ != null && dealPerson != null) {
				processEngineConfiguration.getTaskService().claim(task_.getId(), dealPerson);
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
		return processInstReturn;
	}
}
