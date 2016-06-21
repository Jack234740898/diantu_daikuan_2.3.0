package com.sxjs.diantu_daikuan.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.constants.GlobalFlag;
import com.constants.ParamsKey;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.activity.ApplyDkActivity;
import com.sxjs.diantu_daikuan.ui.activity.ChooseGuarantyActivity;
import com.sxjs.diantu_daikuan.ui.adapter.IdentifyChooseAdapter;
import com.ui.view.MyGridView;
import com.utils.IntentUtil;
import com.utils.ScreenUtil;

public class ApplyDkFg extends BaseFragment implements OnClickListener,OnItemClickListener{
	private String[] data;
	private int screenW,loanCategory;
	
	@Override
	protected int setContentView() {
		return R.layout.choose_identity;
	}

	@Override
	protected void initView() {
		data = mActivity.getResources().getStringArray(R.array.home_workidentity_name);
		screenW = ScreenUtil.getWidth(mActivity);
		initV();
	}

	private MyGridView identify_gridview;
	@SuppressLint("ResourceAsColor")
	private void initV() {	
		findViewById(R.id.city_ll).setVisibility(View.GONE);
		findViewById(R.id.head_right_ll).setVisibility(View.GONE);
		//hideLeftBtn();
		mHeadView.setCentreTextView("申请贷款");
		//hideRightBtn();
		//828*260
		ImageView imgview = (ImageView) findViewById(R.id.imgview);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(screenW, screenW*260/828);
		imgview.setLayoutParams(param);
		identify_gridview = (MyGridView) findViewById(R.id.identify_gridview);
		identify_gridview.setAdapter(new IdentifyChooseAdapter(mActivity));
		identify_gridview.setOnItemClickListener(this);
		findViewById(R.id.ignore_step_text).setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(GlobalFlag.loanCategory>0){
			loanCategory = GlobalFlag.loanCategory;
			GlobalFlag.loanCategory=0;
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ignore_step_text:
			forward_dk(0,"上班族");//默认上班族
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		forward_dk(position+1,data[position]);
	}

	/*
	 * 跳转到发单页面
	 */
	private void forward_dk(int workingIdentity,String workingIdentityName){
		Bundle bundle = new Bundle();
		bundle.putString(ParamsKey.workingIdentityName, workingIdentityName);
		bundle.putInt(ParamsKey.workingIdentity, workingIdentity);
		bundle.putInt(ParamsKey.loanCategory, loanCategory);
		bundle.putInt(ParamsKey.guaranteeWill, -1);
		IntentUtil.activityForward(mActivity, ApplyDkActivity.class, bundle, false);
	}
	
	/*
	 * 跳转到选择抵押页面
	 */
	private void forward_diya_activiy1(int workingIdentity,String workingIdentityName){
		Bundle bundle = new Bundle();
		bundle.putString(ParamsKey.workingIdentityName, workingIdentityName);
		bundle.putInt(ParamsKey.workingIdentity, workingIdentity);
		bundle.putInt(ParamsKey.loanCategory, loanCategory);
		IntentUtil.activityForward(mActivity, ChooseGuarantyActivity.class, bundle, false);
	}
	
}
