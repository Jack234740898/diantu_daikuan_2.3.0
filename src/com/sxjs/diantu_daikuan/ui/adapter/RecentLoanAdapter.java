package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.constants.ParamsKey;
import com.sxjs.diantu_daikuan.R;
import com.utils.DatetimeUtil;

import org.json.JSONObject;

public class RecentLoanAdapter extends MyBaseAdapter {

	public RecentLoanAdapter(Context con) {
		super(con);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(null==convertView){
			vh = new ViewHolder();
			convertView = mInflater.inflate(R.layout.recent_loan_item, null);
			vh.text1 = (TextView) convertView.findViewById(R.id.text1);
			vh.text2 = (TextView) convertView.findViewById(R.id.text2);
			vh.text3 = (TextView) convertView.findViewById(R.id.text3);
			vh.bottom_line_img = convertView.findViewById(R.id.bottom_line_img);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		bindViewData(vh,position);
		return convertView;
	}
	
	private void bindViewData(ViewHolder vh,int position) {
		//vh.bottom_line_img.setVisibility(View.VISIBLE);
		if(position==(dataList.size()-1)){
			//vh.bottom_line_img.setVisibility(View.INVISIBLE);
		}
		JSONObject obj = dataList.get(position);
		int loanUseType = obj.optInt("loanUseType");
		int loanDays = obj.optInt("loanDays");
		long time = obj.optLong("time");
		vh.text1.setText(DatetimeUtil.getYMDTimeLocal2(time));
		vh.text2.setText("个人消费贷");
		if(ParamsKey.company_daikuan_type==loanUseType){
			vh.text2.setText("企业经营贷");
		}
		vh.text3.setText(loanDays+"天放款");
	}

	static final class ViewHolder{
		TextView text1,text2,text3;
		View bottom_line_img;
	}
}
