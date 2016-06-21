package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constants.ParamsKey;
import com.net.service.BusinessJsonService;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.MyAsyncTask;
import com.utils.IntentUtil;
import com.utils.JSONUtil;
import com.utils.LogUtil;
import com.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
 * 贷款产品详情
 */
public class DK_PD_DetailActivity extends BaseActivity implements
		OnClickListener {

	private BusinessJsonService mBusinessService;

	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mBusinessService = new BusinessJsonService(mActivity);
		initParams();
		initView();
		loadData();
	}

	private int pd_id, dkType;

	private void initParams() {
		Bundle bundle = getIntent().getExtras();
		pd_id = bundle.getInt(ParamsKey.pd_id);
	}

	private LinearLayout apply_conditon_group, need_material_group;
	private TextView product_name_text, product_addr_text, dk_salary_text,
			dk_term_text, manage_fee_text, other_contents_text,
			need_material_text;
	private View body;
	private ImageView appy_condition_img,need_material_img,other_contents_img;
	private void initView() {
		body = findViewById(R.id.body);
		body.setVisibility(View.INVISIBLE);
		findViewById(R.id.leftImg).setOnClickListener(this);
		findViewById(R.id.apply_dk_text).setOnClickListener(this);
		appy_condition_img = (ImageView) findViewById(R.id.appy_condition_img);
		appy_condition_img.setOnClickListener(this);
		need_material_img = (ImageView) findViewById(R.id.need_material_img);
		need_material_img.setOnClickListener(this);
		other_contents_img = (ImageView) findViewById(R.id.other_contents_img);
		other_contents_img.setOnClickListener(this);
		apply_conditon_group = (LinearLayout) findViewById(R.id.apply_conditon_group);
		need_material_group = (LinearLayout) findViewById(R.id.need_material_group);
		other_contents_text = (TextView) findViewById(R.id.other_contents_text);
		product_name_text = (TextView) findViewById(R.id.product_name_text);
		product_addr_text = (TextView) findViewById(R.id.product_addr_text);
		dk_salary_text = (TextView) findViewById(R.id.dk_salary_text);
		dk_term_text = (TextView) findViewById(R.id.dk_term_text);
		manage_fee_text = (TextView) findViewById(R.id.manage_fee_text);
		need_material_text = (TextView) findViewById(R.id.need_material_text);
	}

	@Override
	protected void loadData() {
		super.loadData();
		new AsyLoadData(mActivity,"").execute();
	}

	private class AsyLoadData extends MyAsyncTask {
		protected AsyLoadData(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			mBusinessService.setNeedCach(false);
			return mBusinessService.qd_pd_detail(pd_id);
		}

		@Override
		protected void onPostExecute(Object data) {
			super.onPostExecute(data);
			if (null == data)
				return;
			body.setVisibility(View.VISIBLE);
			bindviewData((JSONObject) data);
		}
	}

	/*
	 * 添加所需材料
	 */
	private void add_need_material(JSONArray certificate) {
		need_material_text.setVisibility(View.VISIBLE);
		need_material_group.setVisibility(View.GONE);
		need_material_img.setVisibility(View.GONE);
		if (null == certificate || certificate.length() <= 0)
			return;
		need_material_text.setVisibility(View.GONE);
		need_material_group.setVisibility(View.VISIBLE);
		need_material_img.setVisibility(View.VISIBLE);
		int length = certificate.length();// ;
		need_material_group.removeAllViews();
		for (int i = 0; i < length; i++) {
			View view = mInflater.inflate(R.layout.dk_pd_detail_item, null);
			TextView key_text = (TextView) view.findViewById(R.id.key_text);
			TextView value_text = (TextView) view.findViewById(R.id.value_text);
			View dash_view = findViewById(R.id.dash_view);
			dash_view.setVisibility(View.VISIBLE);
			key_text.setVisibility(View.GONE);
			if (i == length - 1) {
				dash_view.setVisibility(View.GONE);
			}
			String ss = certificate.optString(i);
			value_text.setText((i+1)+"." + ss);
			need_material_group.addView(view);
		}
	}

	private HashMap<String, String> conditionMaps;
	private JSONArray certificate;
	public void bindviewData(JSONObject data) {
		conditionMaps = new HashMap<String, String>();
		dkType = data.optInt("typeNum");
		String typeString = data.optString("type");
		String companyName = data.optString("companyName");
		String city = data.optString("city");
		String name = data.optString("name");
		int amountMin = data.optInt("amountMin");
		int amountMax = data.optInt("amountMax");
		int termMin = data.optInt("termMin");
		int termMax = data.optInt("termMax");
		int ageMin = data.optInt("ageMin");
		int ageMax = data.optInt("ageMax");
		int workingAge = data.optInt("workingAge");
		String rateType = data.optString("rateType");
		int income = data.optInt("income");
		int incomeCash = data.optInt("incomeCash");
		JSONArray creditRecord = data.optJSONArray("creditRecord");
		certificate = data.optJSONArray("certificate");
		String content1 = data.optString("content1");
		double onceFee = data.optDouble("onceFee");
		JSONArray repaymentType = data.optJSONArray("repaymentType");
		JSONArray guaranteeType = data.optJSONArray("guaranteeType");
		int loanDay = data.optInt("loanDay");
		JSONArray wokingIdentity = data.optJSONArray("wokingIdentity");
		String socialSecurityAge = data.optString("socialSecurityAge");
		int housingFund = data.optInt("housingFund");
		JSONObject rateString = data.optJSONObject("rateString");
		product_name_text.setText("");
		if (StringUtil.checkStr(name)) {
			product_name_text.setText(name);
		}
		product_addr_text.setText("");
		if (StringUtil.checkStr(companyName)) {
			product_addr_text.setText(companyName);
		}
		dk_salary_text.setText(amountMin + "元-" + amountMax + "元");
		dk_term_text.setText(termMin + "个月-" + termMax + "个月");
		
		LogUtil.d("getJSONArrayStr", "rateString is "+rateString);
		manage_fee_text.setVisibility(View.GONE);
		if (StringUtil.checkStr(rateType)) {
			manage_fee_text.setVisibility(View.VISIBLE);
			// [1:2.0,12:3.0]
			String rateStr = StringUtil.getJSONObjectStr(rateString);
			if (StringUtil.checkStr(rateStr)) {
				manage_fee_text.setText(rateType + "：" + rateStr);
			} else {
				manage_fee_text.setText(rateType + "：无");
			}
		}

		conditionMaps.put("申请人所在地", city + "");
		conditionMaps.put("一次性服务费率", onceFee + "%");
		conditionMaps
				.put("还款方式", JSONUtil.arryToStringWithSplit(repaymentType));
		conditionMaps
				.put("是否抵押", JSONUtil.arryToStringWithSplit(guaranteeType));
		conditionMaps.put("放款时间", loanDay + "天以内");
		conditionMaps.put("职业要求",
				JSONUtil.arryToStringWithSplit(wokingIdentity));
		conditionMaps.put("年龄限制", ageMin + "周岁到" + ageMax + "周岁");
		conditionMaps.put("工作年限", workingAge + "月以上");
		conditionMaps.put("社保缴纳", socialSecurityAge+"月以上");
		conditionMaps.put("公积金缴纳", housingFund + "元以上每月");
		conditionMaps.put("贷款类型", typeString);
		String incomeStr = null;
		if (income > 0) {
			incomeStr = "月打卡工资" + income + "元";
		}
		if (incomeCash > 0) {
			if (income > 0) {
				incomeStr = incomeStr + "或" + "月现金工资" + incomeCash + "元";
			}
		}
		conditionMaps.put("月收入", incomeStr);
		conditionMaps.put("征信要求", JSONUtil.arryToStringWithSplit(creditRecord));
		add_apply_condition(conditionMaps);
		add_need_material(certificate);
		other_contents_img.setVisibility(View.GONE);
		other_contents_text.setText("暂无");
		if (StringUtil.checkStr(content1)) {
			other_contents_text.setText(content1);
			other_contents_img.setVisibility(View.VISIBLE);
		}
	}

	/*
	 * 
	 * 添加申请条件
	 */
	private void add_apply_condition(HashMap<String, String> conditionMaps) {
		if (null == conditionMaps || conditionMaps.isEmpty())
			return;
		int i = -1;
		apply_conditon_group.removeAllViews();
		for (Map.Entry<String, String> map : conditionMaps.entrySet()) {
			i++;
			String key = map.getKey();
			String value = map.getValue();
			View view = mInflater.inflate(R.layout.dk_pd_detail_item, null);
			TextView key_text = (TextView) view.findViewById(R.id.key_text);
			TextView value_text = (TextView) view.findViewById(R.id.value_text);
			View dash_view = view.findViewById(R.id.dash_view);
			dash_view.setVisibility(View.VISIBLE);
			if (i == conditionMaps.size() - 1) {
				dash_view.setVisibility(View.GONE);
			}
			key_text.setText(key + "");
			value_text.setText("无");
			if (StringUtil.checkStr(value)) {
				value_text.setText(value);
			}
			apply_conditon_group.addView(view);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.appy_condition_img:
			showContent(apply_conditon_group,appy_condition_img);
			break;
		case R.id.need_material_img:
			if(null!=certificate&&certificate.length()>0){
				showContent(need_material_group,need_material_img);
			}
			break;
		case R.id.other_contents_img:
			showContent(other_contents_text,other_contents_img);
			break;
		case R.id.apply_dk_text:
			//send_apply_loan_broad();
			Bundle bundle = new Bundle();
			bundle.putInt(ParamsKey.userType, dkType);
			bundle.putInt(ParamsKey.pd_id, pd_id);
			IntentUtil.activityForward(mActivity, ApplyDkActivity.class, bundle, false);
			/*Bundle bundle = new Bundle();
			bundle.putInt(ParamsKey.pd_id, pd_id);
			bundle.putInt(ParamsKey.daikuan_type, dkType);
			IntentUtil.activityForward(mActivity, ApplyLoanActivity.class, bundle, false);*/
			break;
		default:
			break;
		}
	}

	//appy_condition_img,need_material_img,other_contents_img;
	private void showContent(View view,ImageView imgview) {
		if (view.getVisibility() == View.VISIBLE) {
			view.setVisibility(View.GONE);
			imgview.setBackgroundResource(R.drawable.up_arrow);
		} else {
			view.setVisibility(View.VISIBLE);
			imgview.setBackgroundResource(R.drawable.down_arrow);
		}
	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.dk_pd_detail;
	}
}
