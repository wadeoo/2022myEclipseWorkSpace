package com.sm.lzd.util;

import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.StringEscapeUtils;

import com.sun.rowset.CachedRowSetImpl;

public class StringUtil {
	/**
	 * 瀛楃涓叉牸寮忚浆鎹�
	 * 
	 * */
	public static String handleString(String str){
		try {
			byte[] bb = str.getBytes("UTF-8");
			str = new String(bb);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
		return str;
	}
	
	/**
	 * 鍒ゆ柇瀛楃涓叉槸鍚︿负绌�
	 * @param str String绫诲瀷鐨勫瓧绗︿覆
	 * 
	 * @return 涓嶄负绌鸿繑鍥� true
	 * */
	public static boolean isNotEmpty(String str){
		if(!"".equals(str) && str != null)
			return true;
		else 
			return false;
	}
	
	/**
	 * 灏嗕竴涓瓧绗︿覆涓殑灏忓啓瀛楁瘝杞崲涓哄ぇ鍐欏瓧姣�
	 * 
	 * */
	public static String convertToCapitalString(String src)
    {
         char[] array = src.toCharArray();
          int temp = 0;
          for (int i = 0; i < array.length; i++)
          {
              temp = (int) array[i];
              if (temp <= 122 && temp >= 97){ // array[i]涓哄皬鍐欏瓧姣�
                   array[i] = (char) (temp - 32);
              }
           }
           return String.valueOf(array);
       }  
	
	/**
	 * 瀛楃涓瞂SS杩囨护锛孞avaScript杩囨护锛孲ql杩囨护
	 * 
	 * @param str 浼犲叆鐨勫瓧绗︿覆
	 * 
	 * @return 杞箟鍚庣殑瀛楃涓�
	 * */
	public static String xssEncode(String str){
		String s = StringEscapeUtils.escapeHtml(str);
//		s = StringEscapeUtils.escapeJavaScript(s);
//		s = StringEscapeUtils.escapeSql(s);		
		return s;	
	}
	
	/**
	 * 鑾峰彇褰撳墠鏃堕棿浠ュ強闅忔満鐨刲ength涓瓧绗︾粍鎴愮殑瀛楃涓诧紝鐢ㄦ潵閲嶅懡鍚嶆枃浠�
	 * @param length 鏃ユ湡鍚庨潰鐨勯殢鏈哄瓧绗︿覆鐨勯暱搴�
	 * 
	 * @return 褰撳墠绮剧‘鍒版绉掔殑鏃堕棿+length闀垮害鐨勯殢鏈哄瓧绗︿覆缁勬垚鐨勫瓧绗︿覆
	 * */
	public static String getNewFileNameString(int length){
		String base = "qwertyuioplkjhgfdsazxcvbnm0123456789";
		Date date = new Date();
		Format format = new SimpleDateFormat("yyyyMMddHHmmssSSS");  //鏍煎紡鍖栧綋鍓嶆椂闂达紝绮剧‘鍒版绉�
		String dateString = format.format(date);	
		
		Random random = new Random();
		StringBuffer stringBuffer = new StringBuffer();
		for(int i=0;i<length;i++){
			int dot = random.nextInt(base.length());
			stringBuffer.append(base.charAt(dot));
		}
		String randomString = stringBuffer.toString();
		
		return dateString+randomString;		
	}
	
	/**
	 * 灏嗕粠鏁版嵁搴撲腑鍙栧嚭鐨勬暟鎹互琛ㄦ牸褰㈠紡鏄剧ず
	 * @param page 褰撳墠鏄剧ず椤�
	 * @param pageSize 姣忛〉鏄剧ず璁板綍鏁�
	 * @param rowSet 瀛樺偍琛ㄤ腑鍏ㄩ儴璁板綍鐨勮闆嗗璞�
	 * 
	 * @return 琛ㄦ牸鏍煎紡鍖栧悗鐨勫寘鍚墍鏈夎褰曠殑StringBuffer
	 * */
	public static StringBuffer showMember(int page,int pageSize,CachedRowSetImpl rowSet){
		StringBuffer str = new StringBuffer();
		try {
			rowSet.absolute((page-1)*pageSize + 1);  //鎸囧悜褰撳墠椤电殑绗竴涓厓绱�
			for(int i=1;i<=pageSize;i++){
				str.append("<tr>");
				str.append("<td align=left>" + rowSet.getString(1) + "</td>");
				str.append("<td align=left>" + rowSet.getString(2) + "</td>");
				str.append("<td align=left>" + rowSet.getString(3) + "</td>");
				str.append("<td align=left>" + rowSet.getString(4) + "</td>");
				String imgString = "<img src=data/userfile/image/" + rowSet.getString(5) + " width=100 height=100/>";
				str.append("<td align=left>" + imgString + "</td>");
				str.append("</tr>");
				rowSet.next();
			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return str;
	}
	
	/**
	 * 鏄剧ず鏂囩珷鍒楄〃
	 * @param page 褰撳墠鏄剧ず椤�
	 * @param pageSize 姣忛〉鏄剧ず璁板綍鏁�
	 * @param rowSet 瀛樺偍琛ㄤ腑鍏ㄩ儴璁板綍鐨勮闆嗗璞�
	 * 
	 * @return 琛ㄦ牸鏍煎紡鍖栧悗鐨勫寘鍚墍鏈夎褰曠殑StringBuffer
	 * */
	public static StringBuffer showArticleTitle(int page,int pageSize,CachedRowSetImpl rowSet){
		StringBuffer str = new StringBuffer();
		try {
			rowSet.absolute((page-1)*pageSize + 1);  //鎸囧悜褰撳墠椤电殑绗竴涓厓绱�
			str.append("<br>");
			for(int i=1;i<=pageSize;i++){
				str.append("<tr>");
				str.append("<td align=left><a href=ArticleShowContent?id=" + rowSet.getInt(1) + ">" + rowSet.getString(2) + "</a></td>");				
				str.append("</tr>");
				rowSet.next();
			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return str;
	}
	
}
