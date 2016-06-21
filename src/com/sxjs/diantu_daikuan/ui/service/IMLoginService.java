package com.sxjs.diantu_daikuan.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.alibaba.mobileim.login.YWLoginState;
import com.db.UserData;
import com.openim.NotificationInitSampleHelper;
import com.openim.UserProfileSampleHelper;
import com.sxjs.diantu_daikuan.MyApplication;
import com.utils.LogUtil;

import java.util.Timer;

/*
 * 阿里IM登录后台服务
 */
public class IMLoginService extends Service {

	private static final String TAG = "IMLoginService";
	private MyApplication mContext;

	public IMLoginService() {
		// super("upload_photos");
	}

	public IMLoginService(String name) {
		// super("upload_photos");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = (MyApplication) this.getApplication();

	}

	private Timer mTimer;
	private final int delay_time = 1000 * 1;
	private final int period_time = 1000 * 10;

	private void startTimer() {
		/*
		 * mTimer = new Timer(); TimerTask task = new TimerTask() {
		 * 
		 * @Override public void run() { if(YWLoginState.success
		 * !=mContext.mOpenImUtil.getIMKit().getIMCore().getLoginState()){
		 * initOpenImLogin() ; } } }; mTimer.schedule(task, delay_time,
		 * period_time);
		 */
		new Thread() {
			@Override
			public void run() {
				super.run();
				if (YWLoginState.success != mContext.mOpenImUtil.getIMKit()
						.getIMCore().getLoginState()) {
					initOpenImLogin();
				}
			}
		}.start();
	}

	public void initOpenImLogin() {
		LogUtil.d(TAG, "initOpenImLogin()==openIMUserId is "
				+ UserData.openIMUserId + ",openIMPassword is "
				+ UserData.openIMPassword);
		mContext.mOpenImUtil.login_Sample();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initIMInfo();
		startTimer();
		return super.onStartCommand(intent, flags, startId);
	}

	private void initIMInfo() {
		// 自定义头像昵称
		UserProfileSampleHelper userHelper = new UserProfileSampleHelper(
				mContext);
		userHelper.initProfileCallback();
		// 通知栏相关的初始化
		NotificationInitSampleHelper notificationHelper = new NotificationInitSampleHelper(
				mContext, null);
		notificationHelper.init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// mTimer.cancel();
	}
}
