package com.sm.lzd.servlet.member;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sm.lzd.model.member.Login;

public class HandleExit extends HttpServlet{
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		HttpSession session = request.getSession(true);
		Login login = (Login) session.getAttribute("login");
		if(login == null){
			response.sendRedirect("login.jsp");
			return;
		}
		else{
			//ע��
			session.invalidate();  //����session����
			response.sendRedirect("index.jsp");  //�ض�����ҳ
			return;
		}		
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		doPost(request, response);
	}
}
