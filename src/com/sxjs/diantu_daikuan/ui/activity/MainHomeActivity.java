package com.sxjs.diantu_daikuan.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.apreparey.main.MainFg;
import com.constants.ActionString;
import com.constants.ActivityRecord;
import com.constants.GlobalFlag;
import com.constants.ParamsKey;
import com.net.service.UserJsonService;
import com.openim.ConversationSampleHelper;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.fragment.ApplyDkFg;
import com.sxjs.diantu_daikuan.ui.fragment.HomeFg;
import com.sxjs.diantu_daikuan.ui.fragment.MyFg;
import com.sxjs.diantu_daikuan.ui.service.IMLoginService;
import com.ui.view.DaikuanTypeAlert;
import com.ui.view.PopuApplyLoan;
import com.ui.view.StatusBarCompat;
import com.umeng.update.UmengUpdateAgent;
import com.utils.LogUtil;
import com.utils.TakePicUtil;
import com.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

/*
 * app主界面
 */
public class MainHomeActivity extends BaseActivity implements OnClickListener,Callback {

	private static final String TAG = "MainHomeActivity";
	// 定义FragmentTabHost对象
	private FragmentTabHost mTabHost;

	// 定义数组来存放Fragment界面
//	private Class fragmentArray[] = { HomeFg.class, ConsultantFg.class,ApplyDkFg.class, MyFg.class };//
	private Class fragmentArray[] = { HomeFg.class , MainFg.class , ApplyDkFg.class , MyFg.class };

	// 定义数组来存放按钮图片 
    private int mImageViewArray[] ={R.drawable.tab1_img,R.drawable.tab2_img,R.drawable.tab3_img,R.drawable.tab4_img};
	//private int mImageViewArray[] ={R.drawable.tab1_d_img,R.drawable.tab2_d_img,R.drawable.tab3_d_img,R.drawable.tab4_d_img};
	// Tab选项卡的文字
	private String mTextviewArray[] = { "电兔首页", "顾问咨询", "我要贷款","个人中心" };

