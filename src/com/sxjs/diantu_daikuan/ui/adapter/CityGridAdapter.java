package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;

import org.json.JSONArray;

public class CityGridAdapter extends MyBaseAdapter {

	private JSONArray array;
	public CityGridAdapter(Context con,JSONArray array) {
		super(con);
		this.array = array;
	}

	@Override
	public int getCount() {
		return null==array?0:array.length();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(null==convertView){
			vh = new ViewHolder();
			convertView = mInflater.inflate(R.layout.city_textview_item, null);
			vh.city_textview = (TextView) convertView.findViewById(R.id.city_textview);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		String city_name = array.optString(position);
		vh.city_textview.setVisibility(View.VISIBLE);
		if("全国".equals(city_name)){
			vh.city_textview.setVisibility(View.GONE);
		}
		vh.city_textview.setText(city_name);
		return convertView;
	}
	
	static final class ViewHolder{
		TextView city_textview;
	}
}
