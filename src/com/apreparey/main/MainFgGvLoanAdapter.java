package com.apreparey.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sxjs.diantu_daikuan.R;

public class MainFgGvLoanAdapter extends BaseAdapter{
	private Context mContet;
	public MainFgGvLoanAdapter(Context context) {
		mContet = context ;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(mContet, R.layout.aatest_mainhome_loan_gv_item , null) ;
		return convertView ;
	}

}