	private String tags;
	private int tabIndex;
	private Handler mHandler;
	private ConversationSampleHelper mConversationHelper; 
	public TakePicUtil mTakePicUtil;
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		StatusBarCompat.setStatusBarColor(mActivity,mActivity.getResources().getColor(R.color.black));
		mConversationHelper = mContext.mOpenImUtil.mConversationHelper;
		mHandler = new Handler(this);
		mContext.mOpenImUtil.setHandler(mHandler);
		mTakePicUtil = new TakePicUtil(mActivity);
		registerCeiver();
		//init_option_list();
		checkUpdate();
		initView();
	}

	private void init_option_list() {
		new Thread(){
			public void run() {
				UserJsonService userJsonService = new UserJsonService(mActivity);
				userJsonService.setNeedCach(false);
				userJsonService.option_list(1);
			}
		}.start();
	}

	private LinearLayout root;
	private void initView() {
		root = (LinearLayout) findViewById(R.id.root);
		// 实例化TabHost对象，得到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		// 得到fragment的个数
		int count = fragmentArray.length;

		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			// 设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundColor(getResources().getColor(R.color.white));
		}
		mTabHost.setCurrentTab(tabIndex);
	}

	/**
	 * 给Tab按钮设置图标和文字
	 */
	//private ImageView tab3_img;
	private TextView mmy_remind_text;
	private View getTabItemView(int index) {
		View view = mInflater.inflate(R.layout.tab_item_view, null);
		View view_rl = view.findViewById(R.id.view_rl);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		TextView my_remind_text = (TextView) view.findViewById(R.id.my_remind_text);
		my_remind_text.setVisibility(View.VISIBLE);
		//imageView.setTag(index);
		view_rl.setTag(index);
		view_rl.setOnClickListener(this);
		if (0 == index) {
			imageView.setSelected(true);
		} else {
			imageView.setSelected(false);
		}
		imageView.setImageResource(mImageViewArray[index]);
		textView.setText(mTextviewArray[index]);
		my_remind_text.setVisibility(View.GONE);
		return view;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 用户点击了返回键
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (canExit) {
				return super.onKeyDown(keyCode, event);
			}
			oneAgainExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean canExit = false;

	private void oneAgainExit() {
		// (mContext.getActivityManager()).popActivity(this);
		canExit = true;
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				canExit = false;
			}
		}, 2500);
		if (canExit)
			ToastUtil.showToast(this, R.string.one_again_exit, false);
	}

	@Override
	public void onClick(View v) {
		Object o = v.getTag();
		if (null == o || !(o instanceof Integer))
			return;
		tabIndex = (Integer) o;
		mTabHost.setCurrentTab(tabIndex);
		if (1 == tabIndex) {
			if (null != mmy_remind_text) {
				mmy_remind_text.setVisibility(View.GONE);
			}
		}else if(2 == tabIndex){
			//popu_choose_daikuan_type();
		}
	}
	
	/*
	 * 选择贷款类型
	 */
	private static final int daikuan_type = 3;
	private PopuApplyLoan mPopuApplyLoan;
	private void popu_choose_daikuan_type() {
		if(GlobalFlag.need_choose){
			new DaikuanTypeAlert().showAlert(mActivity, mInflater);
		}
		
		/*if(null==mPopuApplyLoan)
			mPopuApplyLoan = new PopuApplyLoan(mActivity);
		JSONArray array = new JSONArray();
		String[] label = {"个人消费贷","企业经营贷"};
		int[] value = {0,1};
		for(int i=0;i<2;i++){
			JSONObject obj = new JSONObject();
			try {
				obj.put("label", label[i]);
				obj.put("value", value[i]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		mPopuApplyLoan.setData(array, daikuan_type);
		mPopuApplyLoan.setResultListener(this);
		mPopuApplyLoan.showPopupWindow(root);*/
	}

	private void checkUpdate() {
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(mActivity);
	}
	
	private MyReceiver mMyReceiver;
	private void registerCeiver() {
		mMyReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		//filter.addAction(ActionString.click_more_consultant_action);
		filter.addAction(ActionString.click_apply_loan_action);
		filter.addAction(ActionString.click_exit_login_action);
		mActivity.registerReceiver(mMyReceiver, filter);
	}

	private void unRegisterReceiver(){
		if(null!=mMyReceiver){
			mActivity.unregisterReceiver(mMyReceiver);
			mMyReceiver = null;
		}
	}
	private class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {			
			if(ActionString.click_more_consultant_action.equals(intent.getAction())){
				Message msg = new Message();
				msg.what =222;
				mHandler.sendMessage(msg);
			}else if(ActionString.click_apply_loan_action.equals(intent.getAction())){
				Message msg = new Message();
				//int specialStatus = intent.getIntExtra(ParamsKey.specialStatus, -1);
				//msg.arg1 = specialStatus;
				msg.what =333;
				mHandler.sendMessage(msg);
			}else if(ActionString.click_exit_login_action.equals(intent.getAction())){
				Message msg = new Message();
				msg.what =444;
				mHandler.sendMessage(msg);
			}
		}
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		if(msg.what==ParamsKey.total_unread_count){
			if(null!=mmy_remind_text){
				int totalUnreadCount = msg.arg1;
				if(totalUnreadCount>0){
					mmy_remind_text.setVisibility(View.VISIBLE);
					String count = ""+totalUnreadCount;
					if(totalUnreadCount>9){
						count="9+";
					}
					mmy_remind_text.setText(String.valueOf(totalUnreadCount));
				}
			}
		}else if(222==msg.what){
			//更多顾问
			mTabHost.setCurrentTab(tabIndex=1);
		}else if(333==msg.what){
			//我要贷款
			mTabHost.setCurrentTab(tabIndex=2);
			//send_apply_loan_broad(msg.arg1);
		}else if(444==msg.what){
			mTabHost.setCurrentTab(tabIndex=0);
		}
		return false;
	}
	
	/*
	 * 申请贷款
	 */
	private void send_apply_loan_broad(int userType){
		LogUtil.d(TAG, "申请贷款==userType is "+userType);
		Intent intent = new Intent(ActionString.click_apply_loan_action);
		intent.putExtra(ParamsKey.userType, userType);
		mActivity.sendBroadcast(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegisterReceiver();
		mActivity.stopService(new Intent(mActivity, IMLoginService.class));
		ActivityRecord.activityFinish(null);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.d(TAG, "onActivityResult()==requestCode is "+requestCode+",resultCode is "+resultCode);
		switch (requestCode) {
		// 如果是直接从相册获取
		case TakePicUtil.CHOOSE_PIC:
			String pathFromPic = mTakePicUtil.getBitmapFromPic2(data);// getBitmapFromPic(mActivity,												// //
			LogUtil.d(TAG, "从相册获取==pathFromPic is " + pathFromPic);
			send_Modify_head_broad(pathFromPic);
			break;
		// 如果是调用相机拍照时
		case TakePicUtil.TAKE_PIC://TakePicUtil.localimgPath
			String pathFromTakePic = mTakePicUtil.getBitmapPath2(data);// getBitmapPath(data);
			LogUtil.d(TAG, "从拍照获取==pathFromTakePic is " + pathFromTakePic);
			send_Modify_head_broad(pathFromTakePic);
			break;
		// 取得裁剪后的图片
		case 3:
			if (data != null) {
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(GlobalFlag.have_no_apply_loan){
			mTabHost.setCurrentTab(tabIndex=2);
			GlobalFlag.have_no_apply_loan = false;
		}
		
		if(GlobalFlag.dk_sucess_back_home){
			mTabHost.setCurrentTab(tabIndex=0);
			GlobalFlag.dk_sucess_back_home = false;
		}
	}
	private void send_Modify_head_broad(String pic_path){
		Intent intent = new Intent(ActionString.send_Modify_head_broad_action);
		intent.putExtra(ParamsKey.user_head, pic_path);
		mActivity.sendBroadcast(intent);
	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.main_home;
	}
}
