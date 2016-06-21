package com.constants;

/*
 * sns相关的常量及绑定登陆后新uid存储的keu值
 * 1新浪、2腾讯微博、3QQ、4淘宝
 */
public class SNSConstants {

	public static final String SOURCE_ID = "sourceid";
	public static final int SINAWEIBO_ID = 1;
	public static final int QQWEIBO_ID = 2;
	public static final int QQSPACE_ID = 3;
	public static final int TAOBAO_ID = 4;
	
	public static final String USERID_FILE_NAME = "newuserid";
	public static final String SINA_TOKEN_FILE_NAME = "sinaweibo";//新浪token存储文件名
	public static final String QQSPACE_TOKEN_FILE_NAME = "qqspace";//qq空间token存储文件名
	public static final String QQWEIBO_TOKEN_FILE_NAME = "qqweibo";//qq微博token存储文件名
	public static final String TAOBAO_TOKEN_FILE_NAME = "taobaotoken";//淘宝token存储文件名
	
	public static final String NEW_USER_ID = "newuserid";
	public static final String OPENID_KEY = "openid";
	public static final String ACCESS_TOKEN_KEY = "access_token";
	public static final String EXPIRESIN_KEY = "expires_in";
	public static final String USERNAME_KEY = "username";
	public static final String USER_HEAD_URL_KEY = "userheadurl";
	
	public static final String OauthTokenSecret_Key = "OauthTokenSecret";
	public static final String OauthVerifier_Key = "OauthVerifier";
	
	public static final String SHOP_NAME = "shopname" ;//店铺名称
	public static final String SHOP_SUMMERY = "shopsummery" ;//店铺简介
	public static final String SHARE_URL = "shareurl" ;//分享链接
	public static final String IMAGE_URL = "imgurl" ;//图片链接
	
	//public static OAuthV1 validOauth;
	
	public static final String IS_BIND_LOGIN = "isbindlogin";//是否绑定登陆的key
	/*
	 * 分享参数相应的key值
	 */
	public static final String SHARE_CONTENT_KEY = "content" ;//分享的内容
	public static final String SHARE_IMAGE_URL_KEY = "imgurl" ;//分享 的本地图片地址
	
	/*
	 * 分享成功与否
	 */
	public static final int SHARE_SUCCESS = 1 ;//分享成功
	public static final int SHARE_FAILURED = -1 ;//分享 失败
	
	/*
	 * 淘宝相关
	 */
	public static final int TAOBAO_BACK_CODE = 10 ;//淘宝授权完后ActivityForResult的requestcode
	
	//public static final String TAOBAO_LOGION_ACTION = "com.yoka.goodshop.loginback.action";//淘宝登陆完之后的action
	
	/*
	 * QQ微博相关
	 */
	public static final int QQWEIBO_REQUEST_CODE = 10;
	public static final int QQWEIBO_BIND_SUCCESS = 102;
	public static final int QQWEIBO_BIND_FAILURE = -102;
	public static final int QQWEIBO_BIND_LOGIN = 99;
	
	/*
	 * QQ相关
	 */
	public static final int QQ_REQUEST_CODE = 12;
	
	public static final int QQ_BIND_SUCCESS = 101;
	public static final int QQ_BIND_FAILURE = -101;
	public static final int QQ_SHARE = 98;
	/*
	 * 新浪微博相关
	 */
	public static final int SINA_BIND_SUCCESS = 103;
	public static final int SINA_BIND_FAILURE = -103;
	public static final int SINA_SHARE = 100;
	public static final int SINA_WEIBO_REQUEST_CODE = 11;//新浪微博请求码REQUEST_CODE
	public static final int AUTH_SUCCESS = 1;//auth认证成功
	public static final int SINAWEIBO_SHARE = 96;
	public static final int NEED_BIND_LOGIN = 2;
}
