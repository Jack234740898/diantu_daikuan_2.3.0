package com.net.service;

import android.content.Context;

import com.net.OkHttpClientManager;
import com.sxjs.diantu_daikuan.MyApplication;
import com.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public abstract class BaseJSONService {

	protected OkHttpClientManager mOkHttpClientManager;
	//protected NetRequestService mNetRequService;
	protected Context mContext;
	public BaseJSONService(Context con) {
		this.mContext = con;
		//mNetRequService = new NetRequestService(mContext);
		mOkHttpClientManager = ((MyApplication)mContext.getApplicationContext()).mOkHttpClientManager;
	}

	protected boolean mNeedCach = false;// 是否需要缓存
	public void setNeedCach(boolean needCach) {
		mNeedCach = needCach;
	}
	
	protected JSONObject getJSON(String url,String interface_params,Map<String,String> mapParams,boolean isPost){
		String json = mOkHttpClientManager.requestData(url, interface_params, mapParams, isPost, mNeedCach);
		if(StringUtil.checkStr(json)){
			try {
				return new JSONObject(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
