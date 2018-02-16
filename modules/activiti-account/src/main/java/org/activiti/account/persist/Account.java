package org.activiti.account.persist;

import java.io.Serializable;

import org.activiti.account.face.AccountFace;
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

	@Override
	public Object getId() {
		return null;
	}

}
