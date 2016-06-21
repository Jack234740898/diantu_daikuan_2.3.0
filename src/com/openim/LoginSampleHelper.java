package com.openim;

import android.app.Application;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.login.IYWConnectionListener;
import com.alibaba.mobileim.login.YWLoginCode;

/**
 * SDK 初始化和登录
 * 
 * @author jing.huai
 */
public class LoginSampleHelper {

	private static LoginSampleHelper sInstance = new LoginSampleHelper();

	public static LoginSampleHelper getInstance() {
		return sInstance;
	}

	// 应用APPKEY，这个APPKEY是申请应用时获取的
	//private static final String APP_KEY = "23227021";//"23015524";
	// openIM UI解决方案提供的相关API，创建成功后，保存为全局变量使用
	private YWIMKit mIMKit;

	public YWIMKit getIMKit() {
		return mIMKit;
	}

	/**
	 * 初始化SDK
	 * 
	 * @param context
	 */
	public void initSDK_Sample(Application context) {
		YWAPI.init(context, IMConfig.dgj_APP_KEY);// 设置Appkey,在申请sdk的时候获取
		
		// 用于获取云旺的聊天ui相关的API接口。
		mIMKit = YWAPI.getIMKitInstance();

	}

	/**
	 * 登录操作
	 * 
	 * @param userId
	 *            用户id
	 * @param password
	 *            密码
	 * @param callback
	 *            登录操作结果的回调
	 */
	//------------------请特别注意，OpenIMSDK会自动对所有输入的用户ID转成小写处理-------------------
	//所以开发者在注册用户信息时，尽量用小写
	public void login_Sample(String userId, String password,
			IWxCallback callback) {

		//为了TOKEN登录验证模板消息增加的逻辑。已注释。
//		JSONObject object = new JSONObject();
//		try {
//			object.put("t","1436853338");
//			object.put("sign","6a30d371b80b02f782aeae3fc991d3e876de4d36");
//			object.put("api","mtop.user.getUserSimple");
//			JSONObject data = new JSONObject();
//			data.put("nick","wuwei003");
//			object.put("data",data.toString());
//			object.put("appKey","531772");
//			object.put("sid","0c29e7301367ed24632fb97e54e48bac");
//			object.put("v","1.0");
//			object.put("ttid","201200@taobao_iphone_10.9.9");
//			object.put("deviceId","AiAsTBKSmIqud3hzxMiH2NmzQFDVITBCoUZmm5EWKGZX");
//
//		}catch (Exception e){
//
//		}
//		userId="wuwei003";
////		password="{\"t\":\"1436853338\",\"sign\":\"6a30d371b80b02f782aeae3fc991d3e876de4d36\",\"data\":{\"nick\":\"wuwei003\"},\"api\":\"mtop.user.getUserSimple\",\"appKey\":\"531772\",\"sid\":\"0c29e7301367ed24632fb97e54e48bac\",\"v\":\"1.0\",\"ttid\":\"201200@taobao_iphone_10.9.9\",\"deviceId\":\"AiAsTBKSmIqud3hzxMiH2NmzQFDVITBCoUZmm5EWKGZX\"}";
//		password=object.toString();
		if (mIMKit == null) {
			return;
		}

		initConnectState();
		
		//创建登录参数
		YWLoginParam loginParam = YWLoginParam.createLoginParam(userId,
				password);
		// openIM SDK提供的登录服务
		IYWLoginService mLoginService = mIMKit.getLoginService();
		
		mLoginService.login(loginParam, callback);
	}
	//设置连接状态的监听
	private void initConnectState(){
		if (mIMKit == null){
			return;
		}
		
		YWIMCore imCore = mIMKit.getIMCore();
		imCore.addConnectionListener(new IYWConnectionListener() {
			
			@Override
			public void onReConnecting() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onReConnected() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDisconnect(int arg0, String arg1) {
				// TODO Auto-generated method stub
				if (arg0 == YWLoginCode.LOGON_FAIL_KICKOFF){
					//在其它终端登录，当前用户被踢下线
					//Toast.makeText(DemoApplication.getContext(), "被踢下线", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	/**
	 * 登录出
	 */
	public void loginOut_Sample(){
		if (mIMKit == null) {
			return;
		}
		// openIM SDK提供的登录服务
		IYWLoginService mLoginService = mIMKit.getLoginService();
		mLoginService.logout(new IWxCallback() {
			
			@Override
			public void onSuccess(Object... arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgress(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
