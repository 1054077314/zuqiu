
package com.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;

/**
 * 鍒嗛〉宸ュ叿绫?
 */
public class PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	//鎬昏褰曟暟
	private long total;
	//姣忛〉璁板綍鏁?
	private int pageSize;
	//鎬婚〉鏁?
	private long totalPage;
	//褰撳墠椤垫暟
	private int currPage;
	//鍒楄〃鏁版嵁
	private List<?> list;
	
	/**
	 * 鍒嗛〉
	 * @param list        鍒楄〃鏁版嵁
	 * @param totalCount  鎬昏褰曟暟
	 * @param pageSize    姣忛〉璁板綍鏁?
	 * @param currPage    褰撳墠椤垫暟
	 */
	public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
		this.list = list;
		this.total = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}

	/**
	 * 鍒嗛〉
	 */
	public PageUtils(Page<?> page) {
		this.list = page.getRecords();
		this.total = page.getTotal();
		this.pageSize = page.getSize();
		this.currPage = page.getCurrent();
		this.totalPage = page.getPages();
	}
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	
}

