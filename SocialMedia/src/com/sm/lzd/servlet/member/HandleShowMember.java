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
import com.sun.rowset.CachedRowSetImpl;
import com.sm.lzd.model.member.Login;
import com.sm.lzd.model.member.MemberInform;
import com.sm.lzd.model.member.ShowByPage;
import com.sm.lzd.util.DbConn;
import com.sm.lzd.util.StringUtil;

public class HandleShowMember extends HttpServlet{
	private CachedRowSetImpl rowSet = null;
	
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
			continueDoPost(request,response);  //鏄剧ず鍏ㄤ綋鎴愬憳淇℃伅
	
	}

	private void continueDoPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(true);
		ShowByPage showByPageBean = null;
		//鍥犱负娑夊強鍒扮炕椤甸棶棰橈紝闇�瑕佽繖涓猙ean闀挎湡瀛樺湪锛宻cope璁句负session
		try {
			showByPageBean = (ShowByPage) session.getAttribute("showAllMember");
			if(showByPageBean == null){
				showByPageBean = new ShowByPage();
				session.setAttribute("showAllMember", showByPageBean);
			}
		} catch (Exception e) {
			showByPageBean = new ShowByPage();
			session.setAttribute("showAllMember", showByPageBean);
		}
		
		showByPageBean.setPageSize(3);  //姣忛〉鏄剧ず3鏉℃暟鎹�
		int showPage = Integer.parseInt(StringUtil.xssEncode(request.getParameter("showPage")));
		if(showPage > showByPageBean.getPageAllCount())
			showPage = 1;
		else if(showPage < 1)
			showPage = showByPageBean.getPageAllCount();
		showByPageBean.setShowPage(showPage);
		
		Connection connection = DbConn.getConnection();
		try {
			PreparedStatement pStatement = connection.prepareStatement("select id,email,phone,message,pic from member");
			ResultSet resultSet = pStatement.executeQuery();
			rowSet = new CachedRowSetImpl();
			rowSet.populate(resultSet);			
			rowSet.last();  //鎸囧悜缁撴灉闆嗙殑鏈熬
			int m = rowSet.getRow();  //鎬昏鏁�
			int n = showByPageBean.getPageSize();  //姣忛〉鏄剧ず鐨勮褰曟暟
			if(m%n ==0)
				showByPageBean.setPageAllCount(m/n);
			else
				showByPageBean.setPageAllCount(m/n + 1);
			
			int pageSize = showByPageBean.getPageSize();
			showByPageBean.setPresentPageResult(StringUtil.showMember(showPage, pageSize, rowSet));
			
			resultSet.close();
			connection.close();
		} catch (Exception e) {

		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("member/showAllMember.jsp");
		dispatcher.forward(request, response);
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		HttpSession session = request.getSession(true);
		Login loginBean = (Login) session.getAttribute("login");
		
		if(loginBean == null)
			response.sendRedirect("login.jsp");
		else
			continueDoGet(request, response);  //鏄剧ず鎸囧畾鎴愬憳淇℃伅
	}

	private void continueDoGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		//鎸囧畾鎴愬憳鐨刬d
		String id = StringUtil.xssEncode(request.getParameter("selectedId"));
		MemberInform memberInformBean = new MemberInform();
		request.setAttribute("memberInform", memberInformBean);
		
		Connection connection = DbConn.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("select id,email,phone,message,pic from member where id=?");
			preparedStatement.setString(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				memberInformBean.setBackNews("鏌ヨ鍒扮殑浼氬憳淇℃伅锛�");
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
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("member/showLookedMember.jsp");
		dispatcher.forward(request, response);
	}	
}
