package com.openim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.IYWP2PPushListener;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.login.IYWConnectionListener;
import com.alibaba.mobileim.login.YWLoginCode;
import com.alibaba.mobileim.login.YWLoginState;
import com.alibaba.mobileim.utility.IMPrefsTools;
import com.constants.GlobalFlag;
import com.constants.ParamsKey;
import com.db.UserData;
import com.sxjs.diantu_daikuan.AppConfig;
import com.sxjs.diantu_daikuan.MyApplication;
import com.utils.LogUtil;
import com.utils.ToastUtil;

//import com.sxjs.dgj_orders.R;

/*
 * 判断当前用户是否登录
 * imkit.getIMCore().getLoginState() 
 */
public class OpenImUtil {

	private static final String TAG = "OpenImUtil";
	private static final String USER_ID = "userId";
	private static final String PASSWORD = "password";
	private MyApplication mContext;

	public ConversationSampleHelper mConversationHelper;

	public OpenImUtil(MyApplication context) {
		this.mContext = context;
	}

	public void initYWSDK(){
		//TODO 注意：--------------------------------------
		//  以下步骤调用顺序有严格要求，请按照示例的步骤（todo step）的顺序调用！
		// ------[todo step1]-------------
		//［IM定制初始化］，如果不需要定制化，可以去掉此方法的调用
		//注意：由于增加全局初始化，该配置需最先执行
		CustomUISampleHelper.initCustomUI();
		// ------[todo step2]-------------
		//SDK初始化
        YWAPI.init(mContext, IMConfig.dtdk_APP_KEY);// 设置Appkey,在申请sdk的时候获取
		// 用于获取云旺的聊天ui相关的API接口。
		mIMKit = YWAPI.getIMKitInstance();
        //通知栏相关的初始化
		//设置是否开启通知提醒
		mIMKit.setEnableNotification(true);
        mConversationHelper = new ConversationSampleHelper(mContext);
        YWAPI.enableSDKLogOutput(AppConfig.IS_TEST);
	}
	
	private YWIMKit mIMKit;

	public YWIMKit getIMKit() {
		return mIMKit;
	}

	public void beforeLoginInit() {

	}

	public Handler mHandler;

