package com.constants;
//import com.ymall.presentshop.db.UserData;

/*
 * 用户个人数据信息，及其存储相应的key值
 */
public class UserDataConstants {

	public static final String USER_DATA_FILE_NAME = "sxjs_userdata";//用户私有数据的文件名
	
	public static final String USER_HEAD_URL_KEY = "userhead";
	public static final String USER_NAME_KEY = "username";
	public static final String NEW_USER_ID = "newuserid";
	public static final String NEW_USER_TOKEN = "newusertoken";
	public static final String NEW_USER_PHONE = "newuserphone";
	//public static String HAS_PWD = UserData.userPhone;
	
	public static final String USER_CACH_DATA_FILE_NAME = "usercachdata";//用户的缓存数据文件名(包含登陆和未登录)

	//public static final String CACH_CONTENT_KEY ;//用户的缓存数据文件内容的key值  根据请求的接口编号和请求参数生成
	
	public static final int CACH_DATA = 1;//缓存数据
	public static final int REQUEST_DATA = 2;//网络请求数据
	

	/*
	 * 第三方登录授权后的uid和token信息
	 */
	public static String sinaUid;
	public static String sinaToken;
	public static String qqUid;
	public static String qqToken;
	public static String qqExpires_in;
	
	/*
	 * qqweibo相关
	 */
	public static String qqweiboUid;
	public static String qqweiboToken;
	public static String oauthConsumerSecret;
	public static String oauthTokenSecret;
	public static String qqweibo_secret;
	public static String qqweibo_verifier;
	
	/*
	 * 淘宝相关
	 */
	public static String taobaoUid;
	public static String taobaoToken;
	
	/*
	 * 分享成功与否的状态标识
	 */
	public static final int SHARE_SUCCESS = 1;
	public static final int SHARE_FAILURED = -1;
}
