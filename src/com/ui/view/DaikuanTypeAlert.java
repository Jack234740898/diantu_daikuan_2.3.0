package com.ui.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.constants.ActionString;
import com.constants.ParamsKey;
import com.sxjs.diantu_daikuan.R;
import com.utils.LogUtil;
import com.utils.ScreenUtil;

public class DaikuanTypeAlert {

	private static final String TAG = "DaikuanTypeAlert";
	private AlertDialog mDialog;
	//private AlertDialog.Builder builder;
	private Activity mContext;
	public void showAlert(Activity context,LayoutInflater inflater) {
		mContext = context;
		View view = inflater.inflate(R.layout.daikuan_type_alert, null);
		/*TextView text1 = (TextView) view.findViewById(R.id.text1);
		TextView text2 = (TextView) view.findViewById(R.id.text2);
		text1.setOnClickListener(new MyClickListener(0));
		text2.setOnClickListener(new MyClickListener(1));
		builder = new Builder(context);
		builder.setView(view);
		mDialog = builder.show();*/
		mDialog = new AlertDialog.Builder(context).create();
		mDialog.show();
		Window window = mDialog.getWindow();
		window.setGravity(Gravity.CENTER);
		int width = ScreenUtil.getWidth(context) * 4 / 5;
		window.setLayout(width,
				android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		window.setContentView(view);//
		TextView text1 = (TextView) view.findViewById(R.id.text1);
		TextView text2 = (TextView) view.findViewById(R.id.text2);
		text1.setOnClickListener(new MyClickListener(0));
		text2.setOnClickListener(new MyClickListener(1));
	}

	private class MyClickListener implements OnClickListener{
		int value;
		MyClickListener(int value){
			this.value = value;
		}
		@Override
		public void onClick(View v) {
			send_apply_loan_broad(value);
			mDialog.dismiss();
		}
	}
	
	/*
	 * 申请贷款
	 */
	private void send_apply_loan_broad(int userType){
		LogUtil.d(TAG, "申请贷款==userType is "+userType);
		Intent intent = new Intent(ActionString.click_apply_loan_action);
		intent.putExtra(ParamsKey.userType, userType);
		mContext.sendBroadcast(intent);
	}
}
