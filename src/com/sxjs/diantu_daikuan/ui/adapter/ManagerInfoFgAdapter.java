package com.sxjs.diantu_daikuan.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sxjs.diantu_daikuan.ui.fragment.CustomerReviewsFg;
import com.sxjs.diantu_daikuan.ui.fragment.RecentLoanFg;

import org.json.JSONArray;

public class ManagerInfoFgAdapter extends FragmentPagerAdapter {

	private JSONArray invest,score;
	//0 抢单中 -1000 全部
	public ManagerInfoFgAdapter(FragmentManager fm,JSONArray invest,JSONArray score) {
		super(fm);
		this.invest = invest;
		this.score = score;
	}

	@Override
	public Fragment getItem(int arg0) {
		if(0==arg0){
			return new RecentLoanFg(invest);
		}
		return new CustomerReviewsFg(score);
	}

	@Override
	public int getCount() {
		return 2;
	}

}
