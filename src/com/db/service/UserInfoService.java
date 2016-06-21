package com.db.service;

import com.constants.ParamsKey;
import com.constants.UserDataConstants;
import com.db.SharePreferDB;
import com.sxjs.diantu_daikuan.MyApplication;

import java.util.Map;

/*
 * 用户相关数据的存储与获取
 */
public class UserInfoService {

	private MyApplication mContext;
	private SharePreferDB mSharePreferDB;
	public UserInfoService(MyApplication context,String userId){
		this.mContext = context;
		mSharePreferDB = new SharePreferDB(mContext, UserDataConstants.USER_DATA_FILE_NAME+userId);
		
	}
	
	/*
	 * 存储信息
	 */
	public void saveData(Map<String, String> map){
		mSharePreferDB.saveData(map);
	}
	
	/*
	 * 得到用户的头像
	 */
	public String getUserHead(){
		Map<String, String>maps = readUserData();
		if(null!=maps){
			return maps.get(ParamsKey.user_head);
		}
		return null;
	}
	
	/*
	 * 得到用户的真实姓名
	 */
	public String getUserRealName(){
		Map<String, String>maps = readUserData();
		if(null!=maps){
			return maps.get(ParamsKey.user_realname);
		}
		return null;
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
	
}
