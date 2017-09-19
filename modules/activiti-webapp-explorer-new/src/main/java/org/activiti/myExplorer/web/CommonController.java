package org.activiti.myExplorer.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.activiti.myExplorer.persist.ActReModel;
import org.activiti.myExplorer.persist.ActReProcdef;
import org.activiti.myExplorer.service.ActReModelService;
import org.activiti.myExplorer.service.ActReProcdefService;
import org.activiti.myExplorer.service.DeploymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
public class CommonController {

	protected static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private ActReModelService actReModelService;

	@Autowired
	private ActReProcdefService actReProcdefService;

	@Autowired
	private DeploymentService deploymentService;

	@Autowired
	private StandaloneProcessEngineConfiguration processEngineConfiguration;

	public static final String UNIQUE_PATH = "__unique_path";

	@RequestMapping(method = { RequestMethod.GET }, value = "/hello")
	public String hello(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		ActReModel ac = new ActReModel();
		ac.setName("Mail Task  1");
		Collection<ActReModel> c = actReModelService.selectAll(ac);
		for (ActReModel temp : c) {
			System.out.println("!" + temp.getMetaInfo() + "!" + temp.getName() + "!" + temp.getVersion() + "!"
					+ temp.getLastUpdateTime());
			mm.addAttribute("_content", temp);
		}
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET }, value = "/world")
	public String world(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		ActReProcdef arp = new ActReProcdef();
		arp.setKey("Mail_task_1");
		Collection<ActReProcdef> c = actReProcdefService.selectAll(arp);
		for (ActReProcdef temp : c) {
			mm.addAttribute("_content", temp);
		}
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/getProcess")
	public String getProcess(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "businessId") String businessId) {
		Deployment deployment = deploymentService.getDeployment(businessId);
		if (deployment != null && deployment.getId() != null) {
			ActReProcdef arp_ = new ActReProcdef();
			arp_.setDeploymentId(deployment.getId());
			ActReProcdef actReProcdef = actReProcdefService.selectOne(arp_);
			mm.addAttribute("_content", actReProcdef);
		}
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start")
	public String start(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "businessId") String businessId,
			@RequestParam(value = "dealRole", required = false) String dealRole,
			@RequestParam(value = "dealPerson", required = false) String dealPerson,
			@RequestParam(value = "formData", required = false) String dataStr) {
		JSONObject jsonObj = JSON.parseObject(dataStr);
		JSONObject formData = null;
		if (jsonObj != null) {
			formData = jsonObj.getJSONObject("form_data");
		}
		ActReProcdef actReProcdef = deploymentService.getActReProcdef(businessId);
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
					String[] actRole = deploymentService.getActRole(identityLinkC);
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
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/flow")
	public String flow(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId,
			@RequestParam(value = "dealRole", required = false) String dealRole,
			@RequestParam(value = "dealPerson", required = false) String dealPerson,
			@RequestParam(value = "formData", required = false) String dataStr) {
		JSONObject jsonObj = JSON.parseObject(dataStr);
		JSONObject formData = null;
		if (jsonObj != null) {
			formData = jsonObj.getJSONObject("form_data");
		}
		Execution execution = processEngineConfiguration.getRuntimeService().createExecutionQuery().executionId(exeId)
				.singleResult();
		System.out.println("1::" + execution.getActivityId());
		Task task = null;
		if (execution != null) {
			task = processEngineConfiguration.getTaskService().createTaskQuery().executionId(execution.getId())
					.singleResult();
			if (task != null && dealPerson != null) {
				processEngineConfiguration.getTaskService().claim(task.getId(), dealPerson);
				if (formData != null) {
					for (Map.Entry<String, Object> ee : formData.entrySet()) {
						processEngineConfiguration.getTaskService().setVariable(task.getId(), ee.getKey(),
								ee.getValue());
					}
				}
				processEngineConfiguration.getTaskService().complete(task.getId());
			}
			Collection<Execution> executionC = processEngineConfiguration.getRuntimeService().createExecutionQuery()
					.processInstanceId(execution.getProcessInstanceId()).list();

			if (executionC.size() > 0) {
				for (Execution e : executionC) {
					System.out.println("2::" + e.getId());
				}
			} else {
			}
		}

		return UNIQUE_PATH;
	}
}
