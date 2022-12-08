package com.sm.lzd.servlet.article;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sm.lzd.model.member.Login;
import com.sm.lzd.util.DbConn;
import com.sm.lzd.util.StringUtil;

public class HandleArticleAdd extends HttpServlet{
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		HttpSession session = request.getSession(true);
		Login loginBean = (Login) session.getAttribute("login");
		
		if(loginBean == null){
			response.sendRedirect("login.jsp");
			return;
		}
		else
			continueDoPost(request,response);  
	}

	private void continueDoPost(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(true);
		Login loginBean = (Login) session.getAttribute("login");

		String id = loginBean.getId();  //��ȡ�ѵ��û�¼��id
	
		//��ȡ�ύ�����²���
		String newTitle = StringUtil.xssEncode(request.getParameter("title"));
		String newContent = StringUtil.xssEncode(request.getParameter("content"));
		
		if(StringUtil.isNotEmpty(newTitle)){
			try {
				Connection connection = DbConn.getConnection();
				//����id�Ѿ�����Ϊ����������������Ͳ��ù���
				PreparedStatement pStatement = connection.prepareStatement("insert into article(title,author,content) values(?,?,?)");
				pStatement.setString(1, newTitle);
				pStatement.setString(2, id);
				pStatement.setString(3, newContent);
				
				pStatement.executeUpdate();
				
				pStatement.close();
				connection.close();
			} catch (Exception e) {
				
			}
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("ArticleShowList");
		dispatcher.forward(request, response);	
	}
}
