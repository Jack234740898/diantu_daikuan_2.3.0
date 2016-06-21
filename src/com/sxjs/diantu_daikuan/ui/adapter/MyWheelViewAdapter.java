package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.utils.ColorUtil;
import com.utils.StringUtil;

import org.json.JSONArray;

import kankan.wheel.widget.adapters.WheelViewAdapter;

public class MyWheelViewAdapter implements WheelViewAdapter {

	private Context mContext;
	private JSONArray array;
	private LayoutInflater mInflater;
	public MyWheelViewAdapter(Context context,JSONArray array){
		this.mContext = context;
		this.array = array;
		mInflater = LayoutInflater.from(mContext);
	}
	
	private int selection=-1;
	public void setSelection(int selection){
		this.selection = selection;
	}
	/*HashMap<Integer, TextView> textviewMaps1;
	public void setTextViewMap1(HashMap<Integer, TextView> textviewMaps1) {
		this.textviewMaps1 = textviewMaps1;
	}
	HashMap<Integer, TextView> textviewMaps2;
	public void setTextViewMap2(HashMap<Integer, TextView> textviewMaps2) {
		this.textviewMaps2 = textviewMaps2;
	}*/
	
	@Override
	public int getItemsCount() {
		return null==array?0:array.length();
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.wheel_textview_item, null);
		TextView wheel_name = (TextView) convertView.findViewById(R.id.wheel_name);
		String label = array.optJSONObject(index).optString("label");
		if(!StringUtil.checkStr(label)){
			//放款证明添加的
			label = array.optJSONObject(index).optString("name");
		}
		wheel_name.setText(label+"");
		//textviewMaps1.put(index, wheel_name);
		//textviewMaps2.put(index, wheel_name);
		wheel_name.setTextColor(ColorUtil.getColor(mContext, R.color.desc_text_color));
		if(selection==index){
			wheel_name.setTextColor(ColorUtil.getColor(mContext, R.color.title_text_color));
		}
		return convertView;
	}

	@Override
	public View getEmptyItem(View convertView, ViewGroup parent) {
		return convertView;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

}
