package com.sm.lzd.servlet.member;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sm.lzd.model.member.Register;
import com.sm.lzd.util.DbConn;
import com.sm.lzd.util.StringUtil;

public class HandleRegister extends HttpServlet{
	private String backNews = "",pic = "public.jpg";  //pic涓哄浘鐗囦俊鎭�
	
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		Register registerBean = new Register();
		request.setAttribute("register", registerBean);
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		//鑾峰彇娉ㄥ唽淇℃伅
		String id = StringUtil.xssEncode(request.getParameter("id").trim());
		String password = StringUtil.xssEncode(request.getParameter("password").trim());
		String email = StringUtil.xssEncode(request.getParameter("email").trim());
		String phone = StringUtil.xssEncode(request.getParameter("phone").trim());
		String message = StringUtil.xssEncode(request.getParameter("message"));
		
		boolean isSuccess = false;  //鍒ゆ柇娉ㄥ唽淇℃伅鏄惁绗﹀悎瑙勫畾
		if(StringUtil.isNotEmpty(id) &&StringUtil.isNotEmpty(password)){
			isSuccess = true;
			//鍒ゆ柇id鏄惁绗﹀悎鏍囧噯
			for(int i=0;i<id.length();i++){
				char c = id.charAt(i);
				if(!((c>='a'&&c<='z') || (c>='A'&&c<='Z') || (c>='0'&&c<='9'))){
					isSuccess = false;
					break;
				}
			}
		}
				
		//鍚� mysql 涓敞鍐岀敤鎴�			
		try {	
			if(isSuccess){
				Connection conn = DbConn.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement("insert into member(id,password,email,phone,message,pic) values(?,?,?,?,?,?)");
				preparedStatement.setString(1, id);
				preparedStatement.setString(2, password);
				preparedStatement.setString(3, email);
				preparedStatement.setString(4, phone);
				preparedStatement.setString(5, message);
				preparedStatement.setString(6, pic);
				
				//鎵ц鎴愬姛杩斿洖琛屾暟澶т簬0
				int num = preparedStatement.executeUpdate();
				if(num != 0){
					backNews = "娉ㄥ唽鎴愬姛";
					registerBean.setBackNews(backNews);
					registerBean.setId(id);
					registerBean.setPassword(password);
					registerBean.setEmail(email);
					registerBean.setPhone(phone);
					registerBean.setMessage(message);
					registerBean.setRegisterSuccess(true);
				}
				preparedStatement.close();
				conn.close();
			}
			else{
				backNews = "淇℃伅濉啓涓嶅畬鏁存垨鑰呭悕瀛椾腑鏈夐潪娉曞瓧绗�";
				registerBean.setBackNews(backNews);
			}			
		} catch (SQLException e) {
			backNews = "璇D宸茶浣跨敤锛岃鏇存崲ID";
			registerBean.setBackNews(backNews);
		}
				
		RequestDispatcher dispatcher = request.getRequestDispatcher("showRegisterMess.jsp");
		dispatcher.forward(request, response);	
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		doPost(request, response);
	}
}
