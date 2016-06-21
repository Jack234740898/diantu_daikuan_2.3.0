package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.utils.ColorUtil;
import com.utils.DatetimeUtil;

import org.json.JSONObject;

public class SystemMsgAdapter extends MyBaseAdapter {

	public SystemMsgAdapter(Context con) {
		super(con);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(null==convertView){
			vh = new ViewHolder();
			convertView = mInflater.inflate(R.layout.system_msg_list_item, null);
			vh.title_text = (TextView) convertView.findViewById(R.id.title_text);
			vh.time_text = (TextView) convertView.findViewById(R.id.time_text);
			vh.content_text = (TextView) convertView.findViewById(R.id.content_text);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		bindViewData(vh,position);
		return convertView;
	}
	
	private void bindViewData(ViewHolder vh, int position) {
		JSONObject obj = dataList.get(position);
		long createTime = obj.optLong("createTime");
		int type = obj.optInt("type");
		int readFlag = obj.optInt("readFlag");
		String body = obj.optString("body");
		String time = DatetimeUtil.format(createTime);
		vh.time_text.setText(time+"");
		vh.content_text.setText(body+"");
		if(readFlag==1){
			//已读
			vh.time_text.setTextColor(ColorUtil.getColor(mContext, R.color.have_read_color));
			vh.content_text.setTextColor(ColorUtil.getColor(mContext, R.color.have_read_color));
			vh.title_text.setTextColor(ColorUtil.getColor(mContext, R.color.have_read_color));
		}else{
			vh.time_text.setTextColor(ColorUtil.getColor(mContext, R.color.time_color));
			vh.content_text.setTextColor(ColorUtil.getColor(mContext, R.color.desc_text_color));
			vh.title_text.setTextColor(ColorUtil.getColor(mContext, R.color.black));
		}
	}

	static final class ViewHolder{
		TextView title_text,time_text,content_text;
	}
}
