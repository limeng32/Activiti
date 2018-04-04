package org.activiti.myExplorer.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.account.persist.AccountRole;
import org.activiti.account.service.AccountRoleService;
import org.activiti.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AccountInternalController {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountRoleService accountRoleService;

	public static final String UNIQUE_PATH = "__unique_path";

	@RequestMapping(method = { RequestMethod.GET }, value = "/accountAsd")
	public String currentUser(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		System.out.println("asdasdasd");
		mm.addAttribute("_content", "asdsad");
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET }, value = "/getAccount")
	public String getAccount(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		AccountRole accountRole = accountRoleService.select("c");
		mm.addAttribute("_content", accountRole);
		return UNIQUE_PATH;
	}
}
