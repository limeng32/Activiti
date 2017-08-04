package org.activiti.myExplorer.persist;

import java.io.Serializable;

import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;

@TableMapperAnnotation(tableName = "ACT_RE_MODEL")
public class ActReModel extends PojoSupport<ActReModel> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "ID_", jdbcType = JdbcType.VARCHAR, isUniqueKey = true)
	private java.lang.String id;

	@FieldMapperAnnotation(dbFieldName = "REV_", jdbcType = JdbcType.INTEGER)
	private Integer rev;

	@FieldMapperAnnotation(dbFieldName = "NAME_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String name;

	@FieldMapperAnnotation(dbFieldName = "KEY_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String key;

	@FieldMapperAnnotation(dbFieldName = "CATEGORY_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String category;

	@FieldMapperAnnotation(dbFieldName = "CREATE_TIME_", jdbcType = JdbcType.TIMESTAMP)
	private java.util.Date createTime;

	@FieldMapperAnnotation(dbFieldName = "LAST_UPDATE_TIME_", jdbcType = JdbcType.TIMESTAMP)
	private java.util.Date lastUpdateTime;

	@FieldMapperAnnotation(dbFieldName = "VERSION_", jdbcType = JdbcType.INTEGER)
	private Integer version;

	@FieldMapperAnnotation(dbFieldName = "META_INFO_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String metaInfo;

	@FieldMapperAnnotation(dbFieldName = "DEPLOYMENT_ID_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String deploymentId;

	@FieldMapperAnnotation(dbFieldName = "EDITOR_SOURCE_VALUE_ID_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String editorSourceValueId;

	@FieldMapperAnnotation(dbFieldName = "EDITOR_SOURCE_EXTRA_VALUE_ID_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String editorSourceExtraValueId;

	/* 默认为"" */
	@FieldMapperAnnotation(dbFieldName = "TENANT_ID_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String tenantId = "";

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public Integer getRev() {
		return rev;
	}

	public void setRev(Integer rev) {
		this.rev = rev;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getKey() {
		return key;
	}

	public void setKey(java.lang.String key) {
		this.key = key;
	}

	public java.lang.String getCategory() {
		return category;
	}

	public void setCategory(java.lang.String category) {
		this.category = category;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public java.util.Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(java.util.Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public java.lang.String getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(java.lang.String metaInfo) {
		this.metaInfo = metaInfo;
	}

	public java.lang.String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(java.lang.String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public java.lang.String getEditorSourceValueId() {
		return editorSourceValueId;
	}

	public void setEditorSourceValueId(java.lang.String editorSourceValueId) {
		this.editorSourceValueId = editorSourceValueId;
	}

	public java.lang.String getEditorSourceExtraValueId() {
		return editorSourceExtraValueId;
	}

	public void setEditorSourceExtraValueId(java.lang.String editorSourceExtraValueId) {
		this.editorSourceExtraValueId = editorSourceExtraValueId;
	}

	public java.lang.String getTenantId() {
		return tenantId;
	}

	public void setTenantId(java.lang.String tenantId) {
		this.tenantId = tenantId;
	}

}
