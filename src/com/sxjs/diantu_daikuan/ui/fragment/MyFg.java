package com.sxjs.diantu_daikuan.ui.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.apreparey.MulitLoanActivity;
import com.constants.ActionString;
import com.constants.ParamsKey;
import com.constants.UmenClickEvevt;
import com.db.UserData;
import com.db.service.UserDataService;
import com.net.service.UserJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.activity.AboutUsActivity;
import com.sxjs.diantu_daikuan.ui.activity.FeedBackActivity;
import com.sxjs.diantu_daikuan.ui.activity.LoginActivity;
import com.sxjs.diantu_daikuan.ui.activity.MainHomeActivity;
import com.sxjs.diantu_daikuan.ui.activity.MyDaikuanActivity;
import com.sxjs.diantu_daikuan.ui.activity.MyInfoActivity;
import com.sxjs.diantu_daikuan.ui.service.IMLoginService;
import com.ui.view.CircularImage;
import com.ui.view.MyAsyncTask;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.utils.AnimationUtil;
import com.utils.DialogUtil;
import com.utils.IntentUtil;
import com.utils.LogUtil;
import com.utils.StringUtil;
import com.utils.TakePicUtil;
import com.utils.ToastUtil;
import com.utils.UmenStatisticsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyFg extends BaseFragment implements OnClickListener,Callback{

	private static final String TAG = "MyFg";
	private UserJsonService mUserJsonService;
	private Handler mHandler;
	@Override
	protected void initView() {
		mHandler = new Handler(this);
		mUserJsonService = new UserJsonService(mActivity);
		registerReceiver();
		initV();
		loadData();
	}

	@Override
	protected int setContentView() {
		return R.layout.my_fg;
	}
	
	private CircularImage head_img;
	private TextView name_text;
	private View exit_login_text,root;
	private void initV(){
		root = findViewById(R.id.root);
		findViewById(R.id.feed_back_ll).setOnClickListener(this);
		exit_login_text = findViewById(R.id.exit_login_text);
		exit_login_text.setOnClickListener(this);
		findViewById(R.id.person_info_ll).setOnClickListener(this);
		findViewById(R.id.update_ll).setOnClickListener(this);
		findViewById(R.id.about_us_ll).setOnClickListener(this);
		findViewById(R.id.my_daikuan_ll).setOnClickListener(this);
		head_img = (CircularImage) findViewById(R.id.head_img);
		head_img.setOnClickListener(this);
		name_text = (TextView) findViewById(R.id.name_text);
		findViewById(R.id.top_ll).setOnClickListener(this);
		findViewById(R.id.go_to_score_ll).setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		AnimationUtil.fadeAnimat(mActivity, root, true);
		registerReceiver();
		show_login_status();
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.exit_login_text:
			//退出登录
			exit_login();
			break;
		case R.id.update_ll:
			//手动更新
//			checkUpdate();  
			IntentUtil.activityForward(mActivity, MulitLoanActivity.class, null, false);
			break;
		case R.id.head_img:
			if(StringUtil.checkStr(UserData.userId)){
				choose_photos();
			}else{
				IntentUtil.activityForward(mActivity, LoginActivity.class, null, false);
			}
			break;
		case R.id.top_ll:
			if(!StringUtil.checkStr(UserData.userId)){
				IntentUtil.activityForward(mActivity, LoginActivity.class, null, false);
			}
			break;
		case R.id.about_us_ll:
			IntentUtil.activityForward(mActivity, AboutUsActivity.class, null, false);
			break;
		case R.id.person_info_ll:
			if(StringUtil.checkStr(UserData.userId)){
				IntentUtil.activityForward(mActivity, MyInfoActivity.class, null, false);
			}else{
				IntentUtil.activityForward(mActivity, LoginActivity.class, null, false);
			}
			break;
		case R.id.my_daikuan_ll:
			if(StringUtil.checkStr(UserData.userId)){
				IntentUtil.activityForward(mActivity, MyDaikuanActivity.class, null, false);
			}else{
				Bundle bundle = new Bundle();
				bundle.putBoolean("my_daikuan", true);
				IntentUtil.activityForward(mActivity, LoginActivity.class, bundle, false);
			}
			break;
		case R.id.feed_back_ll:
			IntentUtil.activityForward(mActivity, FeedBackActivity.class, null, false);
			break;
		case R.id.go_to_score_ll:
			go_to_score();
			break;
		default:
			break;
		}
	}
	
	private TakePicUtil mTakePic;
	private void choose_photos() {
		if (null == mTakePic)
			mTakePic = ((MainHomeActivity)mActivity).mTakePicUtil;
		mTakePic.ShowPickDialog();
	}
	
	private void showPicImg(String imgPath) {
		LogUtil.d(TAG, "showPicImg()==imgPath is " + imgPath);
		// 上传图片
		if (!StringUtil.checkStr(imgPath))
			return;
		new AsyUploadPic(mActivity,"",imgPath).execute();
	}

	private class AsyUploadPic extends MyAsyncTask{
		String pic;
		protected AsyUploadPic(Context context,String title,String pic) {
			super(context,title);
			this.pic = pic;
		}

		@Override
		protected Object doInBackground(Object... params) {
			mUserJsonService.setNeedCach(false);
			JSONObject dataResult = mUserJsonService.upload_pictures(pic);
			if(null==dataResult)
				return null;
			JSONObject data = dataResult.optJSONObject("data");
			String pictureURL = data.optString("pictureURL");
			JSONArray pictures = data.optJSONArray("pictures");
			if(null!=pictures&&pictures.length()>0){
				UserData.user_head = pictureURL+pictures.optString(0);
				LogUtil.d(TAG, "UserData.user_head is "+UserData.user_head);
				return mUserJsonService.user2_updateAvatar(pictures.optString(0));
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			mImgLoad.loadImg(head_img, UserData.user_head , R.drawable.default_head);
			String msg = "头像修改失败";
			if(null!=result&&(Boolean)result){
				msg = "头像修改成功";
				UmenStatisticsUtil.onEvent(mActivity, UmenClickEvevt.modify_head);
			}
			ToastUtil.showToast(mActivity, 0, msg, true);
		}
	} 
	
	private void checkUpdate() {
		UmengUpdateAgent.forceUpdate(mActivity);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		    @Override
		    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
		        switch (updateStatus) {
		        case UpdateStatus.Yes: // has update
		            UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
		            break;
		        case UpdateStatus.No: // has no update
		            Toast.makeText(mContext, "当前已是最新版本", Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.NoneWifi: // none wifi
		            Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.Timeout: // time out
		            Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
		            break;
		        }
		    }
		});
	}
	
	AlertDialog dialog;
	private void exit_login() {
		dialog = DialogUtil.showConfirmCancleDialog(mActivity, "您确定要退出登录吗？", "确定", new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				UserDataService userService = new UserDataService(mActivity);
				userService.clearData();
				UserData ud = new UserData(mActivity);
				ud.logout();
				dialog.dismiss();
				//UmengPushAlias.removeAlias(mContext);
				mActivity.stopService(new Intent(mActivity, IMLoginService.class));
				//mContext.mOpenImUtil.loginOut_Sample();
				show_login_status();
				//IntentUtil.activityForward(mActivity, LoginRegActivity.class, null, true);
				Intent intent = new Intent(ActionString.click_exit_login_action);
				mActivity.sendBroadcast(intent);
			}
		});
	}
	
	@Override
	protected void loadData() {
		super.loadData();
		new AsyLoadData().execute();
	}
	
	private String description;
	private int auth;
	private class AsyLoadData extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Void... arg0) {
			mUserJsonService.setNeedCach(false);
			return mUserJsonService.user2_get();
		}
		
		@Override
		protected void onPostExecute(JSONObject obj) {
			super.onPostExecute(obj);
			//mUserJsonService.saveLoginInfo(obj);
			if(null==obj)
				return;
			JSONObject user = obj.optJSONObject("user");
			if(null==user)
				return;
			String id = user.optString("id");
			String userName = user.optString("userName");
			String nickName = user.optString("nickName");
			String avatar = user.optString("avatar");
			String city = user.optString("city");
			String companyName = user.optString("companyName");
			description = user.optString("description");
			UserData.userId = id;
			UserData.userRealName = userName;
			UserData.user_head = avatar;
			
			show_login_status();
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unRegisterReceiver();
	}
	/*
	 * 显示登录与非登录
	 */
	private void show_login_status(){
		LogUtil.d(TAG, "userId is "+UserData.userId+",user_head is "+UserData.user_head);
		if(StringUtil.checkStr(UserData.userId)){
			mImgLoad.loadImg(head_img, UserData.user_head, R.drawable.default_head);
			name_text.setText("");
			if(StringUtil.checkStr(UserData.userRealName)){
				name_text.setText(UserData.userRealName);
			}
			exit_login_text.setVisibility(View.VISIBLE);
		}else{
			LogUtil.d(TAG, "退出登录后==");
			name_text.setText("登录/注册");
			mImgLoad.loadImg(head_img, null, R.drawable.default_head);
			//head_img.setImageDrawable(new BitmapDrawable());
			exit_login_text.setVisibility(View.INVISIBLE);
		}
	}
	
	private MyReceiver mMyReceiver;
	private void registerReceiver() {
		mMyReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ActionString.send_Modify_head_broad_action);
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
			if(ActionString.send_Modify_head_broad_action.equals(intent.getAction())){
				String pic_path = intent.getStringExtra(ParamsKey.user_head);
				Message msg = new Message();
				msg.what =11211;
				msg.obj = pic_path;
				mHandler.sendMessage(msg);
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if(11211==msg.what){
			String user_head = (String) msg.obj;
			showPicImg(user_head);
		}
		return false;
	}
	
	private void go_to_score() {
		UmenStatisticsUtil.onEvent(mActivity, UmenClickEvevt.go_to_score);
		Uri uri = Uri.parse("market://details?id="+mActivity.getPackageName());  
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		mActivity.startActivity(intent);  
	}
}
