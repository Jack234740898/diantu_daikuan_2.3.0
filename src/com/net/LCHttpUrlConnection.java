package com.net;

import android.content.Context;

import com.utils.StreamUtil;
import com.utils.StringUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/*
 * 定义一个静态的HttpConnection对象
 */
public class LCHttpUrlConnection {

    private LCHttpUrlConnection(){}
	/*
	 * path 请求的主机名
	 * params get请求时的参数
	 * 返回一个不带头的HttpURLConnection对象
	 */
    public static HttpURLConnection getHttpConnection(String path,String params) throws MalformedURLException, IOException{
    	if(!StringUtil.checkStr(path))return null;
    	URL url = null;
    	if(StringUtil.checkStr(params)){
    		url = new URL(path+params);
    	}else{
    		url = new URL(path);
    	}   	
    	HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection(); 
    	return httpConnection;
    }
    
    /*
     * interfaceID 请求的接口编号
     * params  get请求时的参数
     * 返回一个HttpURLConnection对象
     */
    public static HttpURLConnection getHttpConnectionWithHeader(Context context,String params) throws MalformedURLException, IOException{
    	return HttpRequestHeader.constructHeader(context,params);
    }
    
    /*
     * 根据HttpURLConnection，解析流得到一个字符串
     */
    public static String decodeConnectionToString(
			HttpURLConnection httpConnection) throws IOException {
		/*String contentType = httpConnection.getHeaderField("Content-Type");
		String contentEncoding = httpConnection.getHeaderField("Content-Encoding");*/
		byte[] bytes = StreamUtil.readStream(httpConnection.getInputStream());
		if (bytes == null || bytes.length == 0)
			return null;
		return new String(bytes, "UTF-8");
	}
}
