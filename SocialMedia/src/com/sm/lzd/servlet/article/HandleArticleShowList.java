package com.sm.lzd.servlet.article;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import com.sun.rowset.CachedRowSetImpl;
import com.sm.lzd.model.article.ShowByPage;
import com.sm.lzd.util.DbConn;
import com.sm.lzd.util.StringUtil;

public class HandleArticleShowList extends HttpServlet{
//	private CachedRowSetImpl rowSet = null;
	
	private CachedRowSet rowSet=null;

	public void init(ServletConfig config)throws ServletException{
		super.init(config);

	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		//�鿴���²���Ҫ��¼
		continueDoGet(request,response);		
	}

	private void continueDoGet(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(true);		
		ShowByPage showByPageBean = null;
		
		try {
			showByPageBean = (ShowByPage)session.getAttribute("showAllTitle");
			if(showByPageBean == null){
				showByPageBean = new ShowByPage();
				session.setAttribute("showAllTitle", showByPageBean);
			}
		} catch (Exception e) {
			showByPageBean = new ShowByPage();
			session.setAttribute("showAllTitle", showByPageBean);
		}
		
		showByPageBean.setPageSize(10);  //ÿҳ��ʾ10ƪ����
		
		String showPageString = StringUtil.xssEncode(request.getParameter("showPage"));
		int showPage = 1;  //��ʼĬ����ʾ��һҳ
		if(showPageString != null)  //��һ�δ���ҳ�ύ��ʱ��showPage����������
			showPage = Integer.parseInt(showPageString);

		if(showPage > showByPageBean.getPageAllCount()) 
			showPage = 1;
		else if(showPage < 1)
			showPage = showByPageBean.getPageAllCount();
		showByPageBean.setShowPage(showPage);
		
		//�������ݿ���в�ѯ�����б�
		Connection connection = DbConn.getConnection();
		try {
			PreparedStatement pStatement = connection.prepareStatement("select id,title from article");
			ResultSet resultSet = pStatement.executeQuery();
//			rowSet = new CachedRowSetImpl();
			try {
				rowSet=RowSetProvider.newFactory().createCachedRowSet();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rowSet.populate(resultSet);
			rowSet.last();
			int m = rowSet.getRow();  //������
			int n = showByPageBean.getPageSize();  //ÿҳ��ʾ��¼
			if(m % n == 0)
				showByPageBean.setPageAllCount(m / n);
			else 
				showByPageBean.setPageAllCount(m / n + 1);
			
			showByPageBean.setPresentPageResult(StringUtil.showArticleTitle(showPage, showByPageBean.getPageSize(), rowSet));
			
			resultSet.close();
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("article/index.jsp");
		dispatcher.forward(request, response);
		
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		doGet(request, response);		
	}
	
}
