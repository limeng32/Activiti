package org.activiti.myExplorer.persist;

import java.io.Serializable;

import org.activiti.engine.repository.Model;
import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
@TableMapperAnnotation(tableName = "ACT_RE_MODEL")
public class ActReModel extends PojoSupport<ActReModel> implements Serializable {

	public ActReModel() {

	}

	public ActReModel(Model model) {

		this.id = model.getId();

		this.name = model.getName();

		this.key = model.getKey();

		this.category = model.getCategory();

		this.createTime = model.getCreateTime();

		this.lastUpdateTime = model.getLastUpdateTime();

		this.version = model.getVersion();

		this.metaInfo = model.getMetaInfo();

		this.deploymentId = model.getDeploymentId();

		this.tenantId = model.getTenantId();

	}

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
	private java.lang.String tenantId;

	private java.util.Collection<MyBusinessModel> myBusinessModel;

	public java.util.Collection<MyBusinessModel> getMyBusinessModel() {
		if (myBusinessModel == null)
			myBusinessModel = new java.util.LinkedHashSet<MyBusinessModel>();
		return myBusinessModel;
	}

	public java.util.Iterator<MyBusinessModel> getIteratorMyBusinessModel() {
		if (myBusinessModel == null)
			myBusinessModel = new java.util.LinkedHashSet<MyBusinessModel>();
		return myBusinessModel.iterator();
	}

	public void setMyBusinessModel(java.util.Collection<MyBusinessModel> newMyBusinessModel) {
		removeAllMyBusinessModel();
		for (java.util.Iterator<MyBusinessModel> iter = newMyBusinessModel.iterator(); iter.hasNext();)
			addMyBusinessModel((MyBusinessModel) iter.next());
	}

	public void addMyBusinessModel(MyBusinessModel newMyBusinessModel) {
		if (newMyBusinessModel == null)
			return;
		if (this.myBusinessModel == null)
			this.myBusinessModel = new java.util.LinkedHashSet<MyBusinessModel>();
		if (!this.myBusinessModel.contains(newMyBusinessModel)) {
			this.myBusinessModel.add(newMyBusinessModel);
			newMyBusinessModel.setActReModel(this);
		} else {
			for (MyBusinessModel temp : this.myBusinessModel) {
				if (newMyBusinessModel.equals(temp)) {
					if (temp != newMyBusinessModel) {
						removeMyBusinessModel(temp);
						this.myBusinessModel.add(newMyBusinessModel);
						newMyBusinessModel.setActReModel(this);
					}
					break;
				}
			}
		}
	}

	public void removeMyBusinessModel(MyBusinessModel oldMyBusinessModel) {
		if (oldMyBusinessModel == null)
			return;
		if (this.myBusinessModel != null && this.myBusinessModel.contains(oldMyBusinessModel)) {
			for (MyBusinessModel temp : this.myBusinessModel) {
				if (oldMyBusinessModel.equals(temp)) {
					if (temp != oldMyBusinessModel) {
						temp.setActReModel((ActReModel) null);
					}
					break;
				}
			}
			this.myBusinessModel.remove(oldMyBusinessModel);
			oldMyBusinessModel.setActReModel((ActReModel) null);
		}
	}

	public void removeAllMyBusinessModel() {
		if (myBusinessModel != null) {
			MyBusinessModel oldMyBusinessModel;
			for (java.util.Iterator<MyBusinessModel> iter = getIteratorMyBusinessModel(); iter.hasNext();) {
				oldMyBusinessModel = (MyBusinessModel) iter.next();
				iter.remove();
				oldMyBusinessModel.setActReModel((ActReModel) null);
			}
			myBusinessModel.clear();
		}
	}

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
