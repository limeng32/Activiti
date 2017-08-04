package org.activiti.myExplorer.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.myExplorer.persist.ActReModel;
import org.activiti.myExplorer.service.ActReModelService;
import org.activiti.myExplorer.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CommonController {

	@Autowired
	private TestService testServie;

	@Autowired
	private ActReModelService actReModelService;

	@RequestMapping(method = { RequestMethod.GET }, value = "/hello")
	public String projectListProject(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		ActReModel ac = new ActReModel();
		ac.setName("Demo model");
		Collection<ActReModel> c = actReModelService.selectAll(ac);
		System.out.println("hello!" + c.size());
		for (ActReModel temp : c) {
			System.out.println("!" + temp.getMetaInfo() + "!" + temp.getName() + "!" + temp.getVersion() + "!"
					+ temp.getLastUpdateTime());
		}
		return "index";
	}

}
