package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.utils.StringUtil;

import org.json.JSONObject;

public class BankServiceAdapter extends MyBaseAdapter {

	public BankServiceAdapter(Context con) {
		super(con);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(null==convertView){
			vh = new ViewHolder();
			convertView = mInflater.inflate(R.layout.bank_service_item, null);
			vh.img = (ImageView) convertView.findViewById(R.id.img);
			vh.text1 = (TextView) convertView.findViewById(R.id.text1);
			vh.text2 = (TextView) convertView.findViewById(R.id.text2);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		bindViewData(position,vh);
		return convertView;
	}
	
	private void bindViewData(int position, ViewHolder vh) {
		final JSONObject obj = dataList.get(position);
		String bankIcon = obj.optString("bankIcon");
		String bankName = obj.optString("bankName");
		String name = obj.optString("name");
		String detail = obj.optString("detail");
		String phoneNumber = obj.optString("phoneNumber");
		mImgLoad.loadImg(vh.img, bankIcon, R.drawable.default_bank_img);
		if (StringUtil.checkStr(bankName)) {
			vh.text1.setText(bankName);
		}
		vh.text2.setText(name + " " + detail);
	}

	static final class ViewHolder{
		ImageView img;
		TextView text1,text2;
	}
}
