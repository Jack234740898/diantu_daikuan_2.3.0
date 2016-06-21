package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.utils.ScreenUtil;

public class IdentifyChooseAdapter extends MyBaseAdapter {

	private int screenW,showW,showH;
	private String[] data;
	private int[] imgsId = {R.drawable.qi_ye_zhu,R.drawable.shang_ban_zhu,R.drawable.ge_ti_hu,
			R.drawable.student,R.drawable.gong_wu_yuan,R.drawable.other_worker};
	public IdentifyChooseAdapter(Context con) {
		super(con);
		data = con.getResources().getStringArray(R.array.home_workidentity_name);
		screenW = ScreenUtil.getWidth(mContext);
		int dash1 = ScreenUtil.dip2px(mContext, 12.0f);
		int dash2 = ScreenUtil.dip2px(mContext, 10.0f);
		showW = (screenW-2*dash1-2*dash2)/3;// 240*300
		showH = showW*300/240;
	}

	@Override
	public int getCount() {
		return imgsId.length;
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder vh=null;
		if(null==view){
			vh = new ViewHolder();
			view = mInflater.inflate(R.layout.identify_choose_item, null);
			vh.item_root = (LinearLayout) view.findViewById(R.id.item_root);
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
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(showW,showH);
		vh.item_root.setLayoutParams(params);
		vh.imgview.setBackgroundResource(imgsId[position]);
		vh.textview.setVisibility(View.GONE);
		vh.textview.setText(data[position]);
		
	}

	static final class ViewHolder{
		ImageView imgview;
		TextView textview;
		LinearLayout item_root;
	}
}
