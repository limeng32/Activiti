package org.activiti.myExplorer.web;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
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
		ProcessInstReturn processInstReturn = deploymentService.justStart(businessId, dealRole, dealPerson, dataStr);
		if (RetCode.success.equals(processInstReturn.getRetCode()) && EndCode.no.equals(processInstReturn.getIsEnd())) {
			if (processInstReturn.getExecutionReturn() != null && processInstReturn.getExecutionReturn().size() > 0) {
				ExecutionReturn[] executionReturns = processInstReturn.getExecutionReturn()
						.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
				String exeId = executionReturns[0].getExeId();
				processInstReturn = deploymentService.flowOneStep(exeId, dealRole, dealPerson, dataStr);
			}
		}
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/justStart")
	public String justStart(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "businessId") String businessId,
			@RequestParam(value = "dealRole", required = false) String dealRole,
			@RequestParam(value = "dealPerson", required = false) String dealPerson,
			@RequestParam(value = "formData", required = false) String dataStr) {
		ProcessInstReturn processInstReturn = deploymentService.justStart(businessId, dealRole, dealPerson, dataStr);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/flow")
	public String flow(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId,
			@RequestParam(value = "dealRole", required = false) String dealRole,
			@RequestParam(value = "dealPerson", required = false) String dealPerson,
			@RequestParam(value = "formData", required = false) String dataStr) {
		ProcessInstReturn processInstReturn = deploymentService.flowOneStep(exeId, dealRole, dealPerson, dataStr);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/suspend")
	public String suspend(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId) {
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

		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/activate")
	public String activate(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId) {
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

		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/terminate")
	public String terminate(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId) {
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

		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/jump")
	public String jump(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId, @RequestParam(value = "targetTaskId") String targetTaskId) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		// Execution execution =
		// processEngineConfiguration.getRuntimeService().createExecutionQuery().executionId(exeId)
		// .singleResult();
		Task task = processEngineConfiguration.getTaskService().createTaskQuery().executionId(exeId).singleResult();
		// System.out.println("1::" + execution.getActivityId());
		// System.out.println("2::" + task.getId());
		processEngineConfiguration.getTaskService().jump(task.getId(), targetTaskId, TaskEntity.JUMP_REASON_JUMP, null);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/withdraw")
	public String withdraw(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId, @RequestParam(value = "taskId") String taskId) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Task task = processEngineConfiguration.getTaskService().createTaskQuery().executionId(exeId).singleResult();
		processEngineConfiguration.getTaskService().jump(task.getId(), taskId, TaskEntity.JUMP_REASON_TURNBACK, null);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}
}
