package com.sm.lzd.model.member;

import com.sun.rowset.CachedRowSetImpl;

public class ShowByPage {
	int pageSize = 10;  //ÿҳ��ʾ�ļ�¼��
	int pageAllCount = 0;  //��ҳ�����ҳ��
	int showPage = 1;  //��ǰ��ʾҳ
	StringBuffer presentPageResult;  //��ʾ��ǰҳ����

	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageAllCount() {
		return pageAllCount;
	}
	public void setPageAllCount(int pageAllCount) {
		this.pageAllCount = pageAllCount;
	}
	public int getShowPage() {
		return showPage;
	}
	public void setShowPage(int showPage) {
		this.showPage = showPage;
	}
	public StringBuffer getPresentPageResult() {
		return presentPageResult;
	}
	public void setPresentPageResult(StringBuffer presentPageResult) {
		this.presentPageResult = presentPageResult;
	}
}
