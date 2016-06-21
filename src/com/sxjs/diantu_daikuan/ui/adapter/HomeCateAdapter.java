package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;

public class HomeCateAdapter extends MyBaseAdapter {

	private String[] data;
	private int[] imgsId = {R.drawable.home_buyhouse,R.drawable.home_buycar,R.drawable.home_weding,
			R.drawable.home_decorate,R.drawable.home_tourism,R.drawable.home_consume,
			R.drawable.home_business,R.drawable.home_abroad,};
	public HomeCateAdapter(Context con) {
		super(con);
		data = con.getResources().getStringArray(R.array.home_cate_name);
	}

	@Override
	public int getCount() {
		return data.length;
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder vh=null;
		if(null==view){
			vh = new ViewHolder();
			view = mInflater.inflate(R.layout.home_cate_grid_item, null);
			vh.imgview = (ImageView) view.findViewById(R.id.imgview);
			vh.textview = (TextView) view.findViewById(R.id.textview);
			view.setTag(vh);
		}else{
			vh = (ViewHolder) view.getTag();
		}
		bindviewData(position,vh);
		return view;
	}
	
	private void bindviewData(int position, ViewHolder vh) {
		vh.imgview.setBackgroundResource(imgsId[position]);
		vh.textview.setText(data[position]);
		
	}

	static final class ViewHolder{
		ImageView imgview;
		TextView textview;
	}
}
