package org.activiti.myExplorer.persist;

import java.io.Serializable;

import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;

@TableMapperAnnotation(tableName = "MY_BUSINESS_MODEL")
public class MyBusinessModel extends PojoSupport<MyBusinessModel> implements Serializable {

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

	@FieldMapperAnnotation(dbFieldName = "MODEL_ID", jdbcType = JdbcType.VARCHAR, dbAssociationUniqueKey = "ID_")
	private ActReModel actReModel;

	public ActReModel getActReModel() {
		return actReModel;
	}

	public void setActReModel(ActReModel newActReModel) {
		if (this.actReModel == null || !this.actReModel.equals(newActReModel)) {
			if (this.actReModel != null) {
				ActReModel oldActReModel = this.actReModel;
				this.actReModel = null;
				oldActReModel.removeMyBusinessModel(this);
			}
			if (newActReModel != null) {
				this.actReModel = newActReModel;
				this.actReModel.addMyBusinessModel(this);
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
