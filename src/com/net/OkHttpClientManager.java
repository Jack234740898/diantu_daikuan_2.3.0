package com.net;

import android.content.Context;

import com.constants.GlobalFlag;
import com.db.UserData;
import com.db.service.LocalCachService;
import com.sxjs.diantu_daikuan.AppConfig;
import com.sxjs.diantu_daikuan.R;
import com.utils.FileUtil;
import com.utils.LogUtil;
import com.utils.NetworkUtil;
import com.utils.StringUtil;
import com.utils.ToastUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttpClientManager代码封装
 */
public class OkHttpClientManager {
	private static final String TAG = "OkHttpClientManager";
	//private static final int cacheSize = 10 * 1024 * 1024;
	private static final int read_time = 30 * 1000;
	private static final int write_time = 30 * 1000;
	private static final int connect_time = 30 * 1000;
	private static final MediaType jsonType = MediaType
			.parse("application/json; charset=utf-8");
	private static OkHttpClient mOkHttpClient;
	private Context mContext;

	public OkHttpClientManager(Context context) {
		this.mContext = context;
		mOkHttpClient = new OkHttpClient();
		//mOkHttpClient.se
		mOkHttpClient.newBuilder()
				.readTimeout(read_time, TimeUnit.MILLISECONDS);
		mOkHttpClient.newBuilder().writeTimeout(write_time,
				TimeUnit.MILLISECONDS);
		mOkHttpClient.newBuilder().connectTimeout(connect_time,
				TimeUnit.MILLISECONDS);
		/*
		 * File cacheDirectory = new
		 * File(SDCardUtil.getStoragePath(File.separator + "." + "dtdk")); Cache
		 * cache = new Cache(cacheDirectory, cacheSize);
		 * mOkHttpClient.newBuilder().cache(cache);
		 */
	}

