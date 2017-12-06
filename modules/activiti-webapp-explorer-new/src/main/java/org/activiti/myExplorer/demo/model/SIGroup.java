package org.activiti.myExplorer.demo.model;

import java.io.Serializable;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class SIGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	private String projectmanager = "projectmanager";

	private String tmo = "tmo";

	private String bfinance = "bfinance";

	private String major = "major";

	private String deputymajor = "deputymajor";

	private String manager = "manager";

	private String finance = "finance";

	private String dean = "dean";

	private String coDean = "co-dean";

	private String btmo = "btmo";

	private String leader = "leader";

	public String getProjectmanager() {
		return projectmanager;
	}

	public void setProjectmanager(String projectmanager) {
		this.projectmanager = projectmanager;
	}

	public String getTmo() {
		return tmo;
	}

	public void setTmo(String tmo) {
		this.tmo = tmo;
	}

	public String getBfinance() {
		return bfinance;
	}

	public void setBfinance(String bfinance) {
		this.bfinance = bfinance;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getDeputymajor() {
		return deputymajor;
	}

	public void setDeputymajor(String deputymajor) {
		this.deputymajor = deputymajor;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getFinance() {
		return finance;
	}

	public void setFinance(String finance) {
		this.finance = finance;
	}

	public String getDean() {
		return dean;
	}

	public void setDean(String dean) {
		this.dean = dean;
	}

	public String getCoDean() {
		return coDean;
	}

	public void setCoDean(String coDean) {
		this.coDean = coDean;
	}

	public String getBtmo() {
		return btmo;
	}

	public void setBtmo(String btmo) {
		this.btmo = btmo;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

}
