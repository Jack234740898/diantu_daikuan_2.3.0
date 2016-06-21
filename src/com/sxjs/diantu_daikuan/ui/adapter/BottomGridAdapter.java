package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.utils.StringUtil;

import org.json.JSONObject;

public class BottomGridAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private JSONObject obj;
	private int useType;
	public BottomGridAdapter(Context context,JSONObject obj){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.obj = obj;
		useType = obj.optInt("useType");
	}
	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = mInflater.inflate(R.layout.manager_info_item, null);
		TextView key_text = (TextView) view.findViewById(R.id.key_text);
		TextView value_text = (TextView) view.findViewById(R.id.value_text);
		if(0==position){
			String city = obj.optString("city");
			key_text.setText("城市");
			value_text.setText("全国");
			if(StringUtil.checkStr(city)){
				value_text.setText(city);
			}
		}else if(1==position){
			String term = obj.optString("term");
			key_text.setText("期限");
			value_text.setText("无");
			if(StringUtil.checkStr(term)){
				value_text.setText(term);
			}
		}else if(2==position){//0 个人消费贷 1 企业经营贷
			String income = obj.optString("income");
			key_text.setText("年流水(万元)");
			if(0==useType){
				key_text.setText("月工资(元)");
			}
			value_text.setText("￥"+income);
		}else if(3==position){
			String creditRecord = obj.optString("creditRecord");
			key_text.setText("信用");
			value_text.setText(creditRecord+"");
			value_text.setTextColor(mContext.getResources().getColor(R.color.orange_red_text));
		}else if(4==position){
			String guaranteeType = obj.optString("guaranteeType");
			String socialSecurityAge = obj.optString("socialSecurityAge");
			key_text.setText("抵押");
			value_text.setText(guaranteeType+"");
			if(0==useType){
				key_text.setText("社保");
				value_text.setText(socialSecurityAge+"");
			}
		}
		return view;
	}

}
