package org.activiti.account.persist;

import java.io.Serializable;
import java.util.Collection;

import org.activiti.account.face.AccountRoleFace;
import org.activiti.account.face.RoleFace;
import org.activiti.account.statics.RoleStatus;
import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import com.alibaba.fastjson.annotation.JSONField;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;
import indi.mybatis.flying.statics.OpLockType;

@TableMapperAnnotation(tableName = "role")
public class Role extends PojoSupport<Role> implements Serializable, RoleFace {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键，以UUID方式保存
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "role_id", jdbcType = JdbcType.VARCHAR, isUniqueKey = true)
	private java.lang.String id;

	/**
	 * 角色名
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "name", jdbcType = JdbcType.VARCHAR)
	private java.lang.String name;

	/**
	 * 角色值，对应枚举类变量
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "value", jdbcType = JdbcType.CHAR)
	private RoleStatus value;

	/**
	 * 乐观锁
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "opLock", jdbcType = JdbcType.INTEGER, opLockType = OpLockType.Version)
	@JSONField(serialize = false)
	private Integer opLock;

	private Collection<AccountRoleFace> accountRole;

	@Override
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public RoleStatus getValue() {
		return value;
	}

	public void setValue(RoleStatus value) {
		this.value = value;
	}

	public void setId(java.lang.String id) {
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
			newAccountRole.setRole(this);
		} else {
			for (AccountRoleFace temp : this.accountRole) {
				if (newAccountRole.equals(temp)) {
					if (temp != newAccountRole) {
						removeAccountRole(temp);
						this.accountRole.add(newAccountRole);
						newAccountRole.setRole(this);
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
							temp.setRole((Role) null);
						}
						break;
					}
				}
				this.accountRole.remove(oldAccountRole);
				oldAccountRole.setRole((Role) null);
			}
	}

	public void removeAllAccountRole() {
		if (accountRole != null) {
			AccountRoleFace oldAccountRole;
			for (java.util.Iterator<AccountRoleFace> iter = getIteratorAccountRole(); iter.hasNext();) {
				oldAccountRole = (AccountRoleFace) iter.next();
				iter.remove();
				oldAccountRole.setRole((Role) null);
			}
			accountRole.clear();
		}
	}
}
