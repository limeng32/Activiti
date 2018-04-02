package org.activiti.account.persist;

import java.io.Serializable;

import org.activiti.account.face.AccountRoleFace;
import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;

@TableMapperAnnotation(tableName = "accountRole")
public class AccountRole extends PojoSupport<AccountRole> implements Serializable, AccountRoleFace {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键，以UUID方式保存
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "accountrole_id", jdbcType = JdbcType.VARCHAR, isUniqueKey = true)
	private String id;

	@FieldMapperAnnotation(dbFieldName = "account_id", jdbcType = JdbcType.VARCHAR, dbAssociationUniqueKey = "account_id")
	private Account account;

	@FieldMapperAnnotation(dbFieldName = "role_id", jdbcType = JdbcType.VARCHAR, dbAssociationUniqueKey = "role_id")
	private Role role;

	public Account getAccount() {
		return account;
	}

	public Role getRole() {
		return role;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setAccount(Account newAccount) {
		if (this.account == null || this.account != newAccount) {
			if (this.account != null) {
				Account oldAccount = this.account;
				this.account = null;
				oldAccount.removeAccountRole(this);
			}
			if (newAccount != null) {
				this.account = newAccount;
				this.account.addAccountRole(this);
			}
		}
	}

	@Override
	public void setRole(Role newRole) {
		if (this.role == null || this.role != newRole) {
			if (this.role != null) {
				Role oldRole = this.role;
				this.role = null;
				oldRole.removeAccountRole(this);
			}
			if (newRole != null) {
				this.role = newRole;
				this.role.addAccountRole(this);
			}
		}
	}

	@Override
	public String getId() {
		return id;
	}

}
