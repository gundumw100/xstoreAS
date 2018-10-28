package com.base.model;

/**
 * 分页，默认第一页，每页10条
 * @author pythoner
 *
 */
public class Pagination {

	public int page=1;
	public int size=10;
	
	public void reset(){
		page=1;
		size=10;
	}
	
	public int nextPage(){
		return page++;
	}
}
