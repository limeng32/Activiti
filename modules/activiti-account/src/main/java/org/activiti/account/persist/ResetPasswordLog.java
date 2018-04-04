package org.activiti.account.persist;

import java.io.Serializable;

import org.activiti.account.face.ResetPasswordLogFace;
import org.activiti.account.statics.ResetPasswordLogStatus;
import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;

@TableMapperAnnotation(tableName = "resetPasswordLog")
public class ResetPasswordLog extends PojoSupport<ResetPasswordLog> implements Serializable, ResetPasswordLogFace {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键，以UUID方式保存
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "reset_pass_id", jdbcType = JdbcType.VARCHAR, isUniqueKey = true)
	private java.lang.String id;

	/**
	 * 重置请求时间
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "emailTime", jdbcType = JdbcType.TIMESTAMP)
	private java.util.Date emailTime;

	/**
	 * 链接有效期到期时间
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "dueTime", jdbcType = JdbcType.TIMESTAMP)
	private java.util.Date dueTime;

	/**
	 * 重置发生时间
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "resetTime", jdbcType = JdbcType.TIMESTAMP)
	private java.util.Date resetTime;

	/**
	 * 发起重置密码请求的用户IP
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "userIP", jdbcType = JdbcType.VARCHAR)
	private java.lang.String userIP;

	/**
	 * 此次重置密码的随机数token
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "token", jdbcType = JdbcType.VARCHAR)
	private java.lang.String token;

	/**
	 * 重置密码的链接
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "url", jdbcType = JdbcType.VARCHAR)
	private java.lang.String url;

	/**
	 * 链接是否可用。若用户使用此链接修改密码成功后便不再可用。
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "available", jdbcType = JdbcType.BOOLEAN)
	private Boolean available;

	/**
	 * 状态值。当为i时表示新增用户，当为u时表示更新用户。
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "status", jdbcType = JdbcType.CHAR)
	private ResetPasswordLogStatus status;

	/**
	 * 用户账号，当此条记录为激活账号记录时才有需要
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "tempAccount", jdbcType = JdbcType.VARCHAR)
	private String tempAccount;

	/**
	 * 用户密码，当此条记录为激活账号记录时才有需要
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "tempPassword", jdbcType = JdbcType.VARCHAR)
	private String tempPassword;

	@FieldMapperAnnotation(dbFieldName = "account_id", jdbcType = JdbcType.VARCHAR, dbAssociationUniqueKey = "account_id")
	private Account account;

	public java.util.Date getEmailTime() {
		return emailTime;
	}

	public void setEmailTime(java.util.Date emailTime) {
		this.emailTime = emailTime;
	}

	public java.util.Date getDueTime() {
		return dueTime;
	}

	public void setDueTime(java.util.Date dueTime) {
		this.dueTime = dueTime;
	}

	public java.util.Date getResetTime() {
		return resetTime;
	}

	public void setResetTime(java.util.Date resetTime) {
		this.resetTime = resetTime;
	}

	public java.lang.String getUserIP() {
		return userIP;
	}

	public void setUserIP(java.lang.String userIP) {
		this.userIP = userIP;
	}

	public java.lang.String getToken() {
		return token;
	}

	public void setToken(java.lang.String token) {
		this.token = token;
	}

	public java.lang.String getUrl() {
		return url;
	}

	public void setUrl(java.lang.String url) {
		this.url = url;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public ResetPasswordLogStatus getStatus() {
		return status;
	}

	public void setStatus(ResetPasswordLogStatus status) {
		this.status = status;
	}

	public String getTempAccount() {
		return tempAccount;
	}

	public void setTempAccount(String tempAccount) {
		this.tempAccount = tempAccount;
	}

	public String getTempPassword() {
		return tempPassword;
	}

	public void setTempPassword(String tempPassword) {
		this.tempPassword = tempPassword;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	@Override
	public void setAccount(Account newAccount) {
		if (this.account == null || this.account != newAccount) {
			if (this.account != null) {
				Account oldAccount = this.account;
				this.account = null;
				oldAccount.removeResetPasswordLog(this);
			}
			if (newAccount != null) {
				this.account = newAccount;
				this.account.addResetPasswordLog(this);
			}
		}
	}

	@Override
	public Object getId() {
		return id;
	}

	public String getName() {
		String ret = null;
		if (tempAccount != null && tempAccount.indexOf("@") != -1) {
			ret = tempAccount.substring(0, tempAccount.indexOf("@"));
			if (ret.length() > 20) {
				ret = ret.substring(0, 20);
			}
		}
		return ret;
	}

}