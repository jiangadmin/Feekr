package com.tl.film.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpParamUtils {
	
	/**
	 * 获取请求参数
	 * @param params		api接口业务参数
	 * @return				返回组装后的请求参数
	 */
	public static Map<String,String> getRequestParams(Map<String,String> params){
		if(params == null){
			params = new HashMap<String,String>();
		}
		
		//创建签名
		String sign = MD5(sortParams(params));
		
		//创建时间戳
		long timestamp = new Date().getTime();
		
		params.put("sign", sign);
		params.put("timestamp", String.valueOf(timestamp));
		return params;
	}
	
	
	/**
	 * 获得参数格式化字符串
	 * 参数名按字典升序排序
	 * @return 
	 */
	public static String sortParams(Map<String,String> params){
	    List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(params.entrySet());
	    Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
	        public int compare(Map.Entry<String, String> arg0, Map.Entry<String, String> arg1) {
	            return (arg0.getKey()).compareTo(arg1.getKey());
	        }
	    });
	    StringBuffer sb = new StringBuffer("");
	    for (Map.Entry<String, String> entry : infoIds) {
	    	sb.append(entry.getKey())
	    		.append("=")
	    		.append(entry.getValue() == null?"":entry.getValue())
	    		.append("&");
	    }
	    String rs = "";
	    try {
	    	rs = sb.toString();
		    rs = rs.substring(0, rs.length() - 1);
			rs = URLEncoder.encode(rs,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    return rs;
	}

	/**
	 * MD5加密
	 * @param data
	 * @return
	 */
	public static String MD5(String data) {
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(data.getBytes("UTF-8"));
	        StringBuilder sb = new StringBuilder();
	        for (byte item : array) {
	            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
	        }
	        return sb.toString().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
    }
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("mac", "00:12:B2:33");
			params.put("mainBoard", "adm");
			params.put("ip", null);
			System.out.println(getRequestParams(params));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
