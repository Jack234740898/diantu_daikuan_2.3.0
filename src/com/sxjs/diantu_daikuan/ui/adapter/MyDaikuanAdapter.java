package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.utils.DatetimeUtil;
import com.utils.StringUtil;

import org.json.JSONObject;

public class MyDaikuanAdapter extends MyBaseAdapter {

	public MyDaikuanAdapter(Context con) {
		super(con);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(null==convertView){
			convertView = mInflater.inflate(R.layout.my_daikuan_item, null);
			vh = new ViewHolder();
			vh.daikuan_salary_text = (TextView) convertView.findViewById(R.id.daikuan_salary_text);
			vh.daikuan_type_text = (TextView) convertView.findViewById(R.id.daikuan_type_text);
			vh.status_text = (TextView) convertView.findViewById(R.id.status_text);
			vh.daikuan_term_text = (TextView) convertView.findViewById(R.id.daikuan_term_text);
			vh.apply_time_text = (TextView) convertView.findViewById(R.id.apply_time_text);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		bindViewData(position,vh);
		return convertView;
	}
	
	private void bindViewData(int position, ViewHolder vh) {
		JSONObject obj = dataList.get(position);
		String userName = obj.optString("userName");
		int useType = obj.optInt("useType");
		String amount = obj.optString("amount");
		String loanCategory = obj.optString("loanCategory");
		String workingIdentityStr = obj.optString("workingIdentityStr");
		int workingIdentity = obj.optInt("workingIdentity");
		int term = obj.optInt("term");
		int status = obj.optInt("status");
		long createTime = obj.optLong("createTime");
		if(1==useType){
			vh.daikuan_type_text.setText("企业经营贷");
		}else {
			String dk_type_str = "个人消费贷";
			if(StringUtil.checkStr(loanCategory)&&StringUtil.checkStr(workingIdentityStr)){
				dk_type_str = workingIdentityStr+" - "+loanCategory;
			}else{
				if(StringUtil.checkStr(workingIdentityStr)){
					dk_type_str = workingIdentityStr;
				}
				if(StringUtil.checkStr(loanCategory)){
					dk_type_str = loanCategory;
				}
			}
			vh.daikuan_type_text.setText(dk_type_str);
		}
			
		vh.daikuan_salary_text.setText(0+"元");
		if(StringUtil.isIntNum(amount)){
			int amountInt = Integer.parseInt(amount);
			vh.daikuan_salary_text.setText(amountInt+"元");		
		}
		vh.daikuan_term_text.setText(term+"个月");//DatetimeUtil.getYearMonth(term)
		vh.apply_time_text.setText("申请时间："+DatetimeUtil.getTimeLocal(createTime));
		if(-1==status){
			////电兔贷款显示 -1已取消 其他提交成功 add 2015-09-25
			vh.status_text.setBackgroundResource(R.drawable.have_cancle);
		}else{
			vh.status_text.setBackgroundResource(R.drawable.commit_success_ico);
		}
	}

	static final class ViewHolder {
		TextView daikuan_type_text,status_text,daikuan_salary_text,daikuan_term_text,apply_time_text;
	}
}
