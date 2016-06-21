package com.db;

import android.content.Context;
import android.widget.TextView;

import com.constants.GlobalFlag;
import com.db.service.UserDataService;


/*
 * 用户对象数据
 */
public class UserData {

	private Context mContext;
	public UserData(Context con){
		this.mContext = con;
	}
	public static String userRealName;
	public static String usernick;
	public static String userId="" ;
	public static String userToken="";
	public static String userPhone;
	public static String user_birthday;
	public static String user_password;
	public static int haspwd;//0为无密码，其他为有密码
	public static String user_age;
	public static String user_gender;
	public static String openIMUserId;
	public static String openIMPassword;
	
	public static String email = "";;
	public static String email_auth = "";;
	public static String mobile_auth = "";
	public static boolean topstatus = false;
	public static String qq = "";
	
	public static int new_remind_count=0;
	public static int new_message_count=0;
	public static int new_letter_count=0;
	public static TextView my_remind_text;
	
	public static String universityName;
	/*推荐应用显示app的数量
	 * */
	public static String App_limit="100";
	public static String deviceIp;
	/*
	 * 账户"balance":"0.00","blocked_balance"
	 */
	public static String user_head;
	public static String userPwd;
	
	public static int usertype;
	
	public static String local_city="";
	
	public static String device_token = "";
	/*
	 * 静态变量赋值
	 */
	public void setUserData() {
		UserDataService userService = new UserDataService(mContext);
		UserData.userId = userService.getUserId();
		GlobalFlag.global_token = UserData.userToken = userService.getUserToken();
		UserData.usernick = userService.getUserNick();
		UserData.userPhone = userService.getUserPhone();
		UserData.userPwd = userService.getUser_pwd();
		UserData.userRealName = userService.getRealName();
		UserData.user_birthday = userService.getBirthday();
		UserData.user_head = userService.getUser_head();
		UserData.user_gender = userService.getUser_gender();
		UserData.user_age = userService.getUser_age();
		UserData.email_auth = userService.getEmail_auth();
		UserData.mobile_auth = userService.getMobile_auth();
		UserData.haspwd = userService.hasPwd();
		UserData.openIMPassword = userService.getopenIMPassword();
		UserData.openIMUserId = userService.getOpenIMUserId();
	}
	
	/*
	 * 退出登录静态变量赋值
	 */
	public void logout() {
		UserDataService userService = new UserDataService(mContext);
		userService.clearData();
		UserData.userId = "";
		UserData.userToken = "";
		UserData.usernick = "";
		UserData.userPhone = "";
		UserData.userRealName = "";
		UserData.user_birthday = "";
		UserData.email_auth ="";
		UserData.mobile_auth = "";
		UserData.haspwd=0;
		GlobalFlag.global_token = DeviceData.device_token;
	}
}
