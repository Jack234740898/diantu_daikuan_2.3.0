package com.db.service;

import android.content.Context;

import com.constants.ParamsKey;
import com.db.SharePreferDB;

import java.util.Map;

public class DeviceDataService {

	private Context mContext;
	private SharePreferDB mSharePreferDB;
	public DeviceDataService(Context context){
		this.mContext = context;
		mSharePreferDB = new SharePreferDB(mContext, "dk_device_info");
	}
	
	/*
	 * 存储信息
	 */
	public void saveData(Map<String, String> map){
		mSharePreferDB.saveData(map);
	}
	
	/*
	 * 读取文件信息
	 */
	private Map<String, String> readUserData(){
		return mSharePreferDB.readData();
	}
	
	/*
	 * 得到设备token
	 */
	public String getDevice_token() {
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.device_token);
	}
}
