package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.constants.ActionString;
import com.constants.GlobalFlag;
import com.constants.ParamsKey;
import com.sxjs.diantu_daikuan.R;
import com.utils.IntentUtil;
import com.utils.LogUtil;
import com.utils.StringUtil;

public class WebviewActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "WebviewActivity";

	private String web_url, head_name;

	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		initParams();
		initView();
	}

	private void initParams() {
		Bundle b = getIntent().getExtras();
		if (null == b)
			return;
		web_url = b.getString(ParamsKey.web_url);
		//web_url = "http://www.diantujinfu.com/appactivity";
		head_name = b.getString(ParamsKey.head_name);
	}

	private WebView webview;
	private void initView() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		if (StringUtil.checkStr(head_name))
			mHeadView.setCentreTextView(head_name + "");
		mHeadView.hideRightBtn();
		webview = (WebView) findViewById(R.id.webview);
		//webview.setVerticalScrollBarEnabled(false);
		//webview.setHorizontalScrollBarEnabled(false);		
		//webview.setInitialScale(scaleInPercent);
		WebSettings ws = webview.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setUseWideViewPort(true);
		ws.setSupportZoom(true);
		//
		//ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		ws.setLoadWithOverviewMode(true);
		LogUtil.d(TAG, "initView()==web_url is "+web_url);
		webview.loadUrl(web_url);
		webview.setWebViewClient(new MyWebViewClient());
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if(StringUtil.checkStr(url)&&url.contains("specialStatus=0")){
				GlobalFlag.specialStatus = 0;
				IntentUtil.activityForward(mActivity, ApplyDkActivity.class, null, true);
			}else{
				view.loadUrl(url);
			}
			return true;
		}
	}
	
	/*
	 * 广播通知跳转到第二个tab页面
	 */
	private void send_apply_loan_broad() {
		Intent intent = new Intent(ActionString.click_apply_loan_action);
		//intent.putExtra(ParamsKey.specialStatus, 0);
		GlobalFlag.specialStatus = 0;
		mActivity.sendBroadcast(intent);
		mActivity.finish();
	}

	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.leftImg:
			webview.clearCache(true);
			webview.clearView();
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected int setContentView() {
		return R.layout.webview;
	}
}
