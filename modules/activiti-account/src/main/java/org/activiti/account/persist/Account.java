package org.activiti.account.persist;

import java.io.Serializable;
import java.util.Collection;

import org.activiti.account.face.AccountBucketFace;
import org.activiti.account.face.AccountFace;
import org.activiti.account.face.AccountRoleFace;
import org.activiti.account.face.LoginlogFace;
import org.activiti.account.face.ResetPasswordLogFace;
import org.activiti.account.statics.AccountStatus;
import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import com.alibaba.fastjson.annotation.JSONField;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.statics.OpLockType;

@TableMapperAnnotation(tableName = "account")
public class Account extends PojoSupport<Account> implements Serializable, AccountFace {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键，以UUID方式保存
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "account_id", jdbcType = JdbcType.VARCHAR, isUniqueKey = true)
	private String id;

	/**
	 * 用户名
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "name", jdbcType = JdbcType.VARCHAR)
	private java.lang.String name;

	/**
	 * 用户邮箱
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "email", jdbcType = JdbcType.VARCHAR)
	private java.lang.String email;

	/**
	 * 登录密码
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "password", jdbcType = JdbcType.VARCHAR)
	private java.lang.String password;

	/**
	 * 是否已激活。0：否；1：是。
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "activated", jdbcType = JdbcType.BOOLEAN)
	private Boolean activated;

	/**
	 * 状态值，对应枚举类变量，包括“休眠、活跃”等状态
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "status", jdbcType = JdbcType.CHAR)
	private AccountStatus status;

	/**
	 * 激活码
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "activateValue", jdbcType = JdbcType.VARCHAR)
	private java.lang.String activateValue;

	/**
	 * 乐观锁
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "opLock", jdbcType = JdbcType.INTEGER, opLockType = OpLockType.Version)
	@JSONField(serialize = false)
	private Integer opLock;

	private Collection<AccountRoleFace> accountRole;

	private Collection<AccountBucketFace> accountBucket;

	private Collection<LoginlogFace> loginlog;

	private Collection<ResetPasswordLogFace> resetPasswordLog;

	@Override
	public String getId() {
		return id;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public java.lang.String getActivateValue() {
		return activateValue;
	}

	public void setActivateValue(java.lang.String activateValue) {
		this.activateValue = activateValue;
	}

	public Integer getOpLock() {
		return opLock;
	}

	public void setId(String id) {
		this.id = id;
	}

	public java.util.Collection<AccountRoleFace> getAccountRole() {
		if (accountRole == null)
			accountRole = new java.util.LinkedHashSet<AccountRoleFace>();
		return accountRole;
	}

	@JSONField(serialize = false)
	public java.util.Iterator<AccountRoleFace> getIteratorAccountRole() {
		if (accountRole == null)
			accountRole = new java.util.LinkedHashSet<AccountRoleFace>();
		return accountRole.iterator();
	}

	public void setAccountRole(java.util.Collection<? extends AccountRoleFace> newAccountRole) {
		removeAllAccountRole();
		for (java.util.Iterator<? extends AccountRoleFace> iter = newAccountRole.iterator(); iter.hasNext();)
			addAccountRole((AccountRoleFace) iter.next());
	}

	public void addAccountRole(AccountRoleFace newAccountRole) {
		if (newAccountRole == null)
			return;
		if (this.accountRole == null)
			this.accountRole = new java.util.LinkedHashSet<AccountRoleFace>();
		if (!this.accountRole.contains(newAccountRole)) {
			this.accountRole.add(newAccountRole);
			newAccountRole.setAccount(this);
		} else {
			for (AccountRoleFace temp : this.accountRole) {
				if (newAccountRole.equals(temp)) {
					if (temp != newAccountRole) {
						removeAccountRole(temp);
						this.accountRole.add(newAccountRole);
						newAccountRole.setAccount(this);
					}
					break;
				}
			}
		}
	}

	public void removeAccountRole(AccountRoleFace oldAccountRole) {
		if (oldAccountRole == null)
			return;
		if (this.accountRole != null)
			if (this.accountRole.contains(oldAccountRole)) {
				for (AccountRoleFace temp : this.accountRole) {
					if (oldAccountRole.equals(temp)) {
						if (temp != oldAccountRole) {
							temp.setAccount((Account) null);
						}
						break;
					}
				}
				this.accountRole.remove(oldAccountRole);
				oldAccountRole.setAccount((Account) null);
			}
	}

	public void removeAllAccountRole() {
		if (accountRole != null) {
			AccountRoleFace oldAccountRole;
			for (java.util.Iterator<AccountRoleFace> iter = getIteratorAccountRole(); iter.hasNext();) {
				oldAccountRole = (AccountRoleFace) iter.next();
				iter.remove();
				oldAccountRole.setAccount((Account) null);
			}
			accountRole.clear();
		}
	}

	public java.util.Collection<AccountBucketFace> getAccountBucket() {
		if (accountBucket == null)
			accountBucket = new java.util.LinkedHashSet<AccountBucketFace>();
		return accountBucket;
	}

	@JSONField(serialize = false)
	public java.util.Iterator<AccountBucketFace> getIteratorAccountBucket() {
		if (accountBucket == null)
			accountBucket = new java.util.LinkedHashSet<AccountBucketFace>();
		return accountBucket.iterator();
	}

	public void setAccountBucket(java.util.Collection<? extends AccountBucketFace> newAccountBucket) {
		removeAllAccountBucket();
		for (java.util.Iterator<? extends AccountBucketFace> iter = newAccountBucket.iterator(); iter.hasNext();)
			addAccountBucket((AccountBucketFace) iter.next());
	}

	public void addAccountBucket(AccountBucketFace newAccountBucket) {
		if (newAccountBucket == null)
			return;
		if (this.accountBucket == null)
			this.accountBucket = new java.util.LinkedHashSet<AccountBucketFace>();
		if (!this.accountBucket.contains(newAccountBucket)) {
			this.accountBucket.add(newAccountBucket);
			newAccountBucket.setAccount(this);
		} else {
			for (AccountBucketFace temp : this.accountBucket) {
				if (newAccountBucket.equals(temp)) {
					if (temp != newAccountBucket) {
						removeAccountBucket(temp);
						this.accountBucket.add(newAccountBucket);
						newAccountBucket.setAccount(this);
					}
					break;
				}
			}
		}
	}

	public void removeAccountBucket(AccountBucketFace oldAccountBucket) {
		if (oldAccountBucket == null)
			return;
		if (this.accountBucket != null)
			if (this.accountBucket.contains(oldAccountBucket)) {
				for (AccountBucketFace temp : this.accountBucket) {
					if (oldAccountBucket.equals(temp)) {
						if (temp != oldAccountBucket) {
							temp.setAccount((Account) null);
						}
						break;
					}
				}
				this.accountBucket.remove(oldAccountBucket);
				oldAccountBucket.setAccount((Account) null);
			}
	}

	public void removeAllAccountBucket() {
		if (accountBucket != null) {
			AccountBucketFace oldAccountBucket;
			for (java.util.Iterator<AccountBucketFace> iter = getIteratorAccountBucket(); iter.hasNext();) {
				oldAccountBucket = (AccountBucketFace) iter.next();
				iter.remove();
				oldAccountBucket.setAccount((Account) null);
			}
			accountBucket.clear();
		}
	}

	public java.util.Collection<LoginlogFace> getLoginlog() {
		if (loginlog == null)
			loginlog = new java.util.LinkedHashSet<LoginlogFace>();
		return loginlog;
	}

	@JSONField(serialize = false)
	public java.util.Iterator<LoginlogFace> getIteratorLoginlog() {
		if (loginlog == null)
			loginlog = new java.util.LinkedHashSet<LoginlogFace>();
		return loginlog.iterator();
	}

	public void setLoginlog(java.util.Collection<? extends LoginlogFace> newLoginlog) {
		removeAllLoginlog();
		for (java.util.Iterator<? extends LoginlogFace> iter = newLoginlog.iterator(); iter.hasNext();)
			addLoginlog((LoginlogFace) iter.next());
	}

	public void addLoginlog(LoginlogFace newLoginlog) {
		if (newLoginlog == null)
			return;
		if (this.loginlog == null)
			this.loginlog = new java.util.LinkedHashSet<LoginlogFace>();
		if (!this.loginlog.contains(newLoginlog)) {
			this.loginlog.add(newLoginlog);
			newLoginlog.setAccount(this);
		} else {
			for (LoginlogFace temp : this.loginlog) {
				if (newLoginlog.equals(temp)) {
					if (temp != newLoginlog) {
						removeLoginlog(temp);
						this.loginlog.add(newLoginlog);
						newLoginlog.setAccount(this);
					}
					break;
				}
			}
		}
	}

	public void removeLoginlog(LoginlogFace oldLoginlog) {
		if (oldLoginlog == null)
			return;
		if (this.loginlog != null)
			if (this.loginlog.contains(oldLoginlog)) {
				for (LoginlogFace temp : this.loginlog) {
					if (oldLoginlog.equals(temp)) {
						if (temp != oldLoginlog) {
							temp.setAccount((Account) null);
						}
						break;
					}
				}
				this.loginlog.remove(oldLoginlog);
				oldLoginlog.setAccount((Account) null);
			}
	}

	public void removeAllLoginlog() {
		if (loginlog != null) {
			LoginlogFace oldLoginlog;
			for (java.util.Iterator<LoginlogFace> iter = getIteratorLoginlog(); iter.hasNext();) {
				oldLoginlog = (LoginlogFace) iter.next();
				iter.remove();
				oldLoginlog.setAccount((Account) null);
			}
			loginlog.clear();
		}
	}

	public java.util.Collection<ResetPasswordLogFace> getResetPasswordLog() {
		if (resetPasswordLog == null)
			resetPasswordLog = new java.util.LinkedHashSet<ResetPasswordLogFace>();
		return resetPasswordLog;
	}

	@JSONField(serialize = false)
	public java.util.Iterator<ResetPasswordLogFace> getIteratorResetPasswordLog() {
		if (resetPasswordLog == null)
			resetPasswordLog = new java.util.LinkedHashSet<ResetPasswordLogFace>();
		return resetPasswordLog.iterator();
	}

	public void setResetPasswordLog(java.util.Collection<? extends ResetPasswordLogFace> newResetPasswordLog) {
		removeAllResetPasswordLog();
		for (java.util.Iterator<? extends ResetPasswordLogFace> iter = newResetPasswordLog.iterator(); iter.hasNext();)
			addResetPasswordLog((ResetPasswordLogFace) iter.next());
	}

	public void addResetPasswordLog(ResetPasswordLogFace newResetPasswordLog) {
		if (newResetPasswordLog == null)
			return;
		if (this.resetPasswordLog == null)
			this.resetPasswordLog = new java.util.LinkedHashSet<ResetPasswordLogFace>();
		if (!this.resetPasswordLog.contains(newResetPasswordLog)) {
			this.resetPasswordLog.add(newResetPasswordLog);
			newResetPasswordLog.setAccount(this);
		} else {
			for (ResetPasswordLogFace temp : this.resetPasswordLog) {
				if (newResetPasswordLog.equals(temp)) {
					if (temp != newResetPasswordLog) {
						removeResetPasswordLog(temp);
						this.resetPasswordLog.add(newResetPasswordLog);
						newResetPasswordLog.setAccount(this);
					}
					break;
				}
			}
		}
	}

	public void removeResetPasswordLog(ResetPasswordLogFace oldResetPasswordLog) {
		if (oldResetPasswordLog == null)
			return;
		if (this.resetPasswordLog != null)
			if (this.resetPasswordLog.contains(oldResetPasswordLog)) {
				for (ResetPasswordLogFace temp : this.resetPasswordLog) {
					if (oldResetPasswordLog.equals(temp)) {
						if (temp != oldResetPasswordLog) {
							temp.setAccount((Account) null);
						}
						break;
					}
				}
				this.resetPasswordLog.remove(oldResetPasswordLog);
				oldResetPasswordLog.setAccount((Account) null);
			}
	}

	public void removeAllResetPasswordLog() {
		if (resetPasswordLog != null) {
			ResetPasswordLogFace oldResetPasswordLog;
			for (java.util.Iterator<ResetPasswordLogFace> iter = getIteratorResetPasswordLog(); iter.hasNext();) {
				oldResetPasswordLog = (ResetPasswordLogFace) iter.next();
				iter.remove();
				oldResetPasswordLog.setAccount((Account) null);
			}
			resetPasswordLog.clear();
		}
	}
}
