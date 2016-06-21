package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.utils.StringUtil;

import org.json.JSONObject;

public class SchoolAdapter extends MyBaseAdapter {

	public SchoolAdapter(Context con) {
		super(con);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder vh = null;
		if(null==view){
			vh = new ViewHolder();
			view = mInflater.inflate(R.layout.school_item, null);
			vh.textview = (TextView) view.findViewById(R.id.textview);
			view.setTag(vh);
		}else{
			vh = (ViewHolder) view.getTag();
		}
		bindviewData(position,vh);
		return view;
	}
	
private void bindviewData(int position, ViewHolder vh) {
	JSONObject obj = dataList.get(position);
	String universityName = obj.optString("universityName");
	vh.textview.setText("");
	if(StringUtil.checkStr(universityName))
		vh.textview.setText(universityName);
	}

	static final class ViewHolder {
		TextView textview;
	}
}