	public void setHandler(Handler h) {
		this.mHandler = h;
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
	// ------------------请特别注意，OpenIMSDK会自动对所有输入的用户ID转成小写处理-------------------
	// 所以开发者在注册用户信息时，尽量用小写
	private boolean isLogining=false;
	public void login_Sample() {
		if (mIMKit == null) {
			return;
		}
		isLogining = true;
		addPushListener();// http://baichuan.taobao.com/portal/doc?articleId=544
		addTotalUnreadChangeListener();
		initConnectState();
		// 创建登录参数
		YWLoginParam loginParam = YWLoginParam.createLoginParam(UserData.openIMUserId,UserData.openIMPassword);
		// openIM SDK提供的登录服务
		IYWLoginService mLoginService = mIMKit.getLoginService();
		mLoginService.login(loginParam, new IWxCallback() {
			
			@Override
			public void onSuccess(Object... arg0) {
				LogUtil.d(TAG, "IM初始化登录==onSuccess");
				mContext.mOpenImUtil.saveIdPasswordToLocal(
						mContext, UserData.openIMUserId,
						UserData.openIMPassword);
				isLogining = false;
			}
			
			@Override
			public void onProgress(int arg0) {	
				LogUtil.d(TAG, "IM初始化登录==onProgress==arg0 is " + arg0);
			}
			
			@Override
			public void onError(int errorCode, String errorMessage) {	
				LogUtil.d(TAG, "登录失败 错误码：" + errorCode + "  错误信息："
						+ errorMessage);
				isLogining = false;
			}
		});
	}

	/*
	 * 注册消息未读总数监听
	 */
	private void addTotalUnreadChangeListener() {
		mIMKit.getConversationService().addTotalUnreadChangeListener(
				new IYWConversationUnreadChangeListener() {
					@Override
					// 当前登录账号的未读消息总数发送变化时会回调该方法，用户可以再该方法中更新UI的未读数
					public void onUnreadChange() {
						int totalUnreadCount = mIMKit.getConversationService()
								.getAllUnreadCount();
						if (null != mHandler) {
							if (totalUnreadCount > 0) {
								Message msg = new Message();
								msg.what = ParamsKey.total_unread_count;
								msg.arg1 = totalUnreadCount;
								mHandler.sendMessage(msg);
							}
						}
					}
				});
	}

	private Handler handler2;
	public void setOnePushHandler(Handler handler2) {	
		this.handler2 = handler2;
	}
	/*
	 * 在登录前注册消息push的监听通知，以便登录成功后即能收取到消息的通知
	 * 请尽早添加该监听，从而保证应用不会因为在登录成功后再添加通知而错过一些消息通知
	 */
	private void addPushListener() {
		mIMKit.getConversationService().addP2PPushListener(new IYWP2PPushListener() {
			@Override
			public void onPushMessage(IYWContact arg0, YWMessage arg1) {
				LogUtil.d(TAG, "addPushListener()==111");
				// 单聊消息
				String userId = arg0.getUserId();
				IYWConversationService service = mIMKit
						.getConversationService();
				int unreadCount=0;
				if (null != service) {
					YWConversation conversation = service
							.getConversationByUserId(userId,IMConfig.dgj_APP_KEY);
					LogUtil.d(TAG, "addPushListener()==222==userId is "+userId);
					if (null != conversation) {
						unreadCount = conversation.getUnreadCount();
						LogUtil.d(TAG, "addPushListener()==333==unreadCount is "+unreadCount);
					}
				}
				GlobalFlag.unReadCountMaps.put(userId, unreadCount);
				if(null!=handler2){
					handler2.sendEmptyMessage(111);
				}
			}
		});
	}

	// 设置连接状态的监听
	private void initConnectState() {
		if (mIMKit == null) {
			return;
		}

		YWIMCore imCore = mIMKit.getIMCore();
		imCore.addConnectionListener(new IYWConnectionListener() {
			@Override
			public void onReConnecting() {
			}

			@Override
			public void onReConnected() {
			}

			@Override
			public void onDisconnect(int arg0, String arg1) {
				// TODO Auto-generated method stub
				if (arg0 == YWLoginCode.LOGON_FAIL_KICKOFF) {
					// 在其它终端登录，当前用户被踢下线
					// Toast.makeText(DemoApplication.getContext(), "被踢下线",
					// Toast.LENGTH_LONG).show();
					LogUtil.d(TAG, "在其它终端登录，当前用户被踢下线");
					if(!isLogining){
						login_Sample();
					}
				}
			}
		});
	}

	/**
	 * 登录出
	 */
	public void loginOut_Sample() {
		if (mIMKit == null) {
			return;
		}
		// openIM SDK提供的登录服务
		IYWLoginService mLoginService = mIMKit.getLoginService();
		mLoginService.logout(new IWxCallback() {
			@Override
			public void onSuccess(Object... arg0) {
			}

			@Override
			public void onProgress(int arg0) {
			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});
	}

	/*
	 * 开启客服聊天
	 */
	public void openCustomerChat(Activity activity,String groupId){
		if(YWLoginState.success !=mIMKit.getIMCore().getLoginState()){
			ToastUtil.showToast(mContext, 0, "IM登录失败，请检查网络设置或退出登录再试试", true);
			if(!isLogining){
				login_Sample();
			}
			return;
		}
		EServiceContact contact = new EServiceContact(groupId, 0);//
		Intent intent = mIMKit.getChattingActivityIntent(contact);
		activity.startActivity(intent);
	}

	/*
	 * 开启一个会话界面
	 */
	public void openNewConversation(Activity activity, String userId) {
		LogUtil.d(TAG, "开启一个会话界面== userId is "+userId);
		if (YWLoginState.success != mIMKit.getIMCore().getLoginState()) {
			ToastUtil.showToast(mContext, 0, "IM登录失败，请检查网络设置或退出登录再试试", true);
			if(!isLogining){
				login_Sample();
			}
			return;
		}//
		Intent intent = mIMKit.getChattingActivityIntent(IMConfig.getUseridPre() + userId,IMConfig.dgj_APP_KEY);
		activity.startActivity(intent);
	}

	/*
	 * 打开会话列表
	 */
	public void openConversationList(Activity activity) {
		if (YWLoginState.success != mIMKit.getIMCore().getLoginState()) {
			ToastUtil.showToast(mContext, 0, "IM登录失败，请检查网络设置或退出登录再试试", true);
			if(!isLogining){
				login_Sample();
			}
			return;
		}
		Intent intent = mIMKit.getConversationActivityIntent();
		activity.startActivity(intent);
	}

	/**
	 * 保存OpenIm登录的用户名密码到本地
	 *
	 * @param userId
	 * @param password
	 */
	public void saveIdPasswordToLocal(Context context, String userId,
			String password) {
		IMPrefsTools.setStringPrefs(context, USER_ID, userId);
		IMPrefsTools.setStringPrefs(context, PASSWORD, password);
	}

	/*
	 * 标记为已读 IYWConversationService.markAllReaded()
	 */
	public void markReaded(boolean ismarkAllReaded, String user_id) {
		IYWConversationService conversationService = mConversationHelper.getConversationService();
		if (null == conversationService)
			return;
		if (ismarkAllReaded) {
			conversationService.markAllReaded();
		} else {
			YWConversation conversa = conversationService.getConversationByUserId(user_id, IMConfig.dgj_APP_KEY);
					//conversation.getConversation(user_id);
			if (null == conversa)
				return;
			conversationService.markReaded(conversa);
			GlobalFlag.unReadCountMaps.put(user_id, 0);
		}
	}

}
