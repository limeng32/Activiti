package org.activiti.myExplorer.model;

import java.util.Collection;

public class PageInfo<T> {

	private int pageNo;

	private int maxPageNum;

	private int totalCount;

	private Collection<T> pageItems;

	private int pageSize;

	public PageInfo(Collection<T> items, int pageNo, int maxPageNum, int totalCount, int pageSize) {
		pageItems = items;
		this.pageNo = pageNo;
		this.maxPageNum = maxPageNum;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getMaxPageNum() {
		return maxPageNum;
	}

	public void setMaxPageNum(int maxPageNum) {
		this.maxPageNum = maxPageNum;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public Collection<T> getPageItems() {
		return pageItems;
	}

	public void setPageItems(Collection<T> pageItems) {
		this.pageItems = pageItems;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
