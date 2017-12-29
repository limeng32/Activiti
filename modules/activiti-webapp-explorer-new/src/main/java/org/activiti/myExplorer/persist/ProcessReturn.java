package org.activiti.myExplorer.persist;

import java.io.Serializable;

import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;

@TableMapperAnnotation(tableName = "PROCESSRETURN")
public class ProcessReturn extends PojoSupport<ProcessReturn> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ProcessReturn() {

	}

	public ProcessReturn(String id, String title) {
		this.id = id;
		this.title = title;
	}

	public ProcessReturn(String id, User owner, String title) {
		this.id = id;
		this.owner = owner;
		this.title = title;
	}

	@FieldMapperAnnotation(dbFieldName = "ID", jdbcType = JdbcType.VARCHAR, isUniqueKey = true)
	private String id;

	@FieldMapperAnnotation(dbFieldName = "OWNER_ID", jdbcType = JdbcType.INTEGER, dbAssociationUniqueKey = "ID")
	private User owner;

	@FieldMapperAnnotation(dbFieldName = "TITLE", jdbcType = JdbcType.VARCHAR)
	private String title;

	@FieldMapperAnnotation(dbFieldName = "LOGO", jdbcType = JdbcType.VARCHAR)
	private String logo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User newOwner) {
		if (this.owner == null || !this.owner.equals(newOwner)) {
			if (this.owner != null) {
				User oldOwner = this.owner;
				this.owner = null;
				oldOwner.removeProcessReturn(this);
			}
			if (newOwner != null) {
				this.owner = newOwner;
				this.owner.addProcessReturn(this);
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}
