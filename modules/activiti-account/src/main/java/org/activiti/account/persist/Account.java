package org.activiti.account.persist;

import java.io.Serializable;

import org.activiti.account.face.AccountFace;
import org.activiti.myExplorer.pojoHelper.PojoSupport;

import indi.mybatis.flying.annotations.TableMapperAnnotation;

@TableMapperAnnotation(tableName = "account")
public class Account extends PojoSupport<Account> implements Serializable, AccountFace {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getId() {
		return null;
	}

}
