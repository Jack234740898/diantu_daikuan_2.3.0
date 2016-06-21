package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.utils.StringUtil;

import org.json.JSONObject;

public class ScoreMarkGridAdapter extends MyBaseAdapter {

	
	public ScoreMarkGridAdapter(Context con) {
		super(con);
	}

	private int mPos=0;
	public void setSelect(int position) {
		this.mPos = position;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder vh=null;
        if(null==view){
        	view = mInflater.inflate(R.layout.score_mark_grid_item, null);
        	vh = new ViewHolder();
        	vh.textview = (TextView) view.findViewById(R.id.textview);
        	view.setTag(vh);
		}else{
			vh = (ViewHolder) view.getTag();
		}
        bindViewData(vh,position);
		return view;
	}
	private void bindViewData(ViewHolder vh, int position) {
		JSONObject obj = dataList.get(position);
		String label = obj.optString("label");
		vh.textview.setText("");
		if(StringUtil.checkStr(label)){
			vh.textview.setText(label);
		}
		if(mPos==position){
			vh.textview.setSelected(true);
		}else{
			vh.textview.setSelected(false);
		}
	}
	
	static final class ViewHolder{
		TextView textview;
	}
	
}
