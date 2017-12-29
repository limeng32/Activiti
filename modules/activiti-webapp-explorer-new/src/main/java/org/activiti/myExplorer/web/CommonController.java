package org.activiti.myExplorer.web;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.Deployment;
import org.activiti.myExplorer.model.ProcessInstReturn;
import org.activiti.myExplorer.persist.ActReModel;
import org.activiti.myExplorer.persist.ActReProcdef;
import org.activiti.myExplorer.persist.ProcessReturn;
import org.activiti.myExplorer.persist.User;
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

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
@Controller
public class CommonController {

	protected static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private ActReModelService actReModelService;

	@Autowired
	private ActReProcdefService actReProcdefService;

	@Autowired
	private CommonService commonService;

	public static final String UNIQUE_PATH = "__unique_path";

	@RequestMapping(method = { RequestMethod.GET }, value = "/hello")
	public String hello(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		ActReModel ac = new ActReModel();
		ac.setName("Mail Task  1");
		Collection<ActReModel> c = actReModelService.selectAll(ac);
		for (ActReModel temp : c) {
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
			ActReProcdef arp = new ActReProcdef();
			arp.setDeploymentId(deployment.getId());
			ActReProcdef actReProcdef = actReProcdefService.selectOne(arp);
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
		ProcessInstReturn processInstReturn = commonService.jump(exeId, activityKey);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/withdraw")
	public String withdraw(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "taskId") String taskId) {
		ProcessInstReturn processInstReturn = commonService.withdraw(taskId);
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

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/message")
	public String message(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "exeId") String exeId, @RequestParam(value = "message") String message) {
		ProcessInstReturn processInstReturn = commonService.message(exeId, message);
		mm.addAttribute("_content", processInstReturn);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/currentUser")
	public String currentUser(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		mm.addAttribute("_content", new User());
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/fake_list")
	public String fakeList(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "count") int count) {
		User user = new User();
		ProcessReturn p1 = new ProcessReturn("1", "ADSASD");
		ProcessReturn p2 = new ProcessReturn("2", "项目管理2");
		ProcessReturn p3 = new ProcessReturn("3", "Manual_task_1");
		ProcessReturn p4 = new ProcessReturn("6", "Real_task_1");
		ProcessReturn p5 = new ProcessReturn("7", "Service_task_1");
		p1.setOwner(user);
		p2.setOwner(user);
		p3.setOwner(user);
		p4.setOwner(user);
		p5.setOwner(user);
		Collection<ProcessReturn> c = new ArrayList<>();
		c.add(p1);
		c.add(p2);
		c.add(p3);
		c.add(p4);
		c.add(p5);
		mm.addAttribute("_content", c);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/fake_list2")
	public String fakeList2(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "count") int count) {
		ProcessReturn p1 = new ProcessReturn("1", "ADSASD");
		ProcessReturn p2 = new ProcessReturn("2", "项目管理2");
		ProcessReturn p3 = new ProcessReturn("3", "Manual_task_1");
		ProcessReturn p4 = new ProcessReturn("6", "Real_task_1");
		ProcessReturn p5 = new ProcessReturn("7", "Service_task_1");
		Collection<ProcessReturn> c = new ArrayList<>();
		c.add(p1);
		c.add(p2);
		c.add(p3);
		c.add(p4);
		c.add(p5);
		mm.addAttribute("_content", c);
		return UNIQUE_PATH;
	}
}
