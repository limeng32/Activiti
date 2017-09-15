package org.activiti.myExplorer.web;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.myExplorer.persist.ActReModel;
import org.activiti.myExplorer.persist.ActReProcdef;
import org.activiti.myExplorer.persist.MyBusinessModel;
import org.activiti.myExplorer.service.ActReModelService;
import org.activiti.myExplorer.service.ActReProcdefService;
import org.activiti.myExplorer.service.MyBusinessModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;

@Controller
public class CommonController {

	protected static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private ActReModelService actReModelService;

	@Autowired
	private ActReProcdefService actReProcdefService;

	@Autowired
	private MyBusinessModelService myBusinessModelService;

	@Autowired
	private StandaloneProcessEngineConfiguration processEngineConfiguration;

	public static final String MODEL_BUSINESS = "business";

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
		Deployment deployment = this.getDeployment(businessId);
		if (deployment != null && deployment.getId() != null) {
			ActReProcdef arp_ = new ActReProcdef();
			arp_.setDeploymentId(deployment.getId());
			ActReProcdef actReProcdef = actReProcdefService.selectOne(arp_);
			mm.addAttribute("_content", actReProcdef);
		}
		return UNIQUE_PATH;
	}

	public Deployment getDeployment(String businessId) {
		MyBusinessModel mbm_ = new MyBusinessModel();
		mbm_.setBusinessId(businessId);
		MyBusinessModel myBusinessModel = myBusinessModelService.selectOne(mbm_);
		if (myBusinessModel != null && myBusinessModel.getActReModel() != null) {
			List<Deployment> deploymentC = processEngineConfiguration.getRepositoryService().createDeploymentQuery()
					.deploymentName(myBusinessModel.getActReModel().getName()).orderByDeploymentId().desc().list();
			if (deploymentC.size() > 0) {
				Deployment deployment = deploymentC.get(0);
				return deployment;
			}
		}
		return null;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start")
	public String start(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "businessId") String businessId,
			@RequestParam(value = "dealRole", required = false) String dealRole,
			@RequestParam(value = "dealPerson", required = false) String dealPerson,
			@RequestParam(value = "formData", required = false) JSONObject dataJson) {

		return UNIQUE_PATH;
	}
}
