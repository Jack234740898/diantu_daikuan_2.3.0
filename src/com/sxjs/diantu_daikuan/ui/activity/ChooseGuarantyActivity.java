package com.sxjs.diantu_daikuan.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.constants.ParamsKey;
import com.sxjs.diantu_daikuan.R;
import com.utils.IntentUtil;

/*
 * 选择身份
 */
public class ChooseGuarantyActivity extends BaseActivity implements OnClickListener{

	protected void onInit(Bundle bundle) {
		initParams();
		initView();
	}

	private String workingIdentityName;
	private int workingIdentity,loanCategory;
	private void initParams() {
		Bundle bundle = getIntent().getExtras();
		if(null==bundle)
			return;
		workingIdentity = bundle.getInt(ParamsKey.workingIdentity);
		loanCategory = bundle.getInt(ParamsKey.loanCategory);
		workingIdentityName = bundle.getString(ParamsKey.workingIdentityName);
	}

	private void initView() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("申请贷款");
		mHeadView.hideRightBtn();
		findViewById(R.id.diya_text).setOnClickListener(this);
		findViewById(R.id.no_diya_text).setOnClickListener(this);
		findViewById(R.id.ignore_step_text).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.diya_text:
			forward_activity(1);
			break;
		case R.id.no_diya_text:
			forward_activity(-1);
			break;
		case R.id.ignore_step_text:
			forward_activity(0);
			break;
		default:
			break;
		}
	}
	private void forward_activity(int guaranteeWill) {
		Bundle bundle = new Bundle();
		bundle.putInt(ParamsKey.guaranteeWill, guaranteeWill);
		bundle.putInt(ParamsKey.workingIdentity, workingIdentity);
		bundle.putString(ParamsKey.workingIdentityName, workingIdentityName);
		IntentUtil.activityForward(mActivity, ApplyDkActivity.class, bundle, false);
	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.choose_guaranty;
	}
}
