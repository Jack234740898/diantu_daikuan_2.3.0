package com.apreparey;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.MyBaseAdapter;

public class MyCarCreditAdapter extends MyBaseAdapter {

	public MyCarCreditAdapter(Context con) {
		super(con);
	}

	@Override
	public int getCount() {
		return 20 ;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		return View.inflate(mContext,R.layout.aatest_carcredit_item , null ) ;
	}
	
}
