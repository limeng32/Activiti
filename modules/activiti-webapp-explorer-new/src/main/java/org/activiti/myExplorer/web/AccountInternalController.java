package org.activiti.myExplorer.web;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.account.condition.ResetPasswordLogCondition;
import org.activiti.account.exception.ActivitiAccountExceptionEnum;
import org.activiti.account.persist.Account;
import org.activiti.account.persist.AccountRole;
import org.activiti.account.persist.ResetPasswordLog;
import org.activiti.account.service.AccountRoleService;
import org.activiti.account.service.AccountService;
import org.activiti.account.service.ResetPasswordLogService;
import org.activiti.account.statics.ResetPasswordLogStatus;
import org.activiti.myExplorer.model.CommonReturn;
import org.activiti.myExplorer.model.RetCode;
import org.activiti.myExplorer.service.RandomGenerator;
import org.activiti.myExplorer.service.ThirdVelocityEmailService;
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

	@Autowired
	private ResetPasswordLogService resetPasswordLogService;

	@Autowired
	private ThirdVelocityEmailService thirdVelocityEmailService;

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

	@RequestMapping(method = { RequestMethod.POST }, value = "/loginCheckEmail", params = { "value" })
	public String loginCheckEmail(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "value") String email) {
		CommonReturn cr = null;
		Account ac = new Account();
		ac.setEmail(email);
		int count = accountService.count(ac);
		switch (count) {
		case 0:
			cr = new CommonReturn(RetCode.EXCEPTION, ActivitiAccountExceptionEnum.EmailIsNotExist.description());
			break;
		default:
			cr = new CommonReturn(RetCode.SUCCESS, "");
			break;
		}
		mm.addAttribute("_content", cr);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.POST }, value = "/registerCheckEmail", params = { "value" })
	public String registerCheckEmail(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "value") String email) {
		CommonReturn cr = null;
		Account ac = new Account();
		ac.setEmail(email);
		ac.setActivated(true);
		int count = accountService.count(ac);
		switch (count) {
		case 0:
			cr = new CommonReturn(RetCode.SUCCESS, "");
			break;
		default:
			cr = new CommonReturn(RetCode.EXCEPTION, ActivitiAccountExceptionEnum.RepetitionEmail.description());
			break;
		}
		mm.addAttribute("_content", cr);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.POST }, value = "/accountLogin", params = { "email", "password" })
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

	@RequestMapping(method = { RequestMethod.POST }, value = "/sendRegisterEmail", params = { "email", "password" })
	public String sendRegisterEmail(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
		String ip = request.getRemoteAddr();
		CommonReturn cr = null;

		Account ac = new Account();
		ac.setEmail(email);
		ac.setActivated(true);
		int c = accountService.count(ac);
		if (c > 0) {
			cr = new CommonReturn(RetCode.EXCEPTION, ActivitiAccountExceptionEnum.RepetitionEmail.description());
		} else {
			Date now = Calendar.getInstance().getTime();
			Date timeLimitationAgo = new Date(now.getTime() - 60000);
			/* 查找在限制时间内是否还有重置申请 */
			ResetPasswordLogCondition rplc = new ResetPasswordLogCondition();
			rplc.setUserIP(ip);
			rplc.setStatus(ResetPasswordLogStatus.i);
			rplc.setEmailTimeGreaterThan(timeLimitationAgo);
			rplc.setEmailTimeLessOrEqual(now);
			int count = resetPasswordLogService.count(rplc);
			if (count == 0) {
				ResetPasswordLog resetPasswordLog = new ResetPasswordLog();
				resetPasswordLog.setStatus(ResetPasswordLogStatus.i);
				resetPasswordLog.setUserIP(ip);
				resetPasswordLog.setEmailTime(now);
				String randomToken = RandomGenerator.getRandomString(50);
				resetPasswordLog.setToken(randomToken);
				resetPasswordLog.setUserIP(ip);
				Date dueTime = new Date(now.getTime() + 86400000);
				resetPasswordLog.setDueTime(dueTime);
				resetPasswordLog.setAvailable(true);
				resetPasswordLog.setTempAccount(email);
				resetPasswordLog.setTempPassword(DigestUtils.md5Hex(password));
				resetPasswordLogService.insert(resetPasswordLog);

				String url = "http://" + resetPasswordLog.getId() + "/" + randomToken;
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("systemName", "科技评奖系统");
				model.put("serviceName", "激活账号服务");
				String userName = resetPasswordLog.getName();
				model.put("userName", userName);
				model.put("activateAccountUrl", url);
				String[] emails = { email };
				thirdVelocityEmailService.sendEmail(model, "联通软件研究院工作流系统-激活账号服务", "vm/register.vm", emails,
						new String[] {});
				cr = new CommonReturn(RetCode.SUCCESS, "激活账号的邮件已发送，1分钟内请不要重复发送邮件");
			} else {
				cr = new CommonReturn(RetCode.EXCEPTION, "系统繁忙，请您稍后再发送邮件");
			}
		}

		mm.addAttribute("_content", cr);
		return UNIQUE_PATH;
	}
}
