package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationCreater;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.constants.GlobalFlag;
import com.net.service.UserJsonService;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.MyAsyncTask;
import com.umeng.sns.PopuUmengShare;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.utils.LogUtil;
import com.utils.StringUtil;

import org.json.JSONObject;

import java.io.File;

/*
 * 贷款申请成功页面
 */
public class ApplySucessActivity extends BaseActivity implements
		OnClickListener {

	private static final String TAG = "ApplySucessActivity";
	private UserJsonService mUserJsonService;

	//private int 
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mUserJsonService = new UserJsonService(mActivity);
		initView();
		loadData();
	}

	@Override
	protected void loadData() {
		super.loadData();
		new AsyGetShareInfo(mActivity,"").execute();
	}

	private View share_root, apply_success_view;
	private TextView text1, text2, share_text;

	private void initView() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("申请贷款");
		mHeadView.hideRightBtn();
		findViewById(R.id.back_home_text1).setOnClickListener(this);
		findViewById(R.id.back_home_text2).setOnClickListener(this);
		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		share_root = findViewById(R.id.share_root);
		apply_success_view = findViewById(R.id.apply_success_view);
		share_text = (TextView) findViewById(R.id.share_text);
		share_text.setOnClickListener(this);
		showView(false);
	}

	private void showView(boolean isShareSuccess) {
		if (isShareSuccess) {
			share_root.setVisibility(View.GONE);
			apply_success_view.setVisibility(View.VISIBLE);
		} else {
			share_root.setVisibility(View.VISIBLE);
			apply_success_view.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.back_home_text1:
			//ActivityRecord.activityFinish(null);
			// IntentUtil.activityForward(mActivity, MainHomeActivity.class,
			// null, false);
			GlobalFlag.dk_sucess_back_home = true;
			finish();
			break;
		case R.id.back_home_text2:
			//ActivityRecord.activityFinish(null);
			// IntentUtil.activityForward(mActivity, MainHomeActivity.class,
			// null, false);
			GlobalFlag.dk_sucess_back_home = true;
			finish();
			break;
		case R.id.share_text:
			share();
			break;
		default:
			break;
		}
	}

	private String title, content, img, link;

	private class AsyGetShareInfo extends MyAsyncTask {

		protected AsyGetShareInfo(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			mUserJsonService.setNeedCach(false);
			return mUserJsonService.dk_user_share();
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (null == result)
				return;
			JSONObject shareInfo = (JSONObject) result;
			title = shareInfo.optString("title");
			content = shareInfo.optString("content");
			img = shareInfo.optString("img");
			link = shareInfo.optString("link");
		}
	}

	/*
	 * 分享
	 */
	private PopuUmengShare mPopuUmengShare;
	private void share() {
		LogUtil.d(TAG, "title is "+title+",content is "+content+",link is "+link+",img is "+img);
		if (null == mPopuUmengShare)
			mPopuUmengShare = new PopuUmengShare(mActivity);
		Bitmap bitmap = getShareBitmap(img);
		LogUtil.d(TAG, "share()==bitmap is " + bitmap);
		mPopuUmengShare.setShareParams(title, content, link, img, bitmap,umShareListener);
		mPopuUmengShare.showPopu(share_root);
	}

	private Bitmap getShareBitmap(String img_url) {
		String local_img_url = mImgLoad.getLocalImgPath(img_url);
		if (StringUtil.checkStr(local_img_url)) {
			if (new File(local_img_url).exists()) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(local_img_url, options);
				options.inSampleSize = 2;
				options.inJustDecodeBounds = false;
				return BitmapFactory.decodeFile(local_img_url, options);
			}
		}
		return null;
	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Toast.makeText(mActivity, "分享成功啦", Toast.LENGTH_SHORT).show();
			showView(true);
			sendMSG();
			LogUtil.d(TAG, "分享成功啦");
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(mActivity, "分享失败啦", Toast.LENGTH_SHORT).show();
			LogUtil.d(TAG, "分享失败啦");
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(mActivity, "分享取消了", Toast.LENGTH_SHORT).show();
			LogUtil.d(TAG, "分享取消啦");
		}
	};

	private void sendMSG(){
		final YWMessage message = YWMessageChannel.createTextMessage("我已分享了电兔贷款app哟");
		YWIMKit imKit = mContext.mOpenImUtil.getIMKit();
		if(null==imKit)
			return;
		EServiceContact contact = new EServiceContact("dtgw05", 0);//
		IYWConversationService service = imKit.getConversationService();
		if(null==service)
			return;
		YWConversationCreater creater = service.getConversationCreater();
		if(null==creater)
			return;
		YWConversation conversation = creater.createConversation(contact);
		if(null==conversation)
			return;
		//将消息发送给对方
		conversation.getMessageSender().sendMessage(message, 10000, new com.alibaba.mobileim.channel.event.IWxCallback(){

			@Override
			public void onError(int arg0, String arg1) {	
				LogUtil.d(TAG, "顾问消息发送==onError==arg0 is "+arg0+",arg1 is "+arg1);
			}

			@Override
			public void onProgress(int arg0) {	
				LogUtil.d(TAG, "顾问消息发送==onProgress"+arg0);
			}

			@Override
			public void onSuccess(Object... arg0) {		
				LogUtil.d(TAG, "顾问消息发送==onSuccess"+arg0);
			}
		});
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode,
				data);
		/** 使用SSO授权必须添加如下代码 */
		/*
		 * if (null == mPopuUmengShare) return; UMSsoHandler ssoHandler =
		 * mPopuUmengShare.mController.getConfig() .getSsoHandler(requestCode);
		 * if (ssoHandler != null) { ssoHandler.authorizeCallBack(requestCode,
		 * resultCode, data); }
		 */
	}

	@Override
	protected int setContentView() {
		return R.layout.apply_sucess;
	}
}