	private Request getRequest(String host, String interface_params,
			Map<String, String> mapParams, boolean isPost) {
		String url = null;
		if (null == host) {
			url = NetUrl.getHost() + interface_params;
		} else {
			url = host ;
			if(null!=interface_params){
				url = url+ interface_params;
			}
		}
		if (!StringUtil.isHttpUrl(url))
			return null;
		Builder builder = new Request.Builder().url(url);
		if (isPost) {
			// post请求
			if (null == mapParams)
				mapParams = new HashMap<String, String>();
			String token = GlobalFlag.global_token;
			LogUtil.d(TAG, "getRequest()==token is "+token);
			if(!StringUtil.checkStr(UserData.userId)){
				//token = URLEncodUtil.getEncodeStr(token);
			}
			//token = URLEncodUtil.getEncodeStr(token);
			mapParams.put("token",token);//URLEncodUtil.getEncodeStr(GlobalFlag.global_token)
			mapParams.put("version", AppConfig.APP_VERSION);
			mapParams.put("appId", AppConfig.appId);
			mapParams.put("app", AppConfig.app);
			mapParams.put("channel", AppConfig.channel_name);
			mapParams.put("net", AppConfig.net_way);
			mapParams.put("platform", "adr");
			mapParams.put("deviceModel", AppConfig.device_model);
			mapParams.put("system_version", AppConfig.system_version);
			mapParams.put("IMSI", AppConfig.IMSI);
			LogUtil.d(TAG, "getRequest()==url is "+url+",mapParams is "+mapParams.toString());
			FormBody.Builder fb = new FormBody.Builder();
			Iterator<Entry<String, String>> iterator = mapParams.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				if (!StringUtil.checkStr(value))
					value = "";
				fb.add(key, value);
			}
			builder.post(fb.build());
		} else {
			// get请求
		}
		builder.header("Cache-Control", String.format("max-age=%d", 0));//, 
		return builder.build();
	}

	private String sync_req(Request request) {
		Call call = mOkHttpClient.newCall(request);
		try {
			Response response = call.execute();
			/*
			 * Response cacheResponse = response.cacheResponse(); LogUtil.d(TAG,
			 * "sync_req==cacheResponse is "+response); if(null==cacheResponse){
			 * response = response.networkResponse(); LogUtil.d(TAG,
			 * "sync_req==networkResponse is "+response); }else{ response =
			 * cacheResponse; }
			 */
			if (null == response)
				return null;
			int res_code = response.code();
			Headers headers = response.headers();
			String message = response.message();
			if (200 == res_code) {
				ResponseBody responseBody = response.body();
				byte[] bytes = responseBody.bytes();
				if(null==bytes || bytes.length<=0)
					return null;
				String dataStr = new String(bytes, "utf-8");
				LogUtil.d(TAG, "请求成功" + dataStr);
				if (StringUtil.checkStr(dataStr)) {
					JSONObject jsonObj = new JSONObject(dataStr);
					int code = jsonObj.optInt("code");
					if (200 != code) {
						String msg = mContext.getResources().getString(
								R.string.server_exception);
						JSONObject data = jsonObj.optJSONObject("data");
						if (null != data) {
							String msgee = data.optString("message");
							if (StringUtil.checkStr(msgee)) {
								msg = msgee;
							}
						}
						ToastUtil.showToast(mContext, 0, msg, true);
						return null;
					}
				}
				return dataStr;
			}
			ToastUtil.showToast(mContext, R.string.POOR_NETWORK_STATUS, null,
					true);
			return null;
		} catch (MalformedURLException e1) {
			LogUtil.d(TAG, "MalformedURLException==" + e1.getMessage());
			ToastUtil
					.showToast(mContext, R.string.reques_error_url, null, true);
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
			// mOkHttpClient.
		}
	}

	private String sync_req1(Request request) {
		Call call = mOkHttpClient.newCall(request);
		try {
			Response response = call.execute();
			int res_code = response.code();
			if (200 == res_code) {
				ResponseBody responseBody = response.body();
				byte[] bytes = responseBody.bytes();
				String dataStr = new String(bytes, "utf-8");
				return dataStr;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 同步get/post请求
	 * @param host 传null时使用默认的host
	 */
	public String requestData(String host, String interface_params,
			Map<String, String> paramMaps, boolean isPost, boolean isShowCach) {
		// 生成缓存文件内容的key值
		String cachContentKey = generateKey(interface_params, paramMaps);
		LocalCachService localcach = new LocalCachService(mContext,
				UserData.userId);
		if (isShowCach) {
			// 先读取缓存文件
			return localcach.getCachData(cachContentKey);
		}
		if (!NetworkUtil.isConnected(mContext)) {
			ToastUtil.showToast(mContext, R.string.POOR_NETWORK_STATUS, null,
					true);
			return null;
		}
		String result = sync_req(getRequest(host, interface_params, paramMaps, isPost));
		if (StringUtil.checkStr(cachContentKey))
			localcach.cachData(cachContentKey, result);
		return result;
	}

	/*
	 * 提交json请求
	 */
	public String postJson(String url, String json) {
		RequestBody body = RequestBody.create(jsonType, json);
		Request request = new Request.Builder().url(url).post(body).build();
		return sync_req(request);
	}

	/*
	 * 上传文件
	 */
	public String postFile(String filePath, String destUrl,
			HashMap<String, String> paramMaps) {
		if (!FileUtil.isExist(filePath))
			return null;
		LogUtil.d(TAG, "postFile==file_length is "+new File(filePath).length());
		okhttp3.MultipartBody.Builder formBuilder = new MultipartBody.Builder();
		if (null == paramMaps)
			paramMaps = new HashMap<String, String>();
		paramMaps.put("token", GlobalFlag.global_token);
		paramMaps.put("version", AppConfig.APP_VERSION);
		LogUtil.d(TAG, "postFile==token is " + GlobalFlag.global_token
				+ ",filePath is " + filePath);
		Iterator<Entry<String, String>> iterator = paramMaps.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			if (!StringUtil.checkStr(value))
				value = "";
			formBuilder.addFormDataPart(key, value);
		}
		RequestBody reqBody = RequestBody.create(MediaType.parse("image/*"),
				new File(filePath));
		formBuilder.addFormDataPart("pictures", filePath, reqBody);
		// formBuilder.addPart(reqBody);
		Builder builder = new Request.Builder().url(destUrl);
		builder.post(formBuilder.build());
		Request request = builder.build();
		return sync_req(request);
	}

	/*
	 * 下载文件
	 */
	public String downloadFile() {
		return "";
	}
	
	private String generateKey(String params, Map<String,String> paramMaps) {
		String key = null;
		if (null == paramMaps) {
			key = params;
		} else {
			key = params + paramMaps.toString();
		}
		return key;
	}
}
