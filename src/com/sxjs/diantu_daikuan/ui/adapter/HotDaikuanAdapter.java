package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.utils.ScreenUtil;
import com.utils.StringUtil;

import org.json.JSONObject;

public class HotDaikuanAdapter extends MyBaseAdapter {

	public HotDaikuanAdapter(Context con) {
		super(con);
	}

	private int type;
	public void setType(int type){
		this.type = type;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Viewholder vh = null;
		if(null==convertView){
			vh = new Viewholder();
			convertView = mInflater.inflate(R.layout.hot_daikuan_item, null);
			vh.img = (ImageView) convertView.findViewById(R.id.img);
			//vh.rate_name_text = (TextView) convertView.findViewById(R.id.rate_name_text);
			vh.text1 = (TextView) convertView.findViewById(R.id.text1);
			vh.rate_text = (TextView) convertView.findViewById(R.id.rate_text);
			//vh.rate_salary_text = (TextView) convertView.findViewById(R.id.rate_salary_text);
			vh.text3 = (TextView) convertView.findViewById(R.id.text3);
			convertView.setTag(vh);
		}else {
			vh = (Viewholder) convertView.getTag();
		}
		bindViewData(position,vh);
		return convertView;
	}
	
	private void bindViewData(int position, Viewholder vh) {
		final JSONObject obj = dataList.get(position);
		String companyName = obj.optString("companyName");
		String name = obj.optString("name");
		String icon = obj.optString("icon");
		double monthRate = obj.optDouble("monthRate");
		double monthFeeRate = obj.optDouble("monthFeeRate");
		String totalFee = obj.optString("totalFee");
		String listAdvantage = obj.optString("listAdvantage");
		if(2==type){
			int h = ScreenUtil.dip2px(mContext, 60.0f);
			int w = 270*h/180;
			int margin = ScreenUtil.dip2px(mContext, 10.0f);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(w,h);
			param.topMargin =margin;
			param.bottomMargin =margin;
			param.leftMargin =margin;
			param.rightMargin =margin;
			vh.img.setLayoutParams(param);
			mImgLoad.loadImg(vh.img, icon, R.drawable.default_xianjinfenqi_img);
		}else{
			mImgLoad.loadImg(vh.img, icon, R.drawable.default_bank_img);
		}
		
		vh.text1.setText(companyName + "-" + name);
		vh.rate_text.setVisibility(View.INVISIBLE);
		if(monthRate>0){
			vh.rate_name_text.setText("月利率 ");
			vh.rate_text.setVisibility(View.VISIBLE);
			vh.rate_text.setText(monthRate + "%");
		}else if(monthFeeRate>0){
			vh.rate_name_text.setText("月管理费 ");
			vh.rate_text.setVisibility(View.VISIBLE);
			vh.rate_text.setText(monthFeeRate + "%");
		}
		vh.rate_salary_text.setVisibility(View.INVISIBLE);
		if (StringUtil.checkStr(totalFee)) {
			vh.rate_salary_text.setVisibility(View.VISIBLE);
			vh.rate_salary_text.setText(totalFee + "万元");
		}
		vh.text3.setVisibility(View.GONE);
		if(StringUtil.checkStr(listAdvantage)){
			vh.text3.setVisibility(View.VISIBLE);
			vh.text3.setText(listAdvantage);
		}
		
	}

	static final class Viewholder {
		ImageView img;
		TextView rate_name_text,text1,rate_text,rate_salary_text,text3;
	}
}
