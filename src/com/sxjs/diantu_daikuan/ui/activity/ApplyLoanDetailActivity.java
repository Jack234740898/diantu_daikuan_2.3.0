package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.constants.GlobalFlag;
import com.constants.ParamsKey;
import com.net.service.UserJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.ApplyLoanDetailFgAdapter;
import com.ui.view.MyAsyncTask;
import com.utils.ScreenUtil;
import com.utils.ToastUtil;

import java.util.ArrayList;

/*
 * 单子详情
 */
public class ApplyLoanDetailActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "ApplyLoanDetailActivity";
	private UserJsonService mUserJsonService;
	private int screenW;
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mUserJsonService = new UserJsonService(mActivity);
		screenW = ScreenUtil.getWidth(mActivity);
		initParams();
		initView();
		//AnimationUtil.fadeAnimat(mActivity, root, true);
	}

	private int loanId,status,pd_id;
	private void initParams() {
		Bundle bundle = getIntent().getExtras();
		if(null==bundle)
			return;
		loanId = bundle.getInt(ParamsKey.loanId);
		status = bundle.getInt(ParamsKey.order_status);
		pd_id = bundle.getInt(ParamsKey.pd_id);
	}

	private LinearLayout navi_ll;
	private ViewPager viewPager;
	private void initView() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("单子详情");
		if(-1==status){
			mHeadView.setRightTextView("已取消", null);
		}else{
			mHeadView.setRightTextView("取消贷款", this);
		}
		
		navi_ll = (LinearLayout) findViewById(R.id.navi_ll);
		addNaviView();
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(new ApplyLoanDetailFgAdapter(getSupportFragmentManager(),loanId,pd_id));
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {	
				setNaviViewSelect(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	private class MyNaviClickListener implements OnClickListener{

		int curIndex;
		MyNaviClickListener(int curIndex){
			this.curIndex = curIndex;
		}
		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(curIndex);
		}
		
	}
	private ArrayList<View> viewList;
	private void addNaviView(){
		String[] navi_tab_content = new String[3];//{"申请状态","抢单经理","申请详情"};
		navi_tab_content[0]="申请状态";
		navi_tab_content[1]="抢单经理";
		navi_tab_content[2]="申请详情";
		int itemW = screenW/3;
		if(pd_id>0){
			navi_tab_content = new String[2];
			navi_tab_content[0]="申请状态";
			navi_tab_content[1]="申请详情";
			itemW = screenW/2;
		}
		viewList = new ArrayList<View>();
		navi_ll.removeAllViews();
		
		int itemH = ScreenUtil.dip2px(mActivity, 40.0f);
		for(int i=0;i<navi_tab_content.length;i++){
			View view = mInflater.inflate(R.layout.apply_loan_top_navi_view_item, null);
			RelativeLayout item_root = (RelativeLayout) view.findViewById(R.id.item_root);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemW,itemH);
			item_root.setLayoutParams(params);
			item_root.setOnClickListener(new MyNaviClickListener(i));
			TextView textview = (TextView) view.findViewById(R.id.textview);
			ImageView bottom_line_img = (ImageView) view.findViewById(R.id.bottom_line_img);
			ImageView right_line_img = (ImageView) view.findViewById(R.id.right_line_img);
			textview.setText(navi_tab_content[i]);
			navi_ll.addView(view);
			viewList.add(view);
		}
		setNaviViewSelect(0);
	}
	
	private void setNaviViewSelect(int index){
		for(int i=0;i<viewList.size();i++){
			View view = viewList.get(i);
			TextView textview = (TextView) view.findViewById(R.id.textview);
			ImageView bottom_line_img = (ImageView) view.findViewById(R.id.bottom_line_img);
			textview.setSelected(false);
			bottom_line_img.setVisibility(View.INVISIBLE);
			if(i==index){
				textview.setSelected(true);
				bottom_line_img.setVisibility(View.VISIBLE);
			}
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.rightText:
			//取消贷款
			cancle_loan();
			break;
		default:
			break;
		}
	}
	
	/*
	 * 取消贷款
	 */
	private void cancle_loan() {
		new AsyCancleLoan(mActivity,"").execute();
	}

	private class AsyCancleLoan extends MyAsyncTask{
		protected AsyCancleLoan(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			return mUserJsonService.loan_close(loanId);
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			//String msg = "取消贷款失败";
			if((Boolean)result){
				//finish();
				String msg = "取消贷款成功";
				mHeadView.setRightTextView("已取消", null);
				ToastUtil.showToast(mActivity, 0, msg, true);
			}
		}
	}
	private void setTextcolor(TextView textview){
		textview.setTextColor(mActivity.getResources().getColor(R.color.edit_text_color));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		GlobalFlag.scoreFlagMaps.clear();
	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.apply_loan_detail;
	}
}
