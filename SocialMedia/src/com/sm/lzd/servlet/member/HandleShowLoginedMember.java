package com.sm.lzd.servlet.member;

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

import com.sm.lzd.model.member.Login;
import com.sm.lzd.model.member.MemberInform;
import com.sm.lzd.util.DbConn;

public class HandleShowLoginedMember extends HttpServlet{

	public void init(ServletConfig config)throws ServletException{
		super.init(config);
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		HttpSession session = request.getSession(true);
		Login loginBean = (Login) session.getAttribute("login");
		
		if(loginBean == null){
			response.sendRedirect("login.jsp");
			return;
		}
		else
			continueDoGet(request, response);  //鏄剧ず鐧诲綍鐨勬垚鍛樹俊鎭�
	}

	private void continueDoGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(true);
		Login loginBean = (Login) session.getAttribute("login");
		
		String id = loginBean.getId();
		MemberInform memberInformBean = new MemberInform();
		request.setAttribute("loginedInform", memberInformBean);
		
		Connection connection = DbConn.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("select id,email,phone,message,pic from member where id=?");
			preparedStatement.setString(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				memberInformBean.setBackNews("涓汉淇℃伅锛�");
				memberInformBean.setSelectOk(true);
				memberInformBean.setId(resultSet.getString(1));
				memberInformBean.setEmail(resultSet.getString(2));
				memberInformBean.setPhone(resultSet.getString(3));
				memberInformBean.setMessage(resultSet.getString(4));
				memberInformBean.setPic(resultSet.getString(5));
			}
			else
				memberInformBean.setBackNews("鏈煡鍒颁换浣曚俊鎭�傘�傘��");
		
			preparedStatement.close();
			connection.close();
		} catch (Exception e) {
			memberInformBean.setBackNews("鏈煡鍒颁换浣曚俊鎭�傘�傘��");
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("member/index.jsp");
		dispatcher.forward(request, response);
	}
}
