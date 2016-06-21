package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;

import org.json.JSONArray;

import java.util.HashMap;

/*
 * huankuan_way_gridview
 */
public class OrderSettingAdapter extends MyBaseAdapter{

	public HashMap<Integer, Boolean> selectedMaps;
	private JSONArray array;
	public OrderSettingAdapter(Context con,JSONArray array) {
		super(con);
		this.array = array;
		selectedMaps = new HashMap<Integer, Boolean>();
	}	
	
	private int selection=-1;
	public void setSelection(int selection){
		this.selection = selection;
	}
	
	@Override
	public int getCount() {
		return null==array?0:array.length();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(null==convertView){
			vh = new ViewHolder();
			convertView = mInflater.inflate(R.layout.order_setting_item, null);
			vh.textview = (TextView) convertView.findViewById(R.id.textview);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		vh.textview.setSelected(false);
		if(selection==position){
			vh.textview.setSelected(true);
		}
		if(selectedMaps.containsKey(position)){
			boolean selected = selectedMaps.get(position);
			vh.textview.setSelected(selected);
		}
		String label = array.optJSONObject(position).optString("label");
		vh.textview.setText(label+"");
		return convertView;
	}
	
	static final class ViewHolder{
		TextView textview;
	}
}
