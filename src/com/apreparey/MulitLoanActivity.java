package com.apreparey;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.net.service.BusinessJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.activity.BaseActivity;

public class MulitLoanActivity extends BaseActivity implements OnClickListener{

	private BusinessJsonService mBusinessService;
	
	@Override
	protected int setContentView() {
		return R.layout.aatest_multiloan;
	}

	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mBusinessService = new BusinessJsonService(mActivity);
		initView();
	}

	private void initView() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("申请贷款");
		mHeadView.hideRightBtn();
	}

	@Override
	public void onClick(View v) {
		
	}
	
}
