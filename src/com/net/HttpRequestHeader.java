package com.net;

import android.content.Context;

import com.constants.DeviceParamsDB;
import com.db.UserData;
import com.sxjs.diantu_daikuan.AppConfig;
import com.utils.LogUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/*
 * 构建http请求头,返回一个HttpURLConnection对象
 */
public class HttpRequestHeader {

	private static final String TAG = "HttpRequestHeader";
	
	public static HttpURLConnection constructHeader(Context context,String params) throws MalformedURLException, IOException{

		//当前未得到有效的网络
		String requestUrl = NetUrl.getHost();
		
		HttpURLConnection httpConnection = LCHttpUrlConnection.getHttpConnection(requestUrl,params);
		LogUtil.d(TAG, "requestUrl is "+requestUrl+",httpConnection is "+httpConnection+",UserData.userToken is "+UserData.userToken);
		if(null == httpConnection) return null;
		
		httpConnection.setRequestProperty(Header.DEVICEMODE, DeviceParamsDB.deviceModel);
		httpConnection.setRequestProperty(Header.DEVICESIZE, DeviceParamsDB.deviceSize);
		httpConnection.setRequestProperty(Header.APPVERSION, DeviceParamsDB.appVersion);
		httpConnection.setRequestProperty(Header.APPNAME, "com");
		httpConnection.setRequestProperty(Header.SYSTEM_VERSION, DeviceParamsDB.systemVersion);
		httpConnection.setRequestProperty(Header.UID, UserData.userId);
		httpConnection.setRequestProperty(Header.CHANNELID, DeviceParamsDB.channelID);
		httpConnection.setRequestProperty(Header.CHANNELNAME, DeviceParamsDB.channelName);
		httpConnection.setRequestProperty(Header.ACCESS_MODE, DeviceParamsDB.currentNetType);
		httpConnection.setRequestProperty(Header.ACCESS_TOKEN, UserData.userToken);
		httpConnection.setRequestProperty(Header.PLATFORM, DeviceParamsDB.platform);
		httpConnection.setRequestProperty(Header.CLIENTID, DeviceParamsDB.deviceId);
		//httpConnection.setRequestProperty("TOKEN", "uid="+UserData.userId+"&ver=1.0"+"&token="+URLEncodUtil.getEncodeStr(UserData.userToken));//URLEncoder.encode()
		//httpConnection.setRequestProperty("ver", "1.0");
		httpConnection.setRequestProperty(Header.ACCEPT_ENCODING, "gzip");//"&ver=1.0";
		LogUtil.d(TAG, "UserData.userId"+UserData.userId+",UserData.userToken is "+UserData.userToken);
		httpConnection.setRequestProperty("charsert", "utf-8");
		//httpConnection.setRequestProperty("Connection", "Close");
		httpConnection.setRequestProperty("Connection", "Keep-Alive");
		httpConnection.setRequestProperty("mlj-version", AppConfig.APP_VERSION);
		httpConnection.setRequestProperty("mlj-device", "android");
		httpConnection.setConnectTimeout(AppConfig.CONNECT_TIME_OUT);
		httpConnection.setReadTimeout(AppConfig.CONNECT_TIME_OUT);
		httpConnection.setUseCaches(true); 
		return httpConnection;
	}
	
}
