package com.db.service;

import android.content.Context;

import com.constants.UserDataConstants;
import com.db.SharePreferDB;
import com.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/*
 * 本地数据缓存业务类
 * key，value形式写入本地文件
 */
public class LocalCachService {

	private Context mContext;
	private SharePreferDB mSharePreferDB;
	public LocalCachService(Context context,String userID){
		mContext = context;
		if(StringUtil.checkStr(userID)){
			mSharePreferDB = new SharePreferDB(context, userID+UserDataConstants.USER_CACH_DATA_FILE_NAME);
		}else{
			mSharePreferDB = new SharePreferDB(context, UserDataConstants.USER_CACH_DATA_FILE_NAME);
		}
	}
	/*
	 * 缓存数据，存数据
	 */
	public void cachData(String key,String result){
		if(StringUtil.checkStr(key) && StringUtil.checkStr(result)){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(key, result);
			mSharePreferDB.saveData(map);
		}
	}
	/*
	 * 取缓存数据，返回一个json的字符串
	 */
	public String getCachData(String key){
		if(!StringUtil.checkStr(key))return null;
		Map<String, String> cachMap = mSharePreferDB.readData();
		if(null !=cachMap){
			return cachMap.get(key);
		}
		return null;
	}
	
	/*
	 * 清除缓存
	 */
	public void clearCachData(){
		mSharePreferDB.deletePreference();
	}
}
