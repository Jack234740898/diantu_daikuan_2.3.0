package com.apreparey;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.activity.BaseActivity;

public class 产品详情 extends BaseActivity implements OnClickListener {
	
	private ImageView img_product ;
	private TextView tv_product_name ;
	private TextView tv_product_num ;
	private TextView tv_product_time ;
	
	@Override
	protected int setContentView() {
		return R.layout.aatest_productdetail;
	}
	
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		initView();
	}

	private void initView(){
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("申请贷款");
		mHeadView.hideRightBtn();
	}

	@Override
	public void onClick(View v) {
		finish();
	}

}
