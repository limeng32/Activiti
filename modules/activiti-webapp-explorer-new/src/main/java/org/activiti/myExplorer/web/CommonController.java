package org.activiti.myExplorer.web;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.activiti.myExplorer.model.ProcessInstReturn;
import org.activiti.myExplorer.model.RetCode;
import org.activiti.myExplorer.persist.ActReModel;
import org.activiti.myExplorer.persist.ActReProcdef;
import org.activiti.myExplorer.service.ActReModelService;
import org.activiti.myExplorer.service.ActReProcdefService;
import org.activiti.myExplorer.service.CommonService;
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
	private CommonService commonService;

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
		Deployment deployment = commonService.getDeployment(businessId);
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
		ProcessInstReturn processInstReturn = commonService.start(businessId, dealRole, dealPerson, dataStr);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/justStart")
	public String justStart(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "businessId") String businessId,
			@RequestParam(value = "dealRole", required = false) String dealRole,
			@RequestParam(value = "dealPerson", required = false) String dealPerson,
			@RequestParam(value = "formData", required = false) String dataStr) {
		ProcessInstReturn processInstReturn = commonService.justStart(businessId, dealRole, dealPerson, dataStr);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/flow")
	public String flow(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId,
			@RequestParam(value = "dealRole", required = false) String dealRole,
			@RequestParam(value = "dealPerson", required = false) String dealPerson,
			@RequestParam(value = "formData", required = false) String dataStr) {
		ProcessInstReturn processInstReturn = commonService.flowOneStep(exeId, dealRole, dealPerson, dataStr);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/suspend")
	public String suspend(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId) {
		ProcessInstReturn processInstReturn = commonService.suspend(exeId);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/activate")
	public String activate(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId) {
		ProcessInstReturn processInstReturn = commonService.activate(exeId);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/terminate")
	public String terminate(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId) {
		ProcessInstReturn processInstReturn = commonService.terminate(exeId);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/jump")
	public String jump(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId, @RequestParam(value = "activityKey") String activityKey) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Task task = processEngineConfiguration.getTaskService().createTaskQuery().executionId(exeId).singleResult();
		processEngineConfiguration.getTaskService().jump(task.getId(), activityKey, null);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/withdraw")
	public String withdraw(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId, @RequestParam(value = "taskId") String taskId) {
		ProcessInstReturn processInstReturn = new ProcessInstReturn();
		Task task = processEngineConfiguration.getTaskService().createTaskQuery().executionId(exeId).singleResult();
		if (task != null && task.getId() != null) {
			/* 判断taskId是否是task的上一环节 */
			List<HistoricTaskInstance> htic = processEngineConfiguration.getHistoryService()
					.createHistoricTaskInstanceQuery().executionId(exeId).orderByTaskId().asc().list();
			HistoricTaskInstance lastTask = null;
			for (int i = 1; i < htic.size(); i++) {
				if (task.getId().equals(htic.get(i).getId())) {
					lastTask = htic.get(i - 1);
					break;
				}
			}
			if (lastTask != null && lastTask.getId() != null && lastTask.getId().equals(taskId)) {
				if (task.getAssignee() == null) {
					processEngineConfiguration.getTaskService().withdraw(task.getId(), taskId, null);
					processInstReturn.setRetCode(RetCode.success);
					processInstReturn.setRetVal("1");
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
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/receipt")
	public String receipt(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId, @RequestParam(value = "dealPerson") String dealPerson) {
		ProcessInstReturn processInstReturn = commonService.receipt(exeId, dealPerson);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/unreceipt")
	public String unreceipt(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId, @RequestParam(value = "dealPerson") String dealPerson) {
		ProcessInstReturn processInstReturn = commonService.unreceipt(exeId, dealPerson);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}
}
