package org.activiti.account.persist;

import java.io.Serializable;

import org.activiti.account.face.LoginlogFace;
import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;

@TableMapperAnnotation(tableName = "loginlog")
public class Loginlog extends PojoSupport<Loginlog> implements Serializable, LoginlogFace {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键，以UUID方式保存
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "loginlog_id", jdbcType = JdbcType.VARCHAR, isUniqueKey = true)
	private java.lang.String id;

	/**
	 * 用户登录时IP地址
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "loginIP", jdbcType = JdbcType.VARCHAR)
	private java.lang.String loginIP;

	/**
	 * 记录本次登录的日期
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "loginDate", jdbcType = JdbcType.DATE)
	private java.util.Date loginDate;

	@FieldMapperAnnotation(dbFieldName = "account_id", jdbcType = JdbcType.VARCHAR, dbAssociationUniqueKey = "account_id")
	private Account account;

	public Account getAccount() {
		return account;
	}

	@Override
	public void setAccount(Account newAccount) {
		if (this.account == null || this.account != newAccount) {
			if (this.account != null) {
				Account oldAccount = this.account;
				this.account = null;
				oldAccount.removeLoginlog(this);
			}
			if (newAccount != null) {
				this.account = newAccount;
				this.account.addLoginlog(this);
			}
		}
	}

	public java.lang.String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(java.lang.String loginIP) {
		this.loginIP = loginIP;
	}

	public java.util.Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(java.util.Date loginDate) {
		this.loginDate = loginDate;
	}

	@Override
	public Object getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

}
