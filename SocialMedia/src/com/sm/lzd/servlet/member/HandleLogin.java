package com.sm.lzd.servlet.member;

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

import com.sm.lzd.model.member.Login;
import com.sm.lzd.util.DbConn;
import com.sm.lzd.util.StringUtil;

public class HandleLogin extends HttpServlet{
	private String backNews = "";  //��¼״̬������Ϣ
	
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(true);
		
		//��ȡ��֤��
		String validateCode = StringUtil.xssEncode(request.getParameter("validateCode").trim());
		Object checkcode = session.getAttribute("checkcode");
		//���������֤���е�Сд��ĸת���ɴ�д���ٺ���֤������ʱ������session�е��ַ����Ƚ�		
		if(checkcode != null && checkcode.equals(StringUtil.convertToCapitalString(validateCode))){
			session.removeAttribute("checkcode");
			continueDoPost(request,response);
		}
		else{			
			response.sendRedirect("login.jsp");
			return;
		}
			
	}
	
	private void continueDoPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		Login loginBean = null;
		HttpSession session = request.getSession(true);
		try {
			loginBean = (Login) session.getAttribute("login");
			if(loginBean == null){
				loginBean = new Login();
				session.setAttribute("login", loginBean);
			}
		} catch (Exception e) {
			loginBean = new Login();
			session.setAttribute("login", loginBean);
		}
		
		//��ȡ��¼�Ĳ���
		String id = StringUtil.xssEncode(request.getParameter("id").trim());
		String password = StringUtil.xssEncode(request.getParameter("password").trim());
		
		boolean loginOk = loginBean.isLoginSuccess();
		
		if(loginOk&&(id.equals(loginBean.getId())&&(password.equals(loginBean.getPassword())) )){
			backNews = "�𾴵Ļ�Ա��"+id+",���Ѿ���¼�������ظ���¼��";
			loginBean.setBackNews(backNews);
		}
		else{
			boolean chkLength = id.length()>0 && password.length()>0;  //�ж��û����������Ƿ�Ϊ��
			if(chkLength){
				//�������ݿ���в�ѯ��֤��¼�Ƿ���ȷ
				Connection connection = DbConn.getConnection();
				try {
					PreparedStatement pStatement = connection.prepareStatement("select id from member where id=? and password =?");
					pStatement.setString(1, id);
					pStatement.setString(2, password);
					ResultSet resultSet = pStatement.executeQuery();

					if(resultSet.next()){
						backNews = "��¼�ɹ�";
						loginBean.setId(id);
						loginBean.setPassword(password);
						loginBean.setLoginSuccess(true);
						loginBean.setBackNews(backNews);
					}
					else{
						backNews = "��������û��������ڣ������벻ƥ��";
						loginBean.setId(id);
						loginBean.setLoginSuccess(false);
						loginBean.setBackNews(backNews);
					}
					connection.close();
				} catch (SQLException e) {
					backNews = "��������û��������ڣ������벻ƥ��";
					loginBean.setId(id);
					loginBean.setLoginSuccess(false);
					loginBean.setBackNews(backNews);
				}			
			}
			else{
				backNews = "�û��������벻��Ϊ��";
				loginBean.setId(id);
				loginBean.setLoginSuccess(false);
				loginBean.setBackNews(backNews);
			}
		}	
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("showLoginMess.jsp");
		dispatcher.forward(request, response);
	}

	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		doPost(request, response);
	}
}
