package org.activiti.account.exception;

public enum ActivitiAccountExceptionEnum {

	FindRepeatedLoginLog("发现重复的登录日志"), EmailOrPasswordIsNull(
			"email或password为null"), EmailIsNotExist("您输入的邮箱并不存在。"), EmailOrPasswordIsNotExist("您输入的账号或者密码并不存在。"), YourAccountHasProblem(
			"您的账号出现问题，现在无法登陆，具体情况请联系管理员。"), YourAccountNeedActivate(
			"您的账号还没有激活，请登录您的注册邮箱进行激活。"), ActivateFail("您的邮箱激活失败，请与管理员联系。"), ActivateMismatch(
			"您的邮箱与激活码不匹配，未能完成激活。"), ActivateRepetition("您的邮箱已经是激活状态，无需再次激活。"), RepetitionEmail(
			"您的邮箱已经注册过，无法再次注册"), CannotFindLoginLog("您的账号找不到登录日志，现在无法登录"), FailedToInsertLoginLog(
			"为账号增加登录日志时发生异常，可能为频繁登录导致"), CannotFindAccount("无法找到账号"), ConnotUpdateAccountBucket(
			"无法更新账号的扩展内容"), CannotFindAccountBucket("无法找到账号的扩展内容"), RepetitionAccountBucket(
			"这个账号已经拥有了扩展内容"), NoLogin("您还没有登录"), CaptchaIsEmpty("请获取并输入验证码"), CaptchaIsWrong(
			"验证码错误"), ResetPasswordLogNotExist("重置密码记录不存在"), ResetPasswordUrlOverdue(
			"重置密码链接已过期"), ResetPasswordTokenInvalid("重置密码令牌无效"), ResetPasswordUrlUsed(
			"重置密码链接已被使用"), ANewerResetPasswordUrlWorks("有更新的重置请求已经生效"), ResetPasswordFail(
			"重置密码失败，请您稍后尝试"), ActivateAccountLogNotExist("激活账号记录不存在"), ActivateAccountUrlOverdue(
			"激活账号链接已过期"), ActivateAccountTokenInvalid("激活账号令牌无效"), ActivateAccountUrlUsed(
			"激活账号链接已被使用"), ANewerActivateAccountUrlWorks("有更新的激活请求已经生效"), ActivatingAccountOrPasswordIsNull(
			"待激活的账号或密码为null"), ActivateAccountFail("激活账号失败，请重试"), UpdateAccountFail(
			"更新用户名称失败，请您稍后再试"),ActivateAccountSuccess("账户激活成功");

	private final String description;

	private ActivitiAccountExceptionEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}
}
