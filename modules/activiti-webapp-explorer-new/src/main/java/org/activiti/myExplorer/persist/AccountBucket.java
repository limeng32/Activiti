package org.activiti.myExplorer.persist;

import java.io.Serializable;

import org.activiti.account.face.AccountBucketFace;
import org.activiti.account.persist.Account;
import org.activiti.myExplorer.pojoHelper.PojoSupport;
import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.TableMapperAnnotation;

@TableMapperAnnotation(tableName = "accountBucket")
public class AccountBucket extends PojoSupport<AccountBucket> implements Serializable, AccountBucketFace {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键，以UUID方式保存
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "accountBucket_id", jdbcType = JdbcType.VARCHAR, isUniqueKey = true)
	private String id;

	/**
	 * 用户昵称
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "nickname", jdbcType = JdbcType.VARCHAR)
	private java.lang.String nickname;

	/**
	 * 头像原始图片地址
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "originalPortrait", jdbcType = JdbcType.VARCHAR)
	private java.lang.String originalPortrait;

	/**
	 * 头像图片修饰参数
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "portraitModify", jdbcType = JdbcType.VARCHAR)
	private java.lang.String portraitModify;

	/**
	 * 头像原始图片地址的临时存放处
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "tempOriginalPortrait", jdbcType = JdbcType.VARCHAR)
	private java.lang.String tempOriginalPortrait;

	/**
	 * 用户上传附件总大小
	 * 
	 */
	@FieldMapperAnnotation(dbFieldName = "uploadedSize", jdbcType = JdbcType.BIGINT)
	private Long uploadedSize;

	@FieldMapperAnnotation(dbFieldName = "account_id", jdbcType = JdbcType.VARCHAR, dbAssociationUniqueKey = "account_id")
	private Account account;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public java.lang.String getNickname() {
		return nickname;
	}

	public void setNickname(java.lang.String nickname) {
		this.nickname = nickname;
	}

	public java.lang.String getOriginalPortrait() {
		return originalPortrait;
	}

	public void setOriginalPortrait(java.lang.String originalPortrait) {
		this.originalPortrait = originalPortrait;
	}

	public java.lang.String getPortraitModify() {
		return portraitModify;
	}

	public void setPortraitModify(java.lang.String portraitModify) {
		this.portraitModify = portraitModify;
	}

	public java.lang.String getTempOriginalPortrait() {
		return tempOriginalPortrait;
	}

	public void setTempOriginalPortrait(java.lang.String tempOriginalPortrait) {
		this.tempOriginalPortrait = tempOriginalPortrait;
	}

	public Long getUploadedSize() {
		return uploadedSize;
	}

	public void setUploadedSize(Long uploadedSize) {
		this.uploadedSize = uploadedSize;
	}

	public Account getAccount() {
		return account;
	}

	@Override
	public void setAccount(Account newAccount) {
		if (this.account == null || this.account != newAccount) {
			if (this.account != null) {
				Account oldAccount = this.account;
				this.account = null;
				oldAccount.removeAccountBucket(this);
			}
			if (newAccount != null) {
				this.account = newAccount;
				this.account.addAccountBucket(this);
			}
		}
	}

	public String getPortrait() {
		return originalPortrait + "@" + portraitModify + "_160w_160h";
	}

	public String getSmallPortrait() {
		return originalPortrait + "@" + portraitModify + "_30w_30h";
	}

	public String getPx40Portrait() {
		return originalPortrait + "@" + portraitModify + "_40w_40h";
	}
}
