package com.net.service;

import android.content.Context;

import com.constants.GlobalFlag;
import com.db.UserData;
import com.db.service.LocalCachService;
import com.net.LCHttpUrlConnection;
import com.sxjs.diantu_daikuan.AppConfig;
import com.sxjs.diantu_daikuan.R;
import com.utils.LogUtil;
import com.utils.NetworkUtil;
import com.utils.StringUtil;
import com.utils.ToastUtil;
import com.utils.URLEncodUtil;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

/*
 * 根据Http请求得到数据，String类型
 */
public class NetRequestService {

	private static final String TAG = "NetRequestService";
	private Context mContext;

	public NetRequestService(Context context) {
		this.mContext = context;
	}

	private HttpURLConnection getConnection(String params)
			throws MalformedURLException, IOException {
		return LCHttpUrlConnection
				.getHttpConnectionWithHeader(mContext, params);

	}

	/**
	 * method，请求方式 params，get请求url后的参数 paramsMap，post请求，body里的参数 throws
	 * MalformedURLException, IOException needShowCach 是否需要展示缓存
	 * true为需要，false为不需要
	 */
	public String requestData(String method, String params,
			Map<String, String> paramMaps, boolean needShowCach) {
		// 生成缓存文件内容的key值
		String cachContentKey = generateKey(params, paramMaps);
		LocalCachService localcach = new LocalCachService(mContext,UserData.userId);
		if (needShowCach) {
			// 先读取缓存文件
			return localcach.getCachData(cachContentKey);
		}
		if (!NetworkUtil.isConnected(mContext)) {
			ToastUtil.showToast(mContext, R.string.POOR_NETWORK_STATUS, null,
					true);
			return null;
		}
		//LogUtil.d(TAG, "构造请求==");
		HttpURLConnection httpConn = null;
		try {
			httpConn = getConnection(params);
			//LogUtil.d(TAG, "httpConn为空==" + httpConn);
			if (null == httpConn)
				return null;
			httpConn.setRequestMethod(method);
			//LogUtil.d(TAG, "httpConn对象不为空==");
			// 请求参数放在body里时的post请求
			if (null != paramMaps) {
				StringBuilder data = new StringBuilder();
				httpConn.setDoOutput(true);
				httpConn.setDoInput(true);
				httpConn.setUseCaches(false);//"ASx5geJxPAVv8Kcb2orBKuXvuc7Rb9BqVHM="
				String token = GlobalFlag.global_token;
				if(!StringUtil.checkStr(UserData.userId)){
					//token = URLEncodUtil.getEncodeStr(token);
				}
				token = URLEncodUtil.getEncodeStr(token);
				paramMaps.put("token",token );//URLEncodUtil.getEncodeStr(GlobalFlag.global_token)
				paramMaps.put("version", AppConfig.APP_VERSION);
				paramMaps.put("appId", AppConfig.appId);
				paramMaps.put("app", AppConfig.app);
				paramMaps.put("channel", AppConfig.channel_name);
				paramMaps.put("net", AppConfig.net_way);
				paramMaps.put("platform", "adr");
				paramMaps.put("deviceModel", AppConfig.device_model);
				paramMaps.put("system_version", AppConfig.system_version);
				paramMaps.put("IMSI", AppConfig.IMSI);
				for (Map.Entry<String, String> map : paramMaps.entrySet()) {// Map.Entry<String,String>
																			// map:paramsMap.entrySet()
					data.append(map.getKey()).append("=");
					String value = map.getValue();
					if (StringUtil.checkStr(value)) {
						// value = URLEncoder.encode(value, "utf-8");
					} else {
						value = "";
					}
					data.append(value);
					data.append("&");
				}
				data.deleteCharAt(data.length() - 1);
				LogUtil.d(TAG, "发送post请求==参数 is " + data.toString());
				byte[] paramsdata = data.toString().getBytes();// Content-Type:
																// application/x-www-form-urlencoded
				httpConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				// httpConn.setRequestProperty("Content-Type","application/json");

				DataOutputStream ds = new DataOutputStream(
						httpConn.getOutputStream());

				ds.write(paramsdata);
				ds.flush();
				ds.close();
			}
			httpConn.connect();
			LogUtil.d(TAG, "发送请求==");
			int responseCode = httpConn.getResponseCode();
			LogUtil.d(TAG, "code is ==" + responseCode);
			if (responseCode == 200) {
				getCookies(httpConn);
				String result = LCHttpUrlConnection.decodeConnectionToString(httpConn);
				if(StringUtil.checkStr(result)){
					JSONObject jsonObject = new JSONObject(result);
					// 将字符串写入文件
					if (StringUtil.checkStr(cachContentKey))
						localcach.cachData(cachContentKey, result);
					int code = jsonObject.optInt("code");
					if(200!=code){
						String msg = mContext.getResources().getString(R.string.server_exception);
						JSONObject data = jsonObject.optJSONObject("data");
						if(null!=data){
							String message = data.optString("message");
							if(StringUtil.checkStr(message)){
								msg = message;
							}
						}
						ToastUtil.showToast(mContext,0, msg,true);
						return null;
					}
				}
				return result;
			} else {
				ToastUtil.showToast(mContext, R.string.server_exception, null,true);
				return null;
			}
		} catch (MalformedURLException e1) {
			LogUtil.d(TAG, "MalformedURLException==" + e1.getMessage());
			ToastUtil.showToast(mContext, R.string.reques_error_url, null, true);
			return null;
		} catch (SocketTimeoutException ste) {
			LogUtil.d(TAG, "SocketTimeoutException==" + ste.getMessage());
			ToastUtil
					.showToast(mContext, R.string.connect_time_out, null, true);
			return null;
		} catch (IOException e1) {
			LogUtil.d(TAG, "IOException==" + e1.getMessage());
			ToastUtil.showToast(mContext, R.string.POOR_NETWORK_STATUS, null,
					true);
			return null;
		} catch (Exception e) {
			LogUtil.d(TAG, "Exception==" + e.getMessage());
			ToastUtil.showToast(mContext, R.string.server_or_net_error, null,
					true);
			return null;
		} finally {
			if (null != httpConn) {
				httpConn.disconnect();
			}
			httpConn = null;
		}
		// return null;
	}

	private void getCookies(HttpURLConnection httpConn) {
		Map<String,List<String>> maps=httpConn.getHeaderFields();
		if(null==maps)
			return;
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String,List<String>> map :maps.entrySet()){
			String key = map.getKey();
			List<String> value = map.getValue();
			StringBuilder builder = new StringBuilder();
			if(value!=null){
				for(int i=0;i<value.size();i++){
					builder.append(value.get(i));
				}
			}
			sb.append(key+"="+builder.toString());
		}
		LogUtil.d("cookie", "getCookies()==sb.toString is  "+sb.toString());
		/*Set<String> set=map.keySet();
		
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			if (key.equals("Set-Cookie")) {
				System.out.println("key=" + key+",开始获取cookie");
				List<String> list = map.get(key);
				StringBuilder builder = new StringBuilder();
				for (String str : list) {
					builder.append(str).toString();
				}
				firstCookie=builder.toString();
				System.out.println("第一次得到的cookie="+firstCookie);
		}	}*/
	}

	private String generateKey(String params, Map<?, ?> paramMaps) {
		String key = null;
		if (null == paramMaps) {
			key = params;
		} else {
			key = params + paramMaps.toString();
		}
		return key;
	}
}
