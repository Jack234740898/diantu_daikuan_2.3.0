package com.sxjs.diantu_daikuan;

import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;

import com.alibaba.wxlib.util.SysUtil;
import com.constants.GlobalFlag;
import com.db.DeviceData;
import com.db.UserData;
import com.db.service.DeviceDataService;
import com.db.service.GPSDataService;
import com.db.service.UserDataService;
import com.img.download.ImageLoad;
import com.net.OkHttpClientManager;
import com.net.service.UserJsonService;
import com.openim.OpenImUtil;
import com.push.MyPushIntentService;
import com.umeng.push.utils.PushConfigUtil;
import com.umeng.sns.UmengSNS;
import com.utils.DeviceAppInfoUtil;
import com.utils.PackageUtil;
import com.utils.StringUtil;

import org.json.JSONObject;

import java.util.ArrayList;

public class MyApplication extends MultiDexApplication {

    public static final String TAG = "MyApplication";
    public static MyApplication mContext;
    public Thread mUiThread;
    public ActivityManager activityManager;
    public ImageLoad mImgLoad;

    public OkHttpClientManager mOkHttpClientManager;
    public OpenImUtil mOpenImUtil;
    // 销毁前台程序的msgid
    public final int MSGID_KILLACTIVITYS = 1;
    public final Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSGID_KILLACTIVITYS:
                    activityManager.popAllActivity();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        AppConfig.APP_VERSION = PackageUtil.getVersionName(mContext);
        mImgLoad = new ImageLoad();
        mOkHttpClientManager = new OkHttpClientManager(mContext);
        initDeviceAppInfo();
        initUserData();
        // device_register();
        activityManager = ActivityManager.getActivityManagerInstance();
        mUiThread = Thread.currentThread();

        SysUtil.setApplication(this);
        if (SysUtil.isTCMSServiceProcess(mContext))
            return;

        mOpenImUtil = new OpenImUtil(mContext);
        mOpenImUtil.initYWSDK();
        UmengSNS.init();
        umeng_push_init();
        baiduInit();
    }

    private void umeng_push_init() {
        PushConfigUtil.init(mContext, MyPushIntentService.class, false);
    }

    /*
     * 初始化设备应用信息
     */
    private void initDeviceAppInfo() {
        new Thread() {
            public void run() {
                AppConfig.channel_name = DeviceAppInfoUtil
                        .getChannelName(mContext);
                AppConfig.net_way = DeviceAppInfoUtil.getNet_way(mContext);
                AppConfig.device_model = DeviceAppInfoUtil
                        .getDeviceModel(mContext);
                AppConfig.system_version = DeviceAppInfoUtil
                        .getSysVersion(mContext);
                // AppConfig.app_version_name =
                // DeviceAppInfoUtil.getAppVersionName(mContext);
                AppConfig.IMSI = DeviceAppInfoUtil
                        .getPhoneOperatorName(mContext);
            }
        }.start();
    }

    private void baiduInit() {
        GPSDataService mGPSDataService = new GPSDataService(mContext);
        String local = mGPSDataService.getGps_local();
        if (StringUtil.checkStr(local))
            UserData.local_city = local;
    }

    private void device_register() {
        if (StringUtil.checkStr(UserData.userId)) {
            return;
        }
        new Thread() {
            public void run() {
                UserJsonService userService = new UserJsonService(mContext);
                userService.device_register();
            }
        }.start();
    }

    private void initUserData() {
        UserData userdata = new UserData(mContext);
        userdata.setUserData();
        UserDataService userService = new UserDataService(mContext);
        UserData.userId = userService.getUserId();
        UserData.usernick = userService.getUserNick();
        UserData.userPhone = userService.getUserPhone();
        UserData.userRealName = userService.getRealName();
        UserData.user_birthday = userService.getBirthday();
        UserData.haspwd = userService.hasPwd();
        String device_token = new DeviceDataService(mContext).getDevice_token();
        DeviceData.device_token = device_token;
        String login_token = userService.getUserToken();
        GlobalFlag.global_token = "";
        if (StringUtil.checkStr(device_token)) {
            GlobalFlag.global_token = device_token;
        }
        if (StringUtil.checkStr(UserData.userId)) {
            UserData.openIMUserId = userService.getOpenIMUserId();
            UserData.openIMPassword = userService.getopenIMPassword();
            GlobalFlag.global_token = login_token;
        }
    }

    /**
     * @return the activityManager
     */
    public ActivityManager getActivityManager() {
        return activityManager;
    }

    /**
     * @param activityManager the activityManager to set
     */
    public void setActivityManager(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    public final void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(action);
        } else {
            action.run();
        }
    }

    public static Context getContext() {
        return mContext;
    }

    private ArrayList<JSONObject> pictureWallList;

    public void setPictureWallList(ArrayList<JSONObject> pictureWallList) {
        this.pictureWallList = pictureWallList;
    }

    public ArrayList<JSONObject> getPictureWallList() {
        return pictureWallList;
    }
}
