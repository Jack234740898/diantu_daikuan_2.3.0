package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.net.service.UserJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.service.IMLoginService;
import com.ui.view.MyAsyncTask;
import com.utils.IntentUtil;
import com.utils.LogUtil;
import com.utils.StringUtil;
import com.utils.ToastUtil;

import org.json.JSONObject;

/*
 * 注册页面
 */
public class LoginActivity extends BaseActivity implements OnClickListener,
		TextWatcher {

	private static final String TAG = "Reg1Activity";
	private UserJsonService mUserJsonService;

	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mUserJsonService = new UserJsonService(mActivity);
		initparams();
		initView();
		//AnimationUtil.fadeAnimat(mActivity, root, true);
	}

	//private int login_forward_type;

	private boolean my_daikuan;
	private void initparams() {
		Bundle bundle = getIntent().getExtras();
		if (null == bundle)
			return;
		my_daikuan = bundle.getBoolean("my_daikuan");
		//login_forward_type = bundle.getInt(ParamsKey.login_forward_type);
	}

	private EditText phone_edit, verify_code_edit;
	private TextView verify_text, next_text;
	private View root;
	private void initView() {
		root = findViewById(R.id.root);
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("登录");
		mHeadView.hideRightTextView();
		phone_edit = (EditText) findViewById(R.id.phone_edit);
		verify_code_edit = (EditText) findViewById(R.id.verify_code_edit);
		verify_code_edit.addTextChangedListener(this);
		next_text = (TextView) findViewById(R.id.next_text);
		next_text.setOnClickListener(this);
		verify_text = (TextView) findViewById(R.id.verify_text);
		verify_text.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.next_text:
			// 下一步
			nextVerify();
			break;
		case R.id.verify_text:
			// 获取验证码
			verifyPhone();
			break;
		default:
			break;
		}
	}

	/*
	 * 下一步验证
	 */
	private String verify_num;

	private void nextVerify() {
		phone_num = phone_edit.getText().toString();
		verify_num = verify_code_edit.getText().toString();
		//LogUtil.d(TAG, "nextVerify==phone_num is "+phone_num+",verify_num is "+verify_num);
		if(!StringUtil.isMobileNO(phone_num)){
			ToastUtil.showToast(mActivity, 0, "请输入正确的手机号", true);
			return;
		}
		if (!StringUtil.isNumber(verify_num)) {
			ToastUtil.showToast(mActivity, 0, "请输入正确的验证码", true);
			return;
		}
		new AsyCommitVerifyCode(mActivity,"").execute();
	}

	/*
	 * 发送验证码
	 */
	private class AsySendVerifyCode extends MyAsyncTask {

		protected AsySendVerifyCode(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			mUserJsonService.setNeedCach(false);
			mUserJsonService.device_register();
			return mUserJsonService.userRegister_sendRegCode(phone_num);
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			JSONObject dataResult = (JSONObject) result;
			if (null == dataResult)
				return;
			int code = dataResult.optInt("code");
			JSONObject data = dataResult.optJSONObject("data");
			String message = data.optString("message");
			ToastUtil.showToast(mActivity, 0, message + " ", true);
			if (200 == code) {

			}
		}
	}

	/*
	 * 提交验证码,注册
	 */
	private class AsyCommitVerifyCode extends MyAsyncTask {

		protected AsyCommitVerifyCode(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			return mUserJsonService.userRegister_register(phone_num, verify_num);
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			JSONObject dataResult = (JSONObject) result;
			if (null == dataResult)
				return;
			int code = dataResult.optInt("code");
			JSONObject data = dataResult.optJSONObject("data");
			String message = data.optString("message");
			ToastUtil.showToast(mActivity, 0, message + " ", true);
			if (200 == code) {
				mUserJsonService.saveLoginInfo(data);
				//initOpenImLogin();
				mActivity.startService(new Intent(mActivity, IMLoginService.class));
				if(my_daikuan){
					IntentUtil.activityForward(mActivity, MyDaikuanActivity.class, null, true);
				}else{
					finish();
				}
			}
		}
	}

	private String phone_num;

	private void verifyPhone() {
		phone_num = phone_edit.getText().toString();
		if (!StringUtil.isMobileNO(phone_num)) {
			ToastUtil.showToast(mActivity, 0, "请输入正确的手机号码哟~", true);
			return;
		}
		new AsySendVerifyCode(mActivity,"获取验证码...").execute();
		startCountTime();
	}

	private void startCountTime() {
		MyCount myCount = new MyCount(60000, 1000);
		myCount.start();
	}

	private class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			verify_text.setText("获取验证码");
			verify_text.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			LogUtil.d(TAG, "倒计时==millisUntilFinished is " + millisUntilFinished);
			verify_text.setText(millisUntilFinished / 1000 + "秒后可再发");
			verify_text.setClickable(false);
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		String code = arg0.toString();
		if (StringUtil.isNumber(code) && code.length() == 6) {
			if (StringUtil.isMobileNO(phone_num)) {
				next_text.setSelected(true);
				next_text.setClickable(true);
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	protected int setContentView() {
		return R.layout.login;
	}

}
