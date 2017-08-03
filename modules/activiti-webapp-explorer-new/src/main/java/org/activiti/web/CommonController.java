package org.activiti.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CommonController {

	@Autowired
	private TestService testServie;

	@RequestMapping(method = { RequestMethod.GET }, value = "/hello")
	public String projectListProject(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		System.out.println("hello!" + testServie.getWord());
		return "index";
	}

}
