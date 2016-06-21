package com.db.service;

import android.content.Context;

import com.constants.ParamsKey;
import com.constants.UserDataConstants;
import com.db.SharePreferDB;
import com.utils.StringUtil;

import java.util.Map;

/*
 * 用户相关数据的存储与获取
 */
public class UserDataService {

	private Context mActivity;
	private SharePreferDB mSharePreferDB;
	public UserDataService(Context activity){
		mActivity = activity;
		mSharePreferDB = new SharePreferDB(mActivity, UserDataConstants.USER_DATA_FILE_NAME);
	}
	
	/*
	 * 存储信息
	 */
	public void saveData(Map<String, String> map){
		mSharePreferDB.saveData(map);
	}
	
	/*
	 * 得到用户的openIMUserId
	 */
	public String getOpenIMUserId(){
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.openIMUserId);
	}
	/*
	 * 得到用户的openIMPassword
	 */
	public String getopenIMPassword(){
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.openIMPassword);
	}
	/*
	 * 得到用户的userID
	 */
	public String getUserId(){
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.user_id);
	}
	/*
	 * 得到用户的token信息
	 */
	public String getUserToken(){
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.user_token);
	}
	
	/*
	 * 得到用户的电话号码
	 */
	public String getUserPhone(){
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.user_phone);
	}
	
	/*
	 * 得到用户昵称
	 */
	public String getUserNick(){
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.user_nick);
	}
	
	/*
	 * 得到用户真实姓名
	 */
	public String getRealName(){
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.user_realname);
	}
	public String getBirthday(){
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.user_birthday);
	}
	
	public String getUser_gender(){
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.user_gender);
	}
	
	public String getUser_age(){
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.user_age);
	}
	public String getUser_head(){
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.user_head);
	}
	public String getUser_pwd(){
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.user_pwd);
	}
	
	public String getEmail_auth() {
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.email_auth);
	}
	
	public String getMobile_auth() {
		Map<String, String> maps = readUserData();
		if(null == maps) return null;
		return maps.get(ParamsKey.mobile_auth);
	}
	
	/*
	 * 得到指定电话号码是否有设置过密码
	 * 0为没有，其他为有
	 */
	public int hasPwd(){
		Map<String, String> maps = readUserData();
		if(null == maps) return 0;
		if(!StringUtil.isEmpty(getUserPhone())){
			String haspwd = maps.get(getUserPhone());
			if(!StringUtil.isEmpty(haspwd))
				return Integer.parseInt(haspwd);
		}
		return 0;
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
