package com.sxjs.diantu_daikuan.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.AppConfig;
import com.sxjs.diantu_daikuan.R;

/*
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		initView();
		//AnimationUtil.fadeAnimat(mActivity, root, true);
	}

	private View root;
	private void initView() {
		root = findViewById(R.id.root);
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("关于我们");
		mHeadView.hideRightBtn();
		TextView version_text = (TextView) findViewById(R.id.version_text);
		version_text.setText("版本信息：V"+AppConfig.APP_VERSION);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImg:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected int setContentView() {
		return R.layout.about_us;
	}
}
