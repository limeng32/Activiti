package org.activiti.account.persist;

import java.io.Serializable;
import java.util.Collection;

import org.activiti.account.face.AccountBucketFace;
import org.activiti.account.face.AccountFace;
import org.activiti.account.face.AccountRoleFace;
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
}
