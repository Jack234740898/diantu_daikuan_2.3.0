package com.db.service;

import android.content.Context;

import com.constants.ParamsKey;
import com.db.SharePreferDB;

import java.util.Map;

/*
 * GPS
 */
public class GPSDataService {

	private Context mActivity;
	private SharePreferDB mSharePreferDB;
	public GPSDataService(Context activity){
		mActivity = activity;
		mSharePreferDB = new SharePreferDB(mActivity, "gps");
	}
	
	/*
	 * 存储信息
	 */
	public void saveData(Map<String, String> map){
		mSharePreferDB.saveData(map);
	}
	/*
	 * 清除本地文件数据
	 */
	public void clearData(){
		mSharePreferDB.deletePreference();
	}
	/*
	 * 读取文件信息
	 */
	private Map<String, String> readUserData(){
		return mSharePreferDB.readData();
	}
	
	
	public String getGps_local(){
		Map<String, String> maps = readUserData();
		if(null==maps)
			return "";
		return maps.get(ParamsKey.gps_local);
	}
}
