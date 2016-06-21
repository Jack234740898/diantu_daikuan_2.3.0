package com.sxjs.diantu_daikuan.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sxjs.diantu_daikuan.ui.fragment.ApplyDetailFg;
import com.sxjs.diantu_daikuan.ui.fragment.ApplyStatusFg;
import com.sxjs.diantu_daikuan.ui.fragment.OrderManagerFg;

public class ApplyLoanDetailFgAdapter extends FragmentPagerAdapter {

	private int loanId,pd_id;
	public ApplyLoanDetailFgAdapter(FragmentManager fm,int loanId,int pd_id) {
		super(fm);
		this.loanId = loanId;
		this.pd_id = pd_id;
	}

	@Override
	public Fragment getItem(int arg0) {
		if(0==arg0){
			return new ApplyStatusFg(loanId,pd_id);
		}else if(1==arg0){
			if(pd_id<=0){
				return new OrderManagerFg(loanId);
			}
		}
		return new ApplyDetailFg(loanId);
	}

	@Override
	public int getCount() {
		if(pd_id>0){
			return 2;
		}else{
			return 3;
		}
	}

}
