package org.activiti.myExplorer.persist;

import java.io.Serializable;

import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;

@TableMapperAnnotation(tableName = "ACT_RE_PROCDEF")
public class ActReProcdef extends PojoSupport<ActReProcdef> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "ID_", jdbcType = JdbcType.VARCHAR, isUniqueKey = true)
	private java.lang.String id;

	@FieldMapperAnnotation(dbFieldName = "REV_", jdbcType = JdbcType.INTEGER)
	private Integer rev;

	@FieldMapperAnnotation(dbFieldName = "CATEGORY_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String category;

	@FieldMapperAnnotation(dbFieldName = "NAME_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String name;

	@FieldMapperAnnotation(dbFieldName = "KEY_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String key;

	@FieldMapperAnnotation(dbFieldName = "VERSION_", jdbcType = JdbcType.INTEGER)
	private Integer version;

	@FieldMapperAnnotation(dbFieldName = "DEPLOYMENT_ID_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String deploymentId;

	@FieldMapperAnnotation(dbFieldName = "RESOURCE_NAME_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String resourceName;

	@FieldMapperAnnotation(dbFieldName = "DGRM_RESOURCE_NAME_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String dgrmResourceName;

	@FieldMapperAnnotation(dbFieldName = "DESCRIPTION_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String description;

	@FieldMapperAnnotation(dbFieldName = "HAS_START_FORM_KEY_", jdbcType = JdbcType.BOOLEAN)
	private Boolean hasStartFormKey;

	@FieldMapperAnnotation(dbFieldName = "HAS_GRAPHICAL_NOTATION_", jdbcType = JdbcType.BOOLEAN)
	private Boolean hasGraphicalNotation;

	@FieldMapperAnnotation(dbFieldName = "SUSPENSION_STATE_", jdbcType = JdbcType.INTEGER)
	private Integer suspensionState;

	/* 默认为"" */
	@FieldMapperAnnotation(dbFieldName = "TENANT_ID_", jdbcType = JdbcType.VARCHAR)
	private java.lang.String tenantId = "";

	private java.util.Collection<MyBusinessProcdef> myBusinessProcdef;

	public java.util.Collection<MyBusinessProcdef> getMyBusinessProcdef() {
		if (myBusinessProcdef == null)
			myBusinessProcdef = new java.util.LinkedHashSet<MyBusinessProcdef>();
		return myBusinessProcdef;
	}

	public java.util.Iterator<MyBusinessProcdef> getIteratorMyBusinessProcdef() {
		if (myBusinessProcdef == null)
			myBusinessProcdef = new java.util.LinkedHashSet<MyBusinessProcdef>();
		return myBusinessProcdef.iterator();
	}

	public void setMyBusinessProcdef(java.util.Collection<MyBusinessProcdef> newMyBusinessProcdef) {
		removeAllMyBusinessProcdef();
		for (java.util.Iterator<MyBusinessProcdef> iter = newMyBusinessProcdef.iterator(); iter.hasNext();)
			addMyBusinessProcdef((MyBusinessProcdef) iter.next());
	}

	public void addMyBusinessProcdef(MyBusinessProcdef newMyBusinessProcdef) {
		if (newMyBusinessProcdef == null)
			return;
		if (this.myBusinessProcdef == null)
			this.myBusinessProcdef = new java.util.LinkedHashSet<MyBusinessProcdef>();
		if (!this.myBusinessProcdef.contains(newMyBusinessProcdef)) {
			this.myBusinessProcdef.add(newMyBusinessProcdef);
			newMyBusinessProcdef.setActReProcdef(this);
		} else {
			for (MyBusinessProcdef temp : this.myBusinessProcdef) {
				if (newMyBusinessProcdef.equals(temp)) {
					if (temp != newMyBusinessProcdef) {
						removeMyBusinessProcdef(temp);
						this.myBusinessProcdef.add(newMyBusinessProcdef);
						newMyBusinessProcdef.setActReProcdef(this);
					}
					break;
				}
			}
		}
	}

	public void removeMyBusinessProcdef(MyBusinessProcdef oldMyBusinessProcdef) {
		if (oldMyBusinessProcdef == null)
			return;
		if (this.myBusinessProcdef != null)
			if (this.myBusinessProcdef.contains(oldMyBusinessProcdef)) {
				for (MyBusinessProcdef temp : this.myBusinessProcdef) {
					if (oldMyBusinessProcdef.equals(temp)) {
						if (temp != oldMyBusinessProcdef) {
							temp.setActReProcdef((ActReProcdef) null);
						}
						break;
					}
				}
				this.myBusinessProcdef.remove(oldMyBusinessProcdef);
				oldMyBusinessProcdef.setActReProcdef((ActReProcdef) null);
			}
	}

	public void removeAllMyBusinessProcdef() {
		if (myBusinessProcdef != null) {
			MyBusinessProcdef oldMyBusinessProcdef;
			for (java.util.Iterator<MyBusinessProcdef> iter = getIteratorMyBusinessProcdef(); iter.hasNext();) {
				oldMyBusinessProcdef = (MyBusinessProcdef) iter.next();
				iter.remove();
				oldMyBusinessProcdef.setActReProcdef((ActReProcdef) null);
			}
			myBusinessProcdef.clear();
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

	public java.lang.String getCategory() {
		return category;
	}

	public void setCategory(java.lang.String category) {
		this.category = category;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public java.lang.String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(java.lang.String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public java.lang.String getResourceName() {
		return resourceName;
	}

	public void setResourceName(java.lang.String resourceName) {
		this.resourceName = resourceName;
	}

	public java.lang.String getDgrmResourceName() {
		return dgrmResourceName;
	}

	public void setDgrmResourceName(java.lang.String dgrmResourceName) {
		this.dgrmResourceName = dgrmResourceName;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public Boolean getHasStartFormKey() {
		return hasStartFormKey;
	}

	public void setHasStartFormKey(Boolean hasStartFormKey) {
		this.hasStartFormKey = hasStartFormKey;
	}

	public Boolean getHasGraphicalNotation() {
		return hasGraphicalNotation;
	}

	public void setHasGraphicalNotation(Boolean hasGraphicalNotation) {
		this.hasGraphicalNotation = hasGraphicalNotation;
	}

	public Integer getSuspensionState() {
		return suspensionState;
	}

	public void setSuspensionState(Integer suspensionState) {
		this.suspensionState = suspensionState;
	}

	public java.lang.String getTenantId() {
		return tenantId;
	}

	public void setTenantId(java.lang.String tenantId) {
		this.tenantId = tenantId;
	}

}
