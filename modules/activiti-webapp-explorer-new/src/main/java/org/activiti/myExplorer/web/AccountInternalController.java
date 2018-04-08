package org.activiti.myExplorer.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.account.exception.ActivitiAccountExceptionEnum;
import org.activiti.account.persist.Account;
import org.activiti.account.persist.AccountRole;
import org.activiti.account.service.AccountRoleService;
import org.activiti.account.service.AccountService;
import org.activiti.myExplorer.model.CommonReturn;
import org.activiti.myExplorer.model.RetCode;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

	@RequestMapping(method = { RequestMethod.POST }, value = "/loginTest")
	public String loginTest(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "value") String email) {
		CommonReturn cr = null;
		Account ac = new Account();
		ac.setEmail(email);
		int count = accountService.count(ac);
		switch (count) {
		case 0:
			cr = new CommonReturn(RetCode.EXCEPTION,
					ActivitiAccountExceptionEnum.EmailIsNotExist.description());
			break;
		default:
			cr = new CommonReturn(RetCode.SUCCESS, "");
			break;
		}
		mm.addAttribute("_content", cr);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/accountLogin")
	public String accountLogin(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
		CommonReturn cr = null;
		Account ac = new Account();
		ac.setEmail(email);
		ac.setPassword(DigestUtils.md5Hex(password));
		Collection<Account> accountC = accountService.selectAll(ac);
		switch (accountC.size()) {
		case 1:
			Account account = accountC.toArray(new Account[1])[0];
			if (account.getActivated()) {
				cr = new CommonReturn(RetCode.SUCCESS, "");
			} else {
				cr = new CommonReturn(RetCode.EXCEPTION,
						ActivitiAccountExceptionEnum.YourAccountNeedActivate.description());
			}
			break;
		case 0:
			cr = new CommonReturn(RetCode.EXCEPTION,
					ActivitiAccountExceptionEnum.EmailOrPasswordIsNotExist.description());
			break;
		default:
			cr = new CommonReturn(RetCode.EXCEPTION, ActivitiAccountExceptionEnum.YourAccountHasProblem.description());
			break;
		}
		mm.addAttribute("_content", cr);
		return UNIQUE_PATH;
	}
}
