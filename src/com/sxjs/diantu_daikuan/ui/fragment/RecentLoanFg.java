package com.sxjs.diantu_daikuan.ui.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constants.ParamsKey;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.RecentLoanAdapter;
import com.utils.DatetimeUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class RecentLoanFg extends BaseFragment {

	private JSONArray invest;
	public RecentLoanFg(JSONArray invest){
		this.invest = invest;
	}
	@Override
	protected int setContentView() {
		return R.layout.recent_loan;
	}
	
	@Override
	protected void initView() {
		initV();
	}

	private TextView textview;
	private View empty_view;
	//private ListView listview;
	private View bottom_view;
	private LinearLayout view_group;
	private RecentLoanAdapter mAdapter;
	private void initV() {	
		bottom_view = findViewById(R.id.bottom_view);
		bottom_view.setVisibility(View.VISIBLE);
		view_group = (LinearLayout) findViewById(R.id.view_group);
		empty_view = findViewById(R.id.empty_view);
		empty_view.setVisibility(View.GONE);
		textview = (TextView) findViewById(R.id.textview);
		textview.setText("最近还无放款~");
		//listview = (ListView) findViewById(R.id.listview);
		//listview.setVisibility(View.VISIBLE);
		view_group.setVisibility(View.VISIBLE);
		if(null == invest || invest.length() <= 0){
			//listview.setVisibility(View.GONE);
			bottom_view.setVisibility(View.GONE);
			view_group.setVisibility(View.GONE);
			empty_view.setVisibility(View.VISIBLE);
			empty_view.setBackgroundColor(Color.WHITE);
		}else{
			/*listview.setAdapter(mAdapter = new RecentLoanAdapter(mActivity));
			mAdapter.setData(JSONUtil.arrayToList(invest));
			mAdapter.notifyDataSetChanged();*/
			addView();
		}
		
	}


	private void addView(){
		view_group.removeAllViews();
		for(int i=0;i<invest.length();i++){
			View view = mInflater.inflate(R.layout.recent_loan_item, null);
			TextView text1 = (TextView) view.findViewById(R.id.text1);
			TextView text2 = (TextView) view.findViewById(R.id.text2);
			TextView text3 = (TextView) view.findViewById(R.id.text3);
			JSONObject obj = invest.optJSONObject(i);
			int loanUseType = obj.optInt("loanUseType");
			int loanDays = obj.optInt("loanDays");
			long time = obj.optLong("time");
			text1.setText(DatetimeUtil.getYMDTimeLocal2(time));
			text2.setText("个人消费贷");
			if(ParamsKey.company_daikuan_type==loanUseType){
				text2.setText("企业经营贷");
			}
			text3.setText(loanDays+"天放款");
			view_group.addView(view);
		}
	}
}
