package com.ui.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.utils.LogUtil;

public class HeadView {

	private static final String TAG = "HeadView";
	private View view;
	public HeadView(View view) {
		this.view = view;
	}

	/*
	 * 设置head_view的北京色值
	 */
	public void setHeadviewColor(int color) {
		View head_view = view.findViewById(R.id.head_view);
		if (null != head_view)
			head_view.setBackgroundColor(color);
	}

	/*
	 * 左边button的隐藏
	 */
	public void hideLeftBtn() {
		View leftImg = view.findViewById(R.id.leftImg);
		if (null != leftImg)
			leftImg.setVisibility(View.INVISIBLE);
	}

	/**
	 * 右边button的隐藏
	 */
	public void hideRightBtn() {
		View rightImg = view.findViewById(R.id.rightImg);
		if (null != rightImg)
			rightImg.setVisibility(View.INVISIBLE);
	}

	/*
	 * 设置中间textView上的文字
	 */
	public void setCentreTextView(int resID) {
		TextView textview = (TextView) view.findViewById(R.id.textview);
		if (null != textview)
			textview.setText(resID);
	}

	/*
	 * 设置中间textView上的文字
	 */
	public void setCentreTextView(String s) {
		TextView textview = (TextView) view.findViewById(R.id.textview);
		if (null != textview) {
			textview.setText(s);
		}
	}

	/**
	 * 设置左边button的背景
	 */
	public void setLeftBtnBg(int resID, OnClickListener clickListener) {
		ImageView leftImg = (ImageView) view.findViewById(R.id.leftImg);
		if (null != leftImg) {
			leftImg.setImageResource(resID);
			leftImg.setOnClickListener(clickListener);
		}
	}

	/*
	 * 设置左边button的背景
	 */
	public void setRightBtnBg(int resID, OnClickListener clickListener) {
		ImageView rightImg = (ImageView) view.findViewById(R.id.rightImg);
		if (null != rightImg) {
			rightImg.setImageResource(resID);
			rightImg.setOnClickListener(clickListener);
		}
	}

	/*
	 * 设置右边TextView上的文字
	 */
	public void setRightTextView(int resID, OnClickListener clickListener) {
		TextView rightText = (TextView) view.findViewById(R.id.rightText);
		if (null != rightText) {
			rightText.setVisibility(View.VISIBLE);
			rightText.setText(resID);
			rightText.setOnClickListener(clickListener);
		}
	}

	/*
	 * 设置右边TextView上的文字
	 */
	public void setRightTextView(String content, OnClickListener clickListener) {
		TextView rightText = (TextView) view.findViewById(R.id.rightText);
		if (null != rightText) {
			rightText.setVisibility(View.VISIBLE);
			rightText.setText(content);
			rightText.setOnClickListener(clickListener);
		}
	}

	public void hideRightTextView() {
		TextView rightText = (TextView) view.findViewById(R.id.rightText);
		if (null != rightText) {
			rightText.setVisibility(View.INVISIBLE);
		}
	}

	public void setStatusBarView(Activity activity) {
		int sdkVer = Build.VERSION.SDK_INT;
		LogUtil.d(TAG, "setStatusBarView==sdkVer is "+sdkVer);
		Window window = activity.getWindow();
		// 透明状态栏
		if (sdkVer < Build.VERSION_CODES.KITKAT) {
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		} else {
			// 透明状态栏
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}

	}

	public void initSystemBar(Activity activity) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			setTranslucentStatus(activity, true);

		}

		SystemBarTintManager tintManager = new SystemBarTintManager(activity);
		tintManager.setStatusBarTintEnabled(true);

		// 使用颜色资源

		tintManager.setStatusBarTintResource(R.color.orange_red_color);

	}

	@TargetApi(19)
	private void setTranslucentStatus(Activity activity, boolean on) {
		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();

		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

}
