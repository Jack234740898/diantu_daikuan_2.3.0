package com.net.service;

import android.content.Context;

import com.constants.GlobalFlag;
import com.constants.InterfaceParams;
import com.constants.ParamsKey;
import com.db.DeviceData;
import com.db.UserData;
import com.db.service.DeviceDataService;
import com.db.service.UserDataService;
import com.net.NetUrl;
import com.net.OkHttpClientManager;
import com.utils.DeviceUtil;
import com.utils.JSONUtil;
import com.utils.LogUtil;
import com.utils.MD5;
import com.utils.StringUtil;
import com.utils.URLEncodUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserJsonService extends BaseJSONService{

	private static final String TAG = "UserJsonService";
	public UserJsonService(Context con) {
		super(con);
	}

	/*
	 * 设备注册 device/registerIncludeAppId
	 */
	public JSONObject device_register() {
		String deviceInfo = DeviceUtil.getEncryptDeviceInfo(mContext);
		//String encode = URLEncodUtil.getEncodeStr(deviceInfo);
		String params = InterfaceParams.device_registerIncludeAppId;
		Map<String, String> paramMaps = new HashMap<String, String>();
		paramMaps.put("data", deviceInfo);
		mNeedCach = false;
		JSONObject jsonObject = getJSON(null, params, paramMaps, true);
		LogUtil.d(TAG, "device_register()== encode is "+",jsonObject is "+jsonObject+",deviceInfo is "+deviceInfo);
		if(null==jsonObject)
			return null;
		jsonObject = jsonObject.optJSONObject("data");
		if(null!=jsonObject){
			String token = jsonObject.optString("token");
			if(StringUtil.checkStr(token)){
				GlobalFlag.global_token =DeviceData.device_token= token;
				DeviceDataService udService = new DeviceDataService(mContext);
				Map<String, String> maps = new HashMap<String, String>();
				maps.put(ParamsKey.device_token, token);
				udService.saveData(maps);
			}
		}
		return jsonObject;
	}
	
	/*
	 * 登录 user2/login
	 */
	public Boolean user2_login(String name,String password){
		String params = InterfaceParams.user2_login;
		Map<String, String> paramMaps = new HashMap<String, String>();
		password = MD5.Md5(password);
		paramMaps.put("name", name);
		paramMaps.put("password", password);
		JSONObject jsonObject = getJSON(null, params, paramMaps, true);
		if(null==jsonObject)
			return false;
		jsonObject = jsonObject.optJSONObject("data");
		return saveLoginInfo(jsonObject);
		/*String str = mNetRequService.requestData("POST", params, paramMaps,
				mNeedCach);
		LogUtil.d(TAG, "user2_login==str is "+str);
		if (!StringUtil.checkStr(str))
			return false;
		try {
			JSONObject jsonObject = new JSONObject(str);
			jsonObject = jsonObject.optJSONObject("data");
			return saveLoginInfo(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}

	/*
	 * 主线程调用
	 */
	public Boolean saveLoginInfo(JSONObject jsonObject) {
		LogUtil.d(TAG, "saveLoginInfo==jsonObject is "+jsonObject);
		if(null==jsonObject)
			return false;
		String token = jsonObject.optString("token");
		String openIMUserId = jsonObject.optString("openIMUserId");
		String openIMPassword = jsonObject.optString("openIMPassword");
		JSONObject user = jsonObject.optJSONObject("user");
		if(null==user){
			String message = jsonObject.optString("message");
			//ToastUtil.showToast(mContext, 0, message+"", true);
			return false;
		}
		String authId = jsonObject.optString("authId");
		String productSetting = jsonObject.optString("productSetting");
		String unReadMessageCount = jsonObject.optString("unReadMessageCount");
		
		String id = user.optString("id");
		String wxid = user.optString("wxid");
		String wxname = user.optString("wxname");
		String mobile = user.optString("mobile");
		String userName = user.optString("userName");
		String nickName = user.optString("nickName");
		String cardId = user.optString("cardId");
		String avatar = user.optString("avatar");
		String type = user.optString("type");
		//String avatar = user.optString("avatar")
		UserDataService userService = new UserDataService(mContext);
		Map<String, String> map = new HashMap<String, String>();
		map.put(ParamsKey.user_id, id);
		map.put(ParamsKey.user_nick, nickName);
		map.put(ParamsKey.user_realname, userName);
		if(StringUtil.checkStr(token)){
			GlobalFlag.global_token = token;
			map.put(ParamsKey.user_token, token);
		}
		map.put(ParamsKey.user_phone, mobile);
		map.put(ParamsKey.user_head, avatar);
		map.put(ParamsKey.openIMUserId, openIMUserId);
		map.put(ParamsKey.openIMPassword, openIMPassword);
		userService.saveData(map);
		new UserData(mContext).setUserData();
		return true;
	}
	
	/*
	 * 注册发送短信验证码
	 * /user2/sendRegCode
	 */
	public JSONObject user2_sendRegCode(String mobile){
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("mobile", mobile);
		String param = InterfaceParams.user2_sendRegCode;
		return getJSON(null, param, paramsMap, true);
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 提交注册验证码
	 * user2/register
	 */
	public JSONObject user2_register(String mobile,String verifyCode){
		String params = InterfaceParams.user2_register;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("mobile", mobile);
		paramsMap.put("verifyCode", verifyCode);
		return getJSON(null, params, paramsMap, true);
		/*String str = mNetRequService.requestData("POST", params, paramsMap, false);
		if(!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 注册设置名称与密码
	 * user2/updateNameAndPassword
	 */
	public JSONObject user2_updateNameAndPassword(String userName,String password){
		Map<String, String> paramsMap = new HashMap<String, String>();
		password = MD5.Md5(password);
		paramsMap.put("userName", URLEncodUtil.getEncodeStr(userName));
		paramsMap.put("password", password);
		String params = InterfaceParams.user2_updateNameAndPassword;
		return getJSON(null, params, paramsMap, true);
		/*String str = mNetRequService.requestData("POST", params, paramsMap, false);
		if(!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 找回密码 发送短信验证码
	 * /user2/forgetPassword
	 */
	public JSONObject user2_forgetPassword(String mobile){
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("mobile", mobile);
		String param = InterfaceParams.user2_forgetPassword;
		mNeedCach = false;
		return getJSON(null, param, paramsMap, true);
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 找回密码 验证短信验证码
	 * user2/verifyPasswordCode
	 */
	public JSONObject user2_verifyPasswordCode(String mobile,String verifyCode){
		String params = InterfaceParams.user2_verifyPasswordCode;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("mobile", mobile);
		paramsMap.put("verifyCode", verifyCode);
		return getJSON(null, params, paramsMap, true);
		/*String str = mNetRequService.requestData("POST", params, paramsMap, false);
		if(!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 修改密码 发送短信验证码
	 * user2/sendVCode3
	 */
	public JSONObject user2_sendVCode3(){
		Map<String, String> paramsMap = new HashMap<String, String>();
		String param = InterfaceParams.user2_sendVCode3;
		return getJSON(null, param, paramsMap, true);
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 修改密码 验证短信验证码
	 * user2/verifyVcode3
	 */
	public JSONObject user2_verifyVcode3(String verifyCode){
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("verifyCode", verifyCode);
		String param = InterfaceParams.user2_verifyVCode3;
		return getJSON(null, param, paramsMap, true);
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 设置新密码
	 * user2/updatePassword
	 */
	public JSONObject user2_updatePassword(String password){
		Map<String, String> paramsMap = new HashMap<String, String>();
		password = MD5.Md5(password);
		paramsMap.put("password", password);
		String param = InterfaceParams.user2_updatePassword;
		return getJSON(null, param, paramsMap, true);
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 意见反馈
	 */
	public boolean feedback_add(String content){
		String param = InterfaceParams.feedback_add;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("content", content);
		mNeedCach = false;
		JSONObject jsonObject = getJSON(null, param, paramsMap, true);
		if(null==jsonObject)
			return false;
		return 200==jsonObject.optInt("code");
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(!StringUtil.checkStr(str)){
			return false;
		}
		try {
			JSONObject jsonObject = new JSONObject(str); 
			int code = jsonObject.optInt("code");
			if(200==code){
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;*/
	}
	/*
	 * 系统消息
	 * message/system
	 */
	public ArrayList<JSONObject> message_system(int pageNo){
		Map<String, String> paramMaps = new HashMap<String, String>();
		paramMaps.put("pageNo", String.valueOf(pageNo));
		paramMaps.put("pageSize", String.valueOf(20));
		String params = InterfaceParams.message_system;
		JSONObject jsonObject = getJSON(null, params, paramMaps, true);
		if(null==jsonObject)
			return null;
		jsonObject = jsonObject.optJSONObject("data");
		if(null==jsonObject)
			return null;
		jsonObject = jsonObject.optJSONObject("messages");
		if(null==jsonObject)
			return null;
		return JSONUtil.arrayToList(jsonObject.optJSONArray("list"));
		/*String str = mNetRequService.requestData("POST", params, paramMaps, mNeedCach);
		if(!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			jsonObject = jsonObject.optJSONObject("data");
			if(null==jsonObject)
				return null;
			jsonObject = jsonObject.optJSONObject("messages");
			if(null==jsonObject)
				return null;
			return JSONUtil.arrayToList(jsonObject.optJSONArray("list"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 注册发送短信验证码
	 * /user2/sendRegCode
	 */
	public JSONObject userRegister_sendRegCode(String mobile){
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("mobile", mobile);
		String param = InterfaceParams.userRegister_sendRegCode;
		return getJSON(null, param, paramsMap, true);
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 提交注册验证码
	 * userRegister/register
	 */
	public JSONObject userRegister_register(String mobile,String verifyCode){
		String params = InterfaceParams.userRegister_register;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("mobile", mobile);
		paramsMap.put("verifyCode", verifyCode);
		return getJSON(null, params, paramsMap, true);
		/*String str = mNetRequService.requestData("POST", params, paramsMap, false);
		if(!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 我的贷款列表
	 * http://test.daigj.com/api/adr/dk/loan/list
	 */
	public ArrayList<JSONObject> applyLoan_list(){
		String param = InterfaceParams.dk_loan_list;
		JSONObject jsonObject = getJSON(null, param, null, true);
		if(null==jsonObject)
			return null;
		int code = jsonObject.optInt("code");
		if(200==code){
			JSONObject data = jsonObject.optJSONObject("data");
			if(null!=data){
				return JSONUtil.arrayToList(data.optJSONArray("list"));
			}
		}
		return null;
		/*Map<String, String> paramsMap = new HashMap<String, String>();
		
		String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(!StringUtil.checkStr(str)){
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(str); 
			int code = jsonObject.optInt("code");
			if(200==code){
				JSONObject data = jsonObject.optJSONObject("data");
				if(null!=data){
					return JSONUtil.arrayToList(data.optJSONArray("list"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * http://test.daigj.com/api/ios/option/list
	 * 借款表单选项
	 * type==1借款用户选择的数据
	 */
	public JSONObject option_list(int type){
		String param = InterfaceParams.option_list;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("type", String.valueOf(type));
		JSONObject jsonObject = getJSON(null, param, paramsMap, true);
		if(null==jsonObject)
			return null;
		JSONObject data = jsonObject.optJSONObject("data");
		//if(null!=data)
			//return data.optJSONObject("options");
		return data;
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				JSONObject data = jsonObject.optJSONObject("data");
				if(null!=data)
					return data.optJSONObject("options");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * 获取用户信息
	 * http://test.daigj.com/api/adr/userRegister/getUser
	 */
	public JSONObject applyLoan_getUserLoanInfo(){
		String param = InterfaceParams.applyLoan_getUserLoanInfo;
		//Map<String, String> paramsMap = new HashMap<String, String>();
		JSONObject jsonObject = getJSON(null, param, null, true);
		if(null==jsonObject)
			return null;
		jsonObject = jsonObject.optJSONObject("data");
		if(null!=jsonObject){
			return jsonObject.optJSONObject("userLoanInfo");
		}
		return null;
		/*String str = mNetRequService.requestData("POST", param, paramsMap, mNeedCach);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				jsonObject = jsonObject.optJSONObject("data");
				if(null!=jsonObject){
					return jsonObject.optJSONObject("userLoanInfo");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * 保存|更新我的资料
	 * http://test.daigj.com/api/adr/applyLoan/updateUserLoanInfo
	 */
	public JSONObject applyLoan_updateUserLoanInfo(int id,String city,String companyName,String companyType,
			String income,String workingAge,String socialSecurityAge,String housingFund,String creditRecord,
			String car,String house,String userName){
		String param = InterfaceParams.applyLoan_updateUserLoanInfo;
		Map<String, String> paramsMap = new HashMap<String, String>();
		
		if(id>0){
			paramsMap.put("id", id+"");
		}
		paramsMap.put("city", city+"");
		paramsMap.put("companyName", companyName+"");
		paramsMap.put("companyType", companyType+"");
		paramsMap.put("income", income+"");
		paramsMap.put("workingAge", workingAge+"");
		paramsMap.put("socialSecurityAge", socialSecurityAge+"");
		paramsMap.put("housingFund", housingFund+"");
		paramsMap.put("creditRecord", creditRecord+"");
		paramsMap.put("car", car+"");
		paramsMap.put("house", house+"");
		paramsMap.put("userName", userName+"");
		mNeedCach = false;
		return getJSON(null, param, paramsMap, true);
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				//jsonObject = jsonObject.optJSONObject("data");
				return jsonObject;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * 单子进度
	 * http://test.daigj.com/api/adr/dk/loan/process
	 */
	public JSONObject dk_loan_process(int loanId){
		String param = InterfaceParams.dk_loan_process;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("loanId", String.valueOf(loanId));
		JSONObject jsonObject = getJSON(null, param, paramsMap, true);
		if(null==jsonObject)
			return null;
		jsonObject = jsonObject.optJSONObject("data");
		if(null!=jsonObject){
			return jsonObject;//.optJSONArray("list");
		}
		return null;
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				jsonObject = jsonObject.optJSONObject("data");
				if(null!=jsonObject){
					return jsonObject;//.optJSONArray("list");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * 单子进度新接口
	 * http://test.daigj.com/api/adr/dk/loan/processv2
	 */
	public JSONObject dk_loan_processv2(int loanId,int productId){
		String param = InterfaceParams.dk_loan_processv2;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("loanId", String.valueOf(loanId));
		paramsMap.put("productId", String.valueOf(productId));
		JSONObject jsonObject = getJSON(null, param, paramsMap, true);
		if(null==jsonObject)
			return null;
		return jsonObject.optJSONObject("data");
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				jsonObject = jsonObject.optJSONObject("data");
				if(null!=jsonObject){
					return jsonObject;//.optJSONArray("list");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * 关闭贷款申请单
	 * http://test.daigj.com/api/adr/loan/close
	 */
	public Boolean  loan_close(int loanId){
		String param = InterfaceParams.loan_close;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("loanId", String.valueOf(loanId));
		JSONObject jsonObject = getJSON(null, param, paramsMap, true);
		if(null==jsonObject)
			return false;
		return 200==jsonObject.optInt("code");
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				return 200==jsonObject.optInt("code");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;*/
	}
	
	/*
	 * 利息计算
	 * http://test.daigj.com/api/adr/dk/calc
	 */
	public JSONObject dk_calc(String amount,String term){
		String param = InterfaceParams.dk_calc;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("amount",amount);
		paramsMap.put("term", term);
		JSONObject jsonObject = getJSON(null, param, paramsMap, true);
		if(null==jsonObject)
			return null;
		return jsonObject.optJSONObject("data");
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				return jsonObject.optJSONObject("data");
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * 校验并且贷款发单
	 * http://test.daigj.com/api/adr/applyLoan/verifyAndAdd
	 */
	public JSONObject applyLoan_verifyAndAdd(String userName, String amount,String term,String city, 
			String companyName,String income,String socialSecurityAge, String creditRecord,String useType,
			String debtType, String guaranteeType,String age,String sex,String mobile,String verifyCode,int mobileFlag){
		String param = InterfaceParams.applyLoan_verifyAndAdd;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userName", userName);
		paramsMap.put("amount", amount);
		paramsMap.put("term", term);
		paramsMap.put("city", city);
		paramsMap.put("companyName", companyName);
		paramsMap.put("income", income);
		paramsMap.put("socialSecurityAge", socialSecurityAge);
		paramsMap.put("creditRecord", creditRecord);
		paramsMap.put("useType", useType);
		paramsMap.put("debtType", debtType);
		paramsMap.put("guaranteeType", guaranteeType);
		paramsMap.put("age", age);
		paramsMap.put("sex", sex);
		paramsMap.put("mobile", mobile);
		paramsMap.put("verifyCode", verifyCode);
		paramsMap.put("mobileFlag", mobileFlag+"");
		mNeedCach = false;
		JSONObject jsonObject = getJSON(null,param, paramsMap, true);
		if(null==jsonObject)
			return null;
		return jsonObject.optJSONObject("data");
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				return jsonObject.optJSONObject("data");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * 登陆态下贷款发单
	 * http://test.daigj.com/api/adr/applyLoan/add
	 */
	public JSONObject applyLoan_add(int productId,String userName, String amount,String term,String city,
			String househodeCity, String companyName,String income,String incomeCash,String socialSecurityAge,
			String creditRecord,int workingAge,String debtType, String guaranteeType,String age,String sex,
			int mobileFlag,String riskRaw,int workingIdentity,int loanCategory,int guaranteeWill,
			String message,String hasCreditCard,String operatePeriod,
			String startSchoolTime,String housingFund,String housingFundAge,String businessLicense,int specialStatus
			,int lengthOfSchool,int grade,String loanUsed){
		String param = InterfaceParams.applyLoan_add;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("productId", productId+"");
		paramsMap.put("userName", userName);
		paramsMap.put("amount", amount);
		paramsMap.put("term", term);
		paramsMap.put("city", city);
		paramsMap.put("househodeCity", househodeCity);
		paramsMap.put("companyName", companyName);
		paramsMap.put("income", income);
		paramsMap.put("incomeCash", incomeCash);
		paramsMap.put("socialSecurityAge", socialSecurityAge);
		paramsMap.put("creditRecord", creditRecord);
		paramsMap.put("workingAge", String.valueOf(workingAge));
		paramsMap.put("debtType", debtType);
		paramsMap.put("guaranteeType", guaranteeType);
		paramsMap.put("age", age);
		paramsMap.put("sex", sex);
		paramsMap.put("mobileFlag", mobileFlag+"");
		paramsMap.put("message", message);
		paramsMap.put("riskRaw", riskRaw);
		paramsMap.put("startSchoolTime", startSchoolTime);
		paramsMap.put("workingIdentity", String.valueOf(workingIdentity));
		//paramsMap.put("loanCategory", String.valueOf(loanCategory));
		paramsMap.put("guaranteeWill", String.valueOf(guaranteeWill));
		paramsMap.put("hasCreditCard", String.valueOf(hasCreditCard));
		paramsMap.put("operatePeriod", String.valueOf(operatePeriod));
		paramsMap.put("housingFund", String.valueOf(housingFund));
		paramsMap.put("housingFundAge", String.valueOf(housingFundAge));
		paramsMap.put("businessLicense", businessLicense);
		paramsMap.put("lengthOfSchool", String.valueOf(lengthOfSchool));
		paramsMap.put("grade", String.valueOf(grade));
		paramsMap.put("loanUsed", loanUsed);
		if(specialStatus>=0){
			paramsMap.put("specialStatus", String.valueOf(specialStatus));
		}
		mNeedCach = false;
		JSONObject jsonObject = getJSON(null,param, paramsMap, true);
		if(null==jsonObject)
			return null;
		return jsonObject.optJSONObject("data");
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				return jsonObject.optJSONObject("data");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * 贷款通知进度
	 * http://test.daigj.com/api/adr/applyLoan/progress
	 */
	public JSONObject applyLoan_progress(int loanId){
		String param = InterfaceParams.applyLoan_progress;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("loanId", String.valueOf(loanId));
		mNeedCach = false;
		JSONObject jsonObject = getJSON(null,param, paramsMap, true);
		if(null==jsonObject)
			return null;
		return jsonObject.optJSONObject("data");
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				return jsonObject.optJSONObject("data");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * 结束抢单
	 * http://test.daigj.com/api/adr/loan/closeSeckill
	 */
	public Boolean loan_closeSeckill(int loanId){
		String param = InterfaceParams.loan_closeSeckill;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("loanId", String.valueOf(loanId));
		mNeedCach = false;
		JSONObject jsonObject = getJSON(null, param, paramsMap, true);
		if(null==jsonObject)
			return false;
		return 200==jsonObject.optInt("code");
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				return jsonObject.optInt("code")==200;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;*/
	}
	
	/*
	 * 获取用户信息
	 * http://test.daigj.com/api/adr/userRegister/getUser
	 */
	public JSONObject userRegister_getUser(){
		String param = InterfaceParams.userRegister_getUser;
		JSONObject jsonObject = getJSON(null, param, null, true);
		if(null==jsonObject)
			return null;
		jsonObject = jsonObject.optJSONObject("data");
		if(null!=jsonObject){
			return jsonObject.optJSONObject("user");
		}
		return null;
		/*Map<String, String> paramsMap = new HashMap<String, String>();
		String str = mNetRequService.requestData("POST", param, paramsMap, mNeedCach);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				jsonObject = jsonObject.optJSONObject("data");
				if(null!=jsonObject){
					return jsonObject.optJSONObject("user");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * 批量用户信息获取
	 * http://test.daigj.com/api/adr/user2/getUserInfo
	 */
	public JSONArray user2_getUserInfo(String ids){
		String param = InterfaceParams.user2_getUserInfo;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("ids", ids);
		JSONObject jsonObject = getJSON(null, param, paramsMap, true);
		if(null==jsonObject)
			return null;
		jsonObject = jsonObject.optJSONObject("data");
		if(null!=jsonObject){
			return jsonObject.optJSONArray("list");
		}
		return null;
		/*String str = mNetRequService.requestData("POST", param, paramsMap, mNeedCach);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				jsonObject = jsonObject.optJSONObject("data");
				if(null!=jsonObject){
					return jsonObject.optJSONArray("list");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * 用户查询
	 * http://test.daigj.com/api/adr/user2/get
	 */
	public JSONObject user2_get(){
		String param = InterfaceParams.user2_get;
		JSONObject jsonObject = getJSON(null, param, null, true);
		if(null==jsonObject)
			return null;
		return jsonObject.optJSONObject("data");
		/*Map<String, String> paramsMap = new HashMap<String, String>();
		String str = mNetRequService.requestData("POST", param, paramsMap, mNeedCach);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				jsonObject = jsonObject.optJSONObject("data");
				return jsonObject;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * http://test.daigj.com/api/adr/upload/pictures
	 * 图片上传
	 */
	public JSONObject upload_pictures(String filePath){
		String server_url = NetUrl.getHost()+InterfaceParams.upload_pictures;
		//fileMaps.put("pictures", pictures);
		String json = new OkHttpClientManager(mContext).postFile(filePath, server_url, null);//HttpClientUtil.allInfoPost(server_url, paramMaps, fileMaps);
		LogUtil.d(TAG, "upload_pictures==filePath is "+filePath+",json is "+json);
		if(StringUtil.checkStr(json))
			try {
				return new JSONObject(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	/*
	 * 更新用户头像
	 * http://test.daigj.com/api/adr/user2/updateAvatar
	 */
	public boolean user2_updateAvatar(String avatar){
		String param = InterfaceParams.user2_updateAvatar;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("avatar", avatar);
		mNeedCach = false;
		JSONObject jsonObject = getJSON(null, param, paramsMap, true);
		if(null==jsonObject)
			return false;
		return 200==jsonObject.optInt("code");
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				return 200==jsonObject.optInt("code");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;*/
	}
	
	/*
	 * 打分评论
	 * http://test.daigj.com/api/adr/score/add
	 */
	public Boolean score_add(int investId,int userId,String comment,float score){
		String param = InterfaceParams.score_add;
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("investId", String.valueOf(investId));
		paramsMap.put("userId", String.valueOf(userId));
		paramsMap.put("comment", comment);
		paramsMap.put("score", String.valueOf(score));
		mNeedCach = false;
		JSONObject jsonObject = getJSON(null, param, paramsMap, true);
		if(null==jsonObject)
			return false;
		return 200==jsonObject.optInt("code");
		/*String str = mNetRequService.requestData("POST", param, paramsMap, false);
		LogUtil.d(TAG, "score_add==investId is "+investId+",userId is "+userId+",comment is "+comment
				+",score is "+score+",str is "+str);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				return 200==jsonObject.optInt("code");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;*/
	}
	/*分享信息
	 * http://test.daigj.com/api/adr/dk/user/share
	 */
	public JSONObject dk_user_share(){
		String param = InterfaceParams.dk_user_share;
		mNeedCach = false;
		JSONObject jsonObject = getJSON(null,param, null, true);
		if(null==jsonObject)
			return null;
		jsonObject = jsonObject.optJSONObject("data");
		if(null!=jsonObject)
			return jsonObject.optJSONObject("shareInfo");
		return null;
		/*Map<String, String> paramsMap = new HashMap<String, String>();
		String str = mNetRequService.requestData("POST", param, paramsMap, false);
		if(StringUtil.checkStr(str)){
			try {
				JSONObject jsonObject = new JSONObject(str);
				jsonObject = jsonObject.optJSONObject("data");
				if(null!=jsonObject)
					return jsonObject.optJSONObject("shareInfo");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;*/
	}
	
	/*
	 * 上传身份照片：
	 * http://test.daigj.com/api/ios/applyLoan/updateIdentityImg
	 */
	public boolean applyLoan_updateIdentityImg(int loanId,String identityImg){
		String param = InterfaceParams.applyLoan_updateIdentityImg;
		HashMap<String,String> paramMaps = new HashMap<String, String>();
		paramMaps.put("loanId", String.valueOf(loanId));
		paramMaps.put("identityImg",identityImg);
		mNeedCach = false;
		JSONObject jsonObject = getJSON(null, param, paramMaps, true);
		if(null==jsonObject)
			return false;
		return 200==jsonObject.optInt("code");
	}
}
