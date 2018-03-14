package com.handge.housingfund.database.entities;

import java.io.Serializable;
import java.util.List;

public class PageResults<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6945608323400657463L;

	// 下一页
	private int nextPageNo;

	// 当前页
	private int currentPage;

	// 每页个个数
	private int pageSize;

	// 总条数
	private int totalCount;

	// 总页数
	private int pageCount;

	// 记录
	private List<T> results;

	private String tag;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
       
		this.pageCount = pageCount;
	}

	public int getPageNo() {
		if (nextPageNo <= 0) {
			return 1;
		} else {
			return nextPageNo > pageCount ? pageCount : nextPageNo;
		}
	}

	public void setPageNo(int pageNo) {
       
		this.nextPageNo = pageNo;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
       
		this.results = results;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
       
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
       
		this.pageSize = pageSize <= 0 ? 10 : pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
       
		this.totalCount = totalCount;
	}

	public void resetPageNo() {
		nextPageNo = currentPage + 1;
		pageCount = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
	}

}