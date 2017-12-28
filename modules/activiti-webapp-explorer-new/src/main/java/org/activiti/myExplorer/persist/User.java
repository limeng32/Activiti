package org.activiti.myExplorer.persist;

import java.io.Serializable;

import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;

@TableMapperAnnotation(tableName = "USER")
public class User extends PojoSupport<User> implements Serializable {

	private static final long serialVersionUID = 1L;

	@FieldMapperAnnotation(dbFieldName = "ID", jdbcType = JdbcType.INTEGER, isUniqueKey = true)
	private Integer id;

	@FieldMapperAnnotation(dbFieldName = "NAME", jdbcType = JdbcType.VARCHAR)
	private String name = "wfadmin";

	@FieldMapperAnnotation(dbFieldName = "AVATAR", jdbcType = JdbcType.VARCHAR)
	private String avatar = "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png";

	@FieldMapperAnnotation(dbFieldName = "USERID", jdbcType = JdbcType.VARCHAR)
	private String userid = "00000001";

	@FieldMapperAnnotation(dbFieldName = "NOTIFYCOUNT", jdbcType = JdbcType.INTEGER)
	private Integer notifyCount = 12;

	private java.util.Collection<ProcessReturn> processReturn;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getNotifyCount() {
		return notifyCount;
	}

	public void setNotifyCount(Integer notifyCount) {
		this.notifyCount = notifyCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.util.Collection<ProcessReturn> getProcessReturn() {
		if (processReturn == null)
			processReturn = new java.util.LinkedHashSet<ProcessReturn>();
		return processReturn;
	}

	public java.util.Iterator<ProcessReturn> getIteratorProcessReturn() {
		if (processReturn == null)
			processReturn = new java.util.LinkedHashSet<ProcessReturn>();
		return processReturn.iterator();
	}

	public void setProcessReturn(java.util.Collection<ProcessReturn> newProcessReturn) {
		removeAllProcessReturn();
		for (java.util.Iterator<ProcessReturn> iter = newProcessReturn.iterator(); iter.hasNext();)
			addProcessReturn((ProcessReturn) iter.next());
	}

	public void addProcessReturn(ProcessReturn newProcessReturn) {
		if (newProcessReturn == null)
			return;
		if (this.processReturn == null) {
			this.processReturn = new java.util.LinkedHashSet<ProcessReturn>();
		}
		if (!this.processReturn.contains(newProcessReturn)) {
			this.processReturn.add(newProcessReturn);
			newProcessReturn.setOwner(this);
		} else {
			for (ProcessReturn temp : this.processReturn) {
				if (newProcessReturn.equals(temp)) {
					if (temp != newProcessReturn) {
						removeProcessReturn(temp);
						this.processReturn.add(newProcessReturn);
						newProcessReturn.setOwner(this);
					}
					break;
				}
			}
		}
	}

	public void removeProcessReturn(ProcessReturn oldProcessReturn) {
		if (oldProcessReturn == null)
			return;
		if (this.processReturn != null && this.processReturn.contains(oldProcessReturn)) {
			for (ProcessReturn temp : this.processReturn) {
				if (oldProcessReturn.equals(temp)) {
					if (temp != oldProcessReturn) {
						temp.setOwner(null);
					}
					break;
				}
			}
			this.processReturn.remove(oldProcessReturn);
			oldProcessReturn.setOwner(null);
		}
	}

	public void removeAllProcessReturn() {
		if (processReturn != null) {
			ProcessReturn oldProcessReturn;
			for (java.util.Iterator<ProcessReturn> iter = getIteratorProcessReturn(); iter.hasNext();) {
				oldProcessReturn = (ProcessReturn) iter.next();
				iter.remove();
				oldProcessReturn.setOwner(null);
			}
			processReturn.clear();
		}
	}
}
