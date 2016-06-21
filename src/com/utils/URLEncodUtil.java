package com.utils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLEncodUtil {

	public static String getEncodeStr(String params){
		if(!StringUtil.checkStr(params))
			return "";
		try {
			return URLEncoder.encode(params,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getDecodeStr(String params){
		if(!StringUtil.checkStr(params))
			return "";
		try {
			return URLDecoder.decode(params,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
