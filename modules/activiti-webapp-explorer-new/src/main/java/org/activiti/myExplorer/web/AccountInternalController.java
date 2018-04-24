package org.activiti.myExplorer.web;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.account.condition.ResetPasswordLogCondition;
import org.activiti.account.exception.ActivitiAccountException;
import org.activiti.account.exception.ActivitiAccountExceptionEnum;
import org.activiti.account.persist.Account;
import org.activiti.account.persist.AccountRole;
import org.activiti.account.persist.Loginlog;
import org.activiti.account.persist.ResetPasswordLog;
import org.activiti.account.persist.Role;
import org.activiti.account.service.AccountRoleService;
import org.activiti.account.service.AccountService;
import org.activiti.account.service.ActivitiAccountService;
import org.activiti.account.service.LoginlogService;
import org.activiti.account.service.ResetPasswordLogService;
import org.activiti.account.service.RoleService;
import org.activiti.account.statics.AccountStatus;
import org.activiti.account.statics.ResetPasswordLogStatus;
import org.activiti.account.statics.RoleStatus;
import org.activiti.myExplorer.commonservice.RandomGenerator;
import org.activiti.myExplorer.commonservice.ThirdVelocityEmailService;
import org.activiti.myExplorer.config.ActivitiConfig;
import org.activiti.myExplorer.model.AccountSession;
import org.activiti.myExplorer.model.CommonReturn;
import org.activiti.myExplorer.model.RetCode;
import org.activiti.myExplorer.persist.AccountBucket;
import org.activiti.myExplorer.service.AccountBucketService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountInternalController {

	@Autowired
	private ActivitiConfig activitiConfig;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountRoleService accountRoleService;

	@Autowired
	private ActivitiAccountService activitiAccountService;

	@Autowired
	private LoginlogService loginlogService;

	@Autowired
	private ResetPasswordLogService resetPasswordLogService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private AccountBucketService accountBucketService;

	@Autowired
	private ThirdVelocityEmailService thirdVelocityEmailService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplateJson;

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

	@RequestMapping(method = { RequestMethod.POST }, value = "/noSession/loginCheckEmail", params = { "value" })
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

	@RequestMapping(method = { RequestMethod.POST }, value = "/noSession/registerCheckEmail", params = { "value" })
	public String registerCheckEmail(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "value") String email) {
		CommonReturn cr = null;
		Account ac = new Account();
		ac.setEmail(email);
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

	@RequestMapping(method = { RequestMethod.POST }, value = "/noSession/accountLogin", params = { "email",
			"password" })
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
				/* 开始插入日志 */
				Loginlog lc = new Loginlog();
				Date now = Calendar.getInstance().getTime();
				lc.setLoginDate(now);
				loginlogService.loadAccount(account, lc);
				int c = account.getLoginlog().size();
				switch (c) {
				case 0:
					Loginlog l = new Loginlog();
					l.setAccount(account);
					l.setLoginIP(request.getRemoteAddr());
					l.setLoginDate(now);
					try {
						activitiAccountService.insertLoginlogTransactive(l);
						/* 写入session */
						account.removeAllLoginlog();
						cr = loginAndSaveSession(account);
					} catch (ActivitiAccountException e) {
						cr = new CommonReturn(RetCode.EXCEPTION, e.getMessage());
					}
					break;
				case 1:
					/* 写入session */
					account.removeAllLoginlog();
					cr = loginAndSaveSession(account);
					break;
				default:
					cr = new CommonReturn(RetCode.EXCEPTION,
							ActivitiAccountExceptionEnum.FindRepeatedLoginLog.description());
					break;
				}
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

	@RequestMapping(method = { RequestMethod.POST }, value = "/noSession/sendRegisterEmail", params = { "email",
			"password" })
	public String sendRegisterEmail(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
		String ip = request.getRemoteAddr();
		CommonReturn cr = null;

		Account ac = new Account();
		ac.setEmail(email);
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

				String url = activitiConfig.getMicroserviceUrl() + "s/noSession/activateAccount/"
						+ resetPasswordLog.getId() + "/" + randomToken;
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("systemName", "工作流系统");
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

	private CommonReturn loginAndSaveSession(Account account) {
		CommonReturn cr;
		boolean b = saveSession(account);
		if (b) {
			cr = new CommonReturn(RetCode.SUCCESS, account.getId());
		} else {
			cr = new CommonReturn(RetCode.EXCEPTION, ActivitiAccountExceptionEnum.SessionSaveFail.description());
		}
		return cr;
	}

	@RequestMapping(method = { RequestMethod.GET }, value = "/noSession/activateAccount/{rplId}/{token}")
	public String activateAccount(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@PathVariable("rplId") String rplId, @PathVariable("token") String token) {
		CommonReturn cr = null;
		ResetPasswordLog resetPasswordLog = resetPasswordLogService.select(rplId);
		if (resetPasswordLog == null || !ResetPasswordLogStatus.i.equals(resetPasswordLog.getStatus())) {
			/* 判断激活账号记录是否存在 */
			cr = new CommonReturn(RetCode.EXCEPTION, ActivitiAccountExceptionEnum.ActivateAccountLogNotExist.name());
		} else if (resetPasswordLog.getTempAccount() == null || resetPasswordLog.getTempPassword() == null) {
			/* 判断待激活的账号名和密码是否存在 */
			cr = new CommonReturn(RetCode.EXCEPTION,
					ActivitiAccountExceptionEnum.ActivatingAccountOrPasswordIsNull.name());
		} else {
			Date now = Calendar.getInstance().getTime();
			if (now.getTime() > resetPasswordLog.getDueTime().getTime()) {
				/* 判断当前时间在dueTime之前 */
				cr = new CommonReturn(RetCode.EXCEPTION, ActivitiAccountExceptionEnum.ActivateAccountUrlOverdue.name());
			} else if (!token.equals(resetPasswordLog.getToken())) {
				/* 判断token是否合法 */
				cr = new CommonReturn(RetCode.EXCEPTION,
						ActivitiAccountExceptionEnum.ActivateAccountTokenInvalid.name());
			} else if (!resetPasswordLog.getAvailable()) {
				/* 判断链接是否还没被使用 */
				cr = new CommonReturn(RetCode.EXCEPTION, ActivitiAccountExceptionEnum.ActivateAccountUrlUsed.name());
			} else {
				/* 判断密码是否已被时间上更近的激活请求激活过 */
				ResetPasswordLogCondition rplc = new ResetPasswordLogCondition();
				rplc.setAvailable(false);
				rplc.setAccount(resetPasswordLog.getAccount());
				rplc.setStatus(ResetPasswordLogStatus.i);
				rplc.setEmailTimeGreaterThan(now);
				int count = resetPasswordLogService.count(rplc);
				if (count > 0) {
					/* 已被时间上更近的激活请求激活过 */
					cr = new CommonReturn(RetCode.EXCEPTION,
							ActivitiAccountExceptionEnum.ANewerActivateAccountUrlWorks.name());
				} else {
					/* 可以开始激活 */
					String tempAccount = resetPasswordLog.getTempAccount();
					String tempPassword = resetPasswordLog.getTempPassword();
					String name = resetPasswordLog.getName();
					resetPasswordLog.setTempAccount("");
					resetPasswordLog.setTempPassword("");
					resetPasswordLog.setAvailable(false);
					int temp = resetPasswordLogService.update(resetPasswordLog);
					if (temp != 1) {
						cr = new CommonReturn(RetCode.EXCEPTION,
								ActivitiAccountExceptionEnum.ActivateAccountFail.name());
					} else {
						/* 激活成功 */
						cr = signUp(tempAccount, tempPassword, name);
					}
				}
			}
		}
		if (RetCode.SUCCESS.equals(cr.getRetCode())) {
			return "redirect:" + activitiConfig.getAccountUrl() + "#/user/global-success?status="
					+ ActivitiAccountExceptionEnum.ActivateAccountSuccess.name();
		} else {
			return "redirect:" + activitiConfig.getAccountUrl() + "#/user/global-error?status=" + cr.getRetVal();
		}
	}

	private boolean saveSession(Account account) {
		Date now = Calendar.getInstance().getTime();
		Date expirationTime = new Date(now.getTime() + 60000);
		AccountSession accountSession = new AccountSession(account, expirationTime);
		redisTemplateJson.opsForValue().set(account.getId(), accountSession);
		boolean b = redisTemplateJson.expire(account.getId(), 60, TimeUnit.SECONDS);
		return b;
	}

	public String getSession(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "id") String id) {
		Object o = redisTemplateJson.opsForValue().get(id);
		mm.addAttribute("_content", o);
		return UNIQUE_PATH;
	}

	private CommonReturn signUp(String accountName, String password, String name) {
		CommonReturn cr = null;

		Account account = new Account();
		account.setEmail(accountName);
		account.setPassword(password);
		account.setName(name);
		account.setActivated(true);
		account.setStatus(AccountStatus.a);
		/* 新增账号的默认角色为TRANSMITTER */
		Role rc = new Role();
		rc.setValue(RoleStatus.u);
		Collection<Role> c = roleService.selectAll(rc);
		AccountRole accountRole = new AccountRole();
		if (c.size() > 0) {
			Role[] roles = c.toArray(new Role[c.size()]);
			accountRole.setAccount(account);
			accountRole.setRole(roles[0]);
		}
		/* 新增账号的默认所属单位为“客人” */
		try {
			activitiAccountService.insertAccountTransactive(account, accountRole);
			cr = new CommonReturn(RetCode.SUCCESS, "账号已成功激活");
		} catch (ActivitiAccountException e) {
			cr = new CommonReturn(RetCode.EXCEPTION, e.getMessage());
		}
		return cr;
	}

	@RequestMapping(method = { RequestMethod.POST }, value = "/heartBeat")
	public String heartBreak(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		Cookie[] cookies = request.getCookies();
		String _uid = null;
		CommonReturn cr = null;
		for (Cookie cookie : cookies) {
			if ("uid".equals(cookie.getName())) {
				_uid = cookie.getValue();
				break;
			}
		}
		if (_uid == null) {
			cr = new CommonReturn(RetCode.EXCEPTION, ActivitiAccountExceptionEnum.NoSession.name());
		} else {
			redisTemplateJson.expire(_uid, 60, TimeUnit.SECONDS);
			cr = new CommonReturn(RetCode.SUCCESS, "");
		}
		mm.addAttribute("_content", cr);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET }, value = "/noSession/test", params = { "id" })
	public String test(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "id") String id) {
		AccountBucket accountBucket = accountBucketService.select(id);
		mm.addAttribute("_content", accountBucket);
		return UNIQUE_PATH;
	}
}
