package org.activiti.account.condition;

import java.util.Date;

import org.activiti.account.persist.ResetPasswordLog;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.Limitable;
import indi.mybatis.flying.models.Sortable;
import indi.mybatis.flying.statics.ConditionType;

public class ResetPasswordLogCondition extends ResetPasswordLog implements Conditionable {
	private static final long serialVersionUID = 1L;

	private Limitable limiter;

	private Sortable sorter;

	@Override
	public Limitable getLimiter() {
		return limiter;
	}

	@Override
	public Sortable getSorter() {
		return sorter;
	}

	@Override
	public void setLimiter(Limitable arg0) {
		this.limiter = arg0;
	}

	@Override
	public void setSorter(Sortable arg0) {
		this.sorter = arg0;
	}

	@ConditionMapperAnnotation(dbFieldName = "emailTime", conditionType = ConditionType.GreaterThan)
	private Date emailTimeGreaterThan;

	@ConditionMapperAnnotation(dbFieldName = "emailTime", conditionType = ConditionType.LessOrEqual)
	private Date emailTimeLessOrEqual;

	public Date getEmailTimeGreaterThan() {
		return emailTimeGreaterThan;
	}

	public void setEmailTimeGreaterThan(Date emailTimeGreaterThan) {
		this.emailTimeGreaterThan = emailTimeGreaterThan;
	}

	public Date getEmailTimeLessOrEqual() {
		return emailTimeLessOrEqual;
	}

	public void setEmailTimeLessOrEqual(Date emailTimeLessOrEqual) {
		this.emailTimeLessOrEqual = emailTimeLessOrEqual;
	}
}
