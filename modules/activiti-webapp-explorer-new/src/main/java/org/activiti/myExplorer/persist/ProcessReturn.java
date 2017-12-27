package org.activiti.myExplorer.persist;

public class ProcessReturn {

	public ProcessReturn() {

	}

	public ProcessReturn(String id, User owner, String title) {
		this.id = id;
		this.owner = owner;
		this.title = title;
	}

	private String id;

	private User owner;

	private String title;

	private String logo = "https://gw.alipayobjects.com/zos/rmsportal/zOsKZmFRdUtvpqCImOVY.png";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
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
