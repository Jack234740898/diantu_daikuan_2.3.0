package com.apreparey.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;

public class MyLvAdapter extends BaseAdapter{
	private Context context ;
	public MyLvAdapter(Context context){
		this.context = context ;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
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
		// TODO Auto-generated method stub
		convertView = View.inflate(context, R.layout.aatest_autolv_item, null);
		TextView tv = (TextView) convertView.findViewById(R.id.tv);
		tv.setText(""+position);
		return convertView ;
	}

}
