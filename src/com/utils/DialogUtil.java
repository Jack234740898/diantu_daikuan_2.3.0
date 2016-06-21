package com.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;

public class DialogUtil {
	/*
	 * 显示确认取消弹框
	 */
	public static AlertDialog showConfirmCancleDialog(Activity context,
			String title, String confirmStr,OnClickListener confirmListener) {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		int width = ScreenUtil.getWidth(context) * 2 / 3;
		window.setLayout(width,
				android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		View view = context.getLayoutInflater().inflate(
				R.layout.confirm_cancle_dialog, null);
		if(StringUtil.checkStr(confirmStr)){
			TextView confirm = (TextView) view.findViewById(R.id.confirm);
			confirm.setText(confirmStr);
		}
		window.setContentView(view);//

		((TextView) view.findViewById(R.id.title)).setText(title);
		view.findViewById(R.id.confirm).setOnClickListener(confirmListener);
		view.findViewById(R.id.cancle).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		return dialog;
	}

	/*
	 * 显示确认取消弹框
	 */
	public static AlertDialog showConfirmCancleDialog2(Activity context,
			String title,String desc, String confirmStr,OnClickListener confirmListener) {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		int width = ScreenUtil.getWidth(context) * 2 / 3;
		window.setLayout(width,
				android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		View view = context.getLayoutInflater().inflate(
				R.layout.confirm_cancle_dialog2, null);
		if(StringUtil.checkStr(confirmStr)){
			TextView confirm = (TextView) view.findViewById(R.id.confirm);
			confirm.setText(confirmStr);
		}
		window.setContentView(view);//

		TextView title_text = ((TextView) view.findViewById(R.id.title));
		title_text.setVisibility(View.GONE);
		if(StringUtil.checkStr(title)){
			title_text.setVisibility(View.VISIBLE);
			title_text.setText(title);
		}
		title_text.setText(title);
		TextView desc_text = ((TextView) view.findViewById(R.id.desc));
		desc_text.setVisibility(View.GONE);
		if(StringUtil.checkStr(desc)){
			desc_text.setVisibility(View.VISIBLE);
			desc_text.setText(desc);
		}
		
		view.findViewById(R.id.confirm).setOnClickListener(confirmListener);
		view.findViewById(R.id.cancle).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		return dialog;
	}
	
	private static ProgressDialog pd;

	public static void showProgressDialog(Activity context, String title) {
		pd = ProgressDialog.show(context, title, "");
	}

	public static void closeProgressDialog(Activity activity) {
		if (activity.isFinishing()) {
			if (null != pd && pd.isShowing()) {
				pd.dismiss();
				pd = null;
			}
		}

	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		//loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}
	
	/*
	 * 显示提示弹框
	 */
	public static AlertDialog showAlertDialog(Activity context,
			String title, String content) {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		int width = ScreenUtil.getWidth(context) * 4 / 5;
		window.setLayout(width,
				android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		View view = context.getLayoutInflater().inflate(
				R.layout.alert_dialog, null);
		if(StringUtil.checkStr(title)){
			TextView title_text = (TextView) view.findViewById(R.id.title_text);
			title_text.setText(title);
		}
		if(StringUtil.checkStr(content)){
			TextView content_text = (TextView) view.findViewById(R.id.content_text);
			content_text.setText(content);
		}
		window.setContentView(view);
		view.findViewById(R.id.confirm_text).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		return dialog;
	}
	
	/*
	 * 显示提示弹框,了解原因
	 */
	public static AlertDialog showCauseAlertDialog(Activity context) {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		int width = ScreenUtil.getWidth(context) * 4 / 5;
		window.setLayout(width,
				android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		View view = context.getLayoutInflater().inflate(
				R.layout.alert_dialog, null);
		TextView title_text = (TextView) view.findViewById(R.id.title_text);
		title_text.setText("没人抢单原因");
		TextView content_text = (TextView) view.findViewById(R.id.content_text);
		String content = "*您所在的城市当前可能没有信贷经理在线\n*您的个人资质(比如社保缴纳，打卡工资，年龄等)可能不符合要求";
		content_text.setText(content);
		window.setContentView(view);
		TextView confirm_text = (TextView) view.findViewById(R.id.confirm_text);
		confirm_text.setText("好的");
		confirm_text.setTextColor(context.getResources().getColor(R.color.orange_red_text));
		confirm_text.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		return dialog;
	}
}
