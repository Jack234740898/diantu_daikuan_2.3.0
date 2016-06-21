package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.net.service.UserJsonService;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.MyAsyncTask;
import com.utils.StringUtil;
import com.utils.ToastUtil;

/*
 * 实名认证
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "FeedBackActivity";
	private UserJsonService mUserJsonService;
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mUserJsonService = new UserJsonService(mActivity);
		initView();
		//AnimationUtil.fadeAnimat(mActivity, root, true);
	}

	private View root;
	private EditText content_edit;
	private void initView() {
		root = findViewById(R.id.root);
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("意见反馈");
		mHeadView.setRightTextView("提交", this);
		content_edit = (EditText) findViewById(R.id.content_edit);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.rightText:
			commit();
			break;
		default:
			break;
		}
	}

	/*
	 * 提交
	 */
	private String content;
	private void commit() {
		content = content_edit.getText().toString();
		if(!StringUtil.checkStr(content)){
			ToastUtil.showToast(mActivity, 0, "内容不能为空哟", true);
			return;
		}
		new AsyCommit(mActivity,"").execute();
	}
	private class AsyCommit extends MyAsyncTask{

		protected AsyCommit(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			
			return mUserJsonService.feedback_add(content);
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if((Boolean)result){
				ToastUtil.showToast(mActivity, 0, "提交成功", true);
				finish();
			}else{
				ToastUtil.showToast(mActivity, 0, "提交失败", true);
			}
		}
	}
	@Override
	protected int setContentView() {
		return R.layout.feed_back;
	}
}
