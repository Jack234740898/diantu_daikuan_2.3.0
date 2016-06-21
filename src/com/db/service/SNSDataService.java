package com.db.service;

import android.content.Context;

import com.constants.SNSConstants;
import com.db.SharePreferDB;

import java.util.Map;

/*
 * 通过读取文件取得相应平台下用户的信息
 */
public class SNSDataService {

	private Context mActivity;
	private SharePreferDB mSharePreferDB;
	public SNSDataService(Context activity,String filename){
		mActivity = activity;
		mSharePreferDB = new SharePreferDB(mActivity, filename);
	}
	
	/*
	 * 存储信息
	 */
	public void saveData(Map<String, String> maps){
		mSharePreferDB.saveData(maps);
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
	private Map<String, String> readSNSData(){
		return mSharePreferDB.readData();
	}
	
	/*
	 * 读取本地文件取得之前绑定登陆过后的uid
	 */
	public String getNewUserID(){
		Map<String, String> map = readSNSData();
		if(null != map && !map.isEmpty()){
			return map.get(SNSConstants.NEW_USER_ID);
		}
		return null;
	}
	/*
	 * 取得openID或uid
	 */
	public String getUserid(){
		Map<String, String> map = readSNSData();
		if(null != map && !map.isEmpty()){
			return map.get(SNSConstants.OPENID_KEY);
		}
		return null;
	}
	
	/*
	 * 取得username
	 */
	public String getUsername(){
		Map<String, String> map = readSNSData();
		if(null != map && !map.isEmpty()){
			return map.get(SNSConstants.USERNAME_KEY);
		}
		return null;
	}
	/*
	 * 取得userheadurl
	 */
	public String getUserheadurl(){
		Map<String, String> map = readSNSData();
		if(null != map && !map.isEmpty()){
			return map.get(SNSConstants.USER_HEAD_URL_KEY);
		}
		return null;
	}
	/*
	 * 取得access_token
	 */
	public String getAccess_token(){
		Map<String, String> map = readSNSData();
		if(null != map && !map.isEmpty()){
			return map.get(SNSConstants.ACCESS_TOKEN_KEY);
		}
		return null;
	}
	
	
	/*
	 * 取得access_token
	 */
	public String getExpires_in(){
		Map<String, String> map = readSNSData();
		if(null != map && !map.isEmpty()){
			return map.get(SNSConstants.EXPIRESIN_KEY);
		}
		return null;
	}
	
	/*
	 * 取得qq微博OauthTokenSecret_Key
	 */
	public String getOauthTokenSecret_Key(){
		Map<String, String> map = readSNSData();
		if(null != map && !map.isEmpty()){
			return map.get(SNSConstants.OauthTokenSecret_Key);
		}
		return null;
	}
	
	/*
	 * 取得qq微博OauthTokenSecret_Key
	 */
	public String getOauthVerifier_Key(){
		Map<String, String> map = readSNSData();
		if(null != map && !map.isEmpty()){
			return map.get(SNSConstants.OauthVerifier_Key);
		}
		return null;
	}
}
