package com.sm.lzd.servlet.member;

import java.io.File;
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
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sm.lzd.model.member.Login;
import com.sm.lzd.model.member.UploadFile;
import com.sm.lzd.util.DbConn;
import com.sm.lzd.util.StringUtil;

public class HandleUploadImage extends HttpServlet{
	
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
			uploadImage(request,response);
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		doPost(request, response);
	}

	private void uploadImage(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(true);
		Login loginBean = (Login) session.getAttribute("login");
		
		String id = loginBean.getId();
		UploadFile uploadFileBean = new UploadFile();
		request.setAttribute("userImage", uploadFileBean);
			
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);  //鍒ゆ柇鎻愪氦鐨勮〃鍗曟槸鍚︽槸鏂囦欢涓婁紶琛ㄥ崟
		String savedFileName = "";  //鏈�鍚庝繚瀛樼殑鏂囦欢鍚�
		String backNews = "";
		if(isMultipart){
			FileItemFactory fileItemFactory = new DiskFileItemFactory();  //鑾峰緱纾佺洏鏂囦欢鏉＄洰宸ュ巶 
			ServletFileUpload upload = new ServletFileUpload(fileItemFactory);  //楂樻按骞崇殑API鏂囦欢涓婁紶澶勭悊  
			try {
				FileItem item =  (FileItem) upload.parseRequest(request).get(0);  //鍙湁涓�涓笂浼犳枃浠讹紝鍙栫涓�涓嵆鍙�

//				String path1 = request.getRealPath("/data/userfile/image");  //姝ゆ柟娉曞凡缁忚繃鏃�
//				String path1 = session.getServletContext().getRealPath("/data/userfile/image");  //涔熷彲浠ヨ繖鏍峰啓
				String path1 = this.getServletContext().getRealPath("/data/userfile/image");  //鏂囦欢淇濆瓨璺緞

				
				if(!item.isFormField()){
					String value = StringUtil.xssEncode(item.getName());  //鑾峰彇涓婁紶鏂囦欢鐨勬枃浠跺悕
					int start = value.lastIndexOf("\\");
					String fileName = value.substring(start+1);  //鏂囦欢鍚�
					String filetype = "jpg";
					//杩囨护鏂囦欢鏍煎紡
					if (fileName.length() > 0) {   
			            int start1 = fileName.lastIndexOf('.');   
			            if ((start1 >-1) && (start1 < (fileName.length() - 1))) {  
			                String fileTemptype = fileName.substring(start1 + 1);   
			                if("jpg".equals(fileTemptype) || "jpeg".equals(fileTemptype) || "png".equals(fileTemptype))
			                	filetype = fileTemptype;	                	
			            }   
					}
														
					//閲嶅懡鍚嶆枃浠�
					String fileName1 = StringUtil.getNewFileNameString(5)+"."+filetype;	
					savedFileName = fileName1;
					item.write(new File(path1,fileName1));
/*						
					//淇濆瓨鏂囦欢锛屾墜鍔ㄥ啓
					OutputStream out = new FileOutputStream(new File(path1,fileName1));                   
                    InputStream in = item.getInputStream() ;                        
                    int temp = 0 ;  
                    byte [] buf = new byte[1024] ;  
                      
                    System.out.println("鑾峰彇涓婁紶鏂囦欢鐨勬�诲叡鐨勫閲忥細"+item.getSize());  
  
                    // in.read(buf) 姣忔璇诲埌鐨勬暟鎹瓨鏀惧湪   buf 鏁扮粍涓�  
                    while((temp = in.read(buf)) != -1)  
                    {  
                        //鍦�   buf 鏁扮粍涓� 鍙栧嚭鏁版嵁 鍐欏埌 锛堣緭鍑烘祦锛夌鐩樹笂  
                        out.write(buf, 0, temp);                         
                    }                       
                    in.close();  
                    out.close();  
*/					
				}	
				
				backNews = "鍥惧儚涓婁紶鎴愬姛";
				uploadFileBean.setUploadFileOk(true);
				uploadFileBean.setSavedFileName(savedFileName);
			} catch (Exception e) {
				backNews = "鍥惧儚涓婁紶澶辫触";	
			}
		}
		
		//鏇存柊鏁版嵁搴�
		Connection connection = DbConn.getConnection();
		try {
			PreparedStatement pStatement = connection.prepareStatement("update member set pic=? where id=?");
			pStatement.setString(1, savedFileName);
			pStatement.setString(2, id);
			
			int num = pStatement.executeUpdate();
			if(num == 1)
				backNews = backNews + "锛屾洿鏂版暟鎹簱鎴愬姛";
			else
				backNews = backNews + "锛屾洿鏂版暟鎹簱澶辫触";	
			connection.close();
		} catch (SQLException e) {
			backNews = backNews + "锛屾洿鏂版暟鎹簱澶辫触";	
		}
		uploadFileBean.setBackNews(backNews);
				
		RequestDispatcher dispatcher = request.getRequestDispatcher("member/showUploadImageMess.jsp");
		dispatcher.forward(request, response);
	} 
}
