package org.activiti.myExplorer.persist;

import java.io.Serializable;

import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;

@TableMapperAnnotation(tableName = "MY_BUSINESS_PROCDEF")
public class MyBusinessProcdef extends PojoSupport<MyBusinessProcdef> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@FieldMapperAnnotation(dbFieldName = "ID", jdbcType = JdbcType.VARCHAR, isUniqueKey = true)
	private java.lang.String id;

	/**
	 * 对应业务的ID
	 */
	@FieldMapperAnnotation(dbFieldName = "BUSINESS_ID", jdbcType = JdbcType.VARCHAR)
	private java.lang.String businessId;

	@FieldMapperAnnotation(dbFieldName = "PRCODEF_ID", jdbcType = JdbcType.VARCHAR, dbAssociationUniqueKey = "_ID")
	private ActReProcdef actReProcdef;

	public ActReProcdef getActReProcdef() {
		return actReProcdef;
	}

	public void setActReProcdef(ActReProcdef newActReProcdef) {
		if (this.actReProcdef == null || !this.actReProcdef.equals(newActReProcdef)) {
			if (this.actReProcdef != null) {
				ActReProcdef oldActReProcdef = this.actReProcdef;
				this.actReProcdef = null;
				oldActReProcdef.removeMyBusinessProcdef(this);
			}
			if (newActReProcdef != null) {
				this.actReProcdef = newActReProcdef;
				this.actReProcdef.addMyBusinessProcdef(this);
			}
		}
	}

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(java.lang.String businessId) {
		this.businessId = businessId;
	}

}
