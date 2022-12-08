package com.sm.lzd.servlet.article;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sm.lzd.model.article.Article;
import com.sm.lzd.model.article.ErrorMessage;
import com.sm.lzd.model.member.Login;
import com.sm.lzd.util.DbConn;
import com.sm.lzd.util.StringUtil;

public class HandleArticleModify extends HttpServlet{
	
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
	}
	/**
	 * get ���󽫴��޸ĵ���Ϣ���͵� article/modifyArticle.jsp �����û��޸�
	 * */
	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(true);
		Login loginBean = (Login) session.getAttribute("login");
		
		if(loginBean == null){
			response.sendRedirect("login.jsp");
			return;
		}
		else{
			String author = request.getParameter("author");
			String id = loginBean.getId();
			//�����¼�û������������Ƿ���ͬһ��
			if(author.equals(id))
				continueDoGet(request,response);  			
			else {
				ErrorMessage errorMessage = new ErrorMessage();
				request.setAttribute("error", errorMessage);
				errorMessage.setBackNews("��û��Ȩ���޸ĸ�����");
				
				RequestDispatcher dispatcher = request.getRequestDispatcher("article/error.jsp");
				dispatcher.forward(request, response);	
			}
		}			
	}

	private void continueDoGet(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
	
		//��ȡ���޸����µ� id
		String articleId = StringUtil.xssEncode(request.getParameter("articleId"));
		Article article = new Article();
		request.setAttribute("toModifyArticle", article);
		
		Connection connection = DbConn.getConnection();
		try {
			PreparedStatement pStatement = connection.prepareStatement("select id,title,author,content from article where id=?");
			pStatement.setString(1, articleId);
			ResultSet resultSet = pStatement.executeQuery();
			if(resultSet.next()){
				article.setId(resultSet.getInt(1));
				article.setTitle(resultSet.getString(2));
				article.setAuthor(resultSet.getString(3));
				article.setContent(resultSet.getString(4));
			}
			pStatement.close();
			connection.close();
		} catch (Exception e) {

		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("article/modifyArticle.jsp");
		dispatcher.forward(request, response);	
	}

	/**
	 * ��ȡ�û��޸ĺ����Ϣ���������ݿ�
	 * */
	public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(true);
		Login loginBean = (Login) session.getAttribute("login");
		
		if(loginBean == null)
			response.sendRedirect("login.jsp");
		else{
			String author = request.getParameter("author");
			String id = loginBean.getId();
			//�����¼�û������������Ƿ���ͬһ��
			if(author.equals(id))
				continueDoPost(request,response);  			
			else {
				ErrorMessage errorMessage = new ErrorMessage();
				request.setAttribute("error", errorMessage);
				errorMessage.setBackNews("��û��Ȩ���޸ĸ�����");
				
				RequestDispatcher dispatcher = request.getRequestDispatcher("article/error.jsp");
				dispatcher.forward(request, response);	
			}
		}			
	}

	private void continueDoPost(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
	
		//��ȡ���޸����µ� id,title,author,content
		String articleId = StringUtil.xssEncode(request.getParameter("articleId"));
		String title = StringUtil.xssEncode(request.getParameter("title"));
		String content = StringUtil.xssEncode(request.getParameter("content"));
		
		try {
			Connection connection = DbConn.getConnection();
			//�������ݿ���Ϣ
			PreparedStatement pStatement = connection.prepareStatement("update article set title=?,content=? where id=?");
			pStatement.setString(1, title);
			pStatement.setString(2, content);
			pStatement.setString(3, articleId);
	
			pStatement.executeUpdate();
			
			pStatement.close();
			connection.close();
		} catch (Exception e) {
			
		}
			
		RequestDispatcher dispatcher = request.getRequestDispatcher("ArticleShowList");
		dispatcher.forward(request, response);	
	}
}


