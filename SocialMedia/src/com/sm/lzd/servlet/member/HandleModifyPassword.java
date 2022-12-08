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
import com.sm.lzd.model.member.ModifyPassword;
import com.sm.lzd.util.DbConn;
import com.sm.lzd.util.StringUtil;

public class HandleModifyPassword extends HttpServlet{
	
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
		else{
			modifyPassword(request,response);
		}		
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		doPost(request, response);
	}

	private void modifyPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(true);
		Login loginBean = (Login) session.getAttribute("login");
		
		String id = loginBean.getId();  //鑾峰彇宸茬櫥鐢ㄦ埛褰曠殑id
		ModifyPassword passwordBean = new ModifyPassword();
		request.setAttribute("password", passwordBean);
		//鑾峰彇杈撳叆鐨勫弬鏁�
		String oldPassword = StringUtil.xssEncode(request.getParameter("oldPassword").trim());
		String newPassword = StringUtil.xssEncode(request.getParameter("newPassword").trim());
		
		//寮�濮嬫洿鏂�
		Connection connection = DbConn.getConnection();
		try {
			//鍒ゆ柇鏃у瘑鐮佹槸鍚︽纭�
			PreparedStatement pStatementQuery = connection.prepareStatement("select id from member where id=? and password =?");
			pStatementQuery.setString(1, id);
			pStatementQuery.setString(2, oldPassword);
			ResultSet resultSet = pStatementQuery.executeQuery();
			
			if(resultSet.next()){
				//濡傛灉姝ｇ‘鍒欐洿鏂板瘑鐮�
				PreparedStatement pStatementUpdate = connection.prepareStatement("update member set password=? where id=?");
				pStatementUpdate.setString(1, newPassword);
				pStatementUpdate.setString(2, id);
				int num = pStatementUpdate.executeUpdate();
				//鎵ц鎴愬姛杩斿洖1
				if(num == 1){
					passwordBean.setBackNews("瀵嗙爜鏇存柊鎴愬姛");
					passwordBean.setNewPassword(newPassword);
					passwordBean.setModifyPasswordOk(true);
					session.invalidate();  //鏇存柊瀵嗙爜鍚庢秷闄ession锛岄渶瑕侀噸鏂扮櫥褰�
				}
				else
					passwordBean.setBackNews("瀵嗙爜鏇存柊澶辫触");			
			}
			else
				passwordBean.setBackNews("瀵嗙爜鏇存柊澶辫触");			
			connection.close();
		} catch (Exception e) {
			passwordBean.setBackNews("瀵嗙爜鏇存柊澶辫触");			
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("member/showNewPasswordMess.jsp");
		dispatcher.forward(request, response);
	}
}
