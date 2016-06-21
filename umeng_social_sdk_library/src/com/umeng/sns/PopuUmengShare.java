package com.umeng.sns;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.umeng.socialize.UMShareListener;
import com.umeng_social_sdk_res_lib.R;

public class PopuUmengShare implements OnClickListener {

	private static final String TAG = "PopuUmengShare";
	private Activity mActivity;
	private PopupWindow mPopupWindow;
	private UmengSNS mUmengSNS;
	public PopuUmengShare(Activity activity) {
		this.mActivity = activity;
		mUmengSNS = new UmengSNS();
		mUmengSNS.initShare(mActivity);
		mPopupWindow = initPopuwindow();
	}

	private String title;
	private String content;
	private String target_url;
	private String img_url;
	private Bitmap bitmap;
	private UMShareListener umShareListener;
	public void setShareParams(String title, String content, String target_url,String img_url,
			Bitmap bitmap,UMShareListener umShareListener) {
		this.title = title;
		this.content = content;
		this.target_url = target_url;
		this.img_url = img_url;
		this.bitmap = bitmap;
		this.umShareListener = umShareListener;
	}
	
	private PopupWindow initPopuwindow() {
		View view = LayoutInflater.from(mActivity).inflate(
				R.layout.custom_board, null);
		view.findViewById(R.id.wechat).setOnClickListener(this);
		view.findViewById(R.id.wechat_circle).setOnClickListener(this);
		view.findViewById(R.id.qq).setOnClickListener(this);
		view.findViewById(R.id.qzone).setOnClickListener(this);
		view.findViewById(R.id.sinaweibo).setOnClickListener(this);
		view.findViewById(R.id.cancle_text).setOnClickListener(this);
		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		// mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		return mPopupWindow;
	}

	/**
	 * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
	 * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
	 */
	public void showPopu(View root) {
		mPopupWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);

	}

	@Override
	public void onClick(View v) {
		disMiss();
		int id = v.getId();
		if(id == R.id.wechat){
			mUmengSNS.shareToWeixin(false, title, content, target_url, img_url,bitmap,umShareListener);
		}else if(id == R.id.wechat_circle){
			mUmengSNS.shareToWeixin(true, title, content, target_url, img_url,bitmap,umShareListener);
		}else if(id == R.id.qq){
			mUmengSNS.shareToQQ(true, title, content, target_url, img_url,umShareListener);
		}else if(id == R.id.qzone){
			mUmengSNS.shareToQQ(false, title, content, target_url, img_url,umShareListener);
		}else if(id == R.id.sinaweibo){
			mUmengSNS.shareToWeibo(title, content, target_url, img_url,bitmap,umShareListener);
		}
		
	}

	private void disMiss() {
		if (null != mPopupWindow) {
			mPopupWindow.dismiss();
		}
	}
}
