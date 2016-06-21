package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constants.ParamsKey;
import com.db.UserData;
import com.net.service.BusinessJsonService;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.MyAsyncTask;
import com.utils.DialogUtil;
import com.utils.IntentUtil;
import com.utils.LogUtil;
import com.utils.ScreenUtil;
import com.utils.StringUtil;

import org.json.JSONObject;

import java.util.ArrayList;

/*
 * 热门贷款|现金分期 详情
 * http://test.daigj.com/api/adr/productHot/detail
 */
public class DaikuanProductDetailActivity extends BaseActivity implements
		OnClickListener {

	private static final String TAG = "DaikuanProductDetailActivity";
	private BusinessJsonService mBusinessJsonService;
	private String[] detail_item_name;
	private ArrayList<String> hot_daikuan_detail_value;

	private static final String apply_progress = "办理流程";

	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mBusinessJsonService = new BusinessJsonService(mActivity);
		detail_item_name = mActivity.getResources().getStringArray(
				R.array.hot_daikuan_detail_name);
		hot_daikuan_detail_value = new ArrayList<String>();
		initParams();
		initView();
		//AnimationUtil.fadeAnimat(mActivity, root, true);
		loadData();
	}

	private int id;
	private String head_name;

	private void initParams() {
		Bundle bundle = getIntent().getExtras();
		if (null == bundle)
			return;
		id = bundle.getInt(ParamsKey.id);
		head_name = bundle.getString(ParamsKey.head_name);
	}

	private ImageView ico_img;
	private LinearLayout product_detail_group;
	private TextView apply_text, need_text, rate_text, quick_apply_text,
			name_text, salary_text, daikuan_term_text, month_huankuan_text,
			total_rate_text,text22,text44,name_text1,name_text2,name_text3,name_text4,product_tese_text;
	private View root,body,product_tese_ll,rates_ll;
	private void initView() {
		root = findViewById(R.id.root);
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("现金分期详情");
		if (StringUtil.checkStr(head_name)) {
			//setCentreTextView(head_name);
		}
		mHeadView.hideRightBtn();
		body = findViewById(R.id.body);
		body.setVisibility(View.INVISIBLE);
		ico_img = (ImageView) findViewById(R.id.ico_img);
		name_text = (TextView) findViewById(R.id.name_text);
		text22 = (TextView) findViewById(R.id.text22);
		text44 = (TextView) findViewById(R.id.text44);
		salary_text = (TextView) findViewById(R.id.salary_text);
		daikuan_term_text = (TextView) findViewById(R.id.daikuan_term_text);
		month_huankuan_text = (TextView) findViewById(R.id.month_huankuan_text);
		total_rate_text = (TextView) findViewById(R.id.total_rate_text);
		name_text1 = (TextView) findViewById(R.id.name_text1);
		name_text2 = (TextView) findViewById(R.id.name_text2);
		name_text3 = (TextView) findViewById(R.id.name_text3);
		name_text4 = (TextView) findViewById(R.id.name_text4);
		product_tese_ll = findViewById(R.id.product_tese_ll);
		product_tese_text = (TextView) findViewById(R.id.product_tese_text);
		rates_ll = findViewById(R.id.rates_ll);

		product_detail_group = (LinearLayout) findViewById(R.id.product_detail_group);
		apply_text = (TextView) findViewById(R.id.apply_text);
		need_text = (TextView) findViewById(R.id.need_text);
		rate_text = (TextView) findViewById(R.id.rate_text);
		quick_apply_text = (TextView) findViewById(R.id.quick_apply_text);
		quick_apply_text.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		super.loadData();
		new AsyLoadata(mActivity,"").execute();
	}

	private class AsyLoadata extends MyAsyncTask {

		protected AsyLoadata(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			mBusinessJsonService.setNeedCach(false);
			return mBusinessJsonService.productHot_detail(id);
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			LogUtil.d(TAG, "onPostExecute==id is " + id + ",result is "
					+ result);
			if (null == result)
				return;
			body.setVisibility(View.VISIBLE);
			bindViewData((JSONObject) result);
		}
	}

	private void add_product_detail() {
		int length = detail_item_name.length;
		product_detail_group.removeAllViews();
		for (int i = 0; i < length; i++) {
			View item_view = mInflater.inflate(R.layout.product_detail_item,null);
			View product_root = item_view.findViewById(R.id.product_root);
			TextView text1 = (TextView) item_view.findViewById(R.id.text1);
			TextView text2 = (TextView) item_view.findViewById(R.id.text2);
			String value = hot_daikuan_detail_value.get(i);
			String name = detail_item_name[i];
			text1.setText(name + "");
			text2.setText(value + "");
			product_detail_group.addView(item_view);
		}
	}

	private int type,amount,term,monthAmount;
	private double totalFee;
	private String companyName, name, companyIcon, icon, application,amountText,termText,
	dayText,repaymentTypeName,feeText,conditions,material,rates,content,ratesText;
	public void bindViewData(JSONObject productHot) {
		type = productHot.optInt("type");
		companyIcon = productHot.optString("companyIcon");
		icon = productHot.optString("icon");
		companyName = productHot.optString("companyName");
		name = productHot.optString("name");
		
		application = productHot.optString("application");
		amount = productHot.optInt("amount");
		amountText = productHot.optString("amountText");
		monthAmount = productHot.optInt("monthAmount");
		term = productHot.optInt("term");
		termText = productHot.optString("termText");
		totalFee = productHot.optDouble("totalFee");
		dayText = productHot.optString("dayText");
		feeText = productHot.optString("feeText");
		conditions = productHot.optString("conditions");
		material = productHot.optString("material");
		content = productHot.optString("content");
		repaymentTypeName = productHot.optString("repaymentTypeName");
		rates = productHot.optString("rates");
		ratesText = productHot.optString("ratesText");
		
		hot_daikuan_detail_value.add(amountText);
		hot_daikuan_detail_value.add(termText);
		hot_daikuan_detail_value.add(dayText);
		hot_daikuan_detail_value.add(repaymentTypeName);
		hot_daikuan_detail_value.add(feeText);

		name_text.setText(companyName + "-" + name);
		salary_text.setText(amount+"万元");
		month_huankuan_text.setText(monthAmount + "元");
		total_rate_text.setText(totalFee+"万");
		if (2 == type) {
			// 现金分期详情
			showCashTermDetail();
		} else {
			// 贷款详情
			showDaikuanDetail();
		}
		if (StringUtil.checkStr(conditions)) {
			apply_text.setText(conditions);
		}
		add_product_detail();
	}

	/*
	 * 显示现金分期详情数据
	 */
	private void showCashTermDetail(){// 270*180
		detail_item_name = mActivity.getResources().getStringArray(R.array.cash_term_detail_name);
		int h = ScreenUtil.dip2px(mActivity, 60.0f);
		int w = 270 * h / 180;
		int margin = ScreenUtil.dip2px(mActivity, 10.0f);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(w,h);
		param.topMargin = margin;
		param.bottomMargin = margin;
		param.leftMargin = margin;
		param.rightMargin = margin;
		//param.gravity = Gravity.CENTER_VERTICAL;
		ico_img.setLayoutParams(param);
		quick_apply_text.setText(apply_progress);
		text22.setText("分期期数");
		text44.setText("总费用");
		name_text1.setText("申请条件");
		name_text2.setText("产品介绍");
		name_text4.setText("产品特色");
		name_text3.setText("费用说明");
		mImgLoad.loadImg(ico_img, icon, R.drawable.default_xianjinfenqi_img);
		hot_daikuan_detail_value.add(amountText);
		hot_daikuan_detail_value.add(termText);
		hot_daikuan_detail_value.add(dayText);
		hot_daikuan_detail_value.add(repaymentTypeName);
		hot_daikuan_detail_value.add(feeText);
		daikuan_term_text.setText(term+"期");
		product_tese_ll.setVisibility(View.GONE);
		if(StringUtil.checkStr(content)){//产品特色 content
			product_tese_ll.setVisibility(View.VISIBLE);
			product_tese_text.setText(content+"");
		}
		need_text.setText(""+material);//产品介绍 material
		//rate_text.setText(""+rates);//费用说明 rates
		if(StringUtil.checkStr(rates)){
			show_rates_table(rates);		
		}
	}
	
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;    
    private final int FP = ViewGroup.LayoutParams.MATCH_PARENT; 
	private void show_rates_table(String rates) {
		LinearLayout rates_group = (LinearLayout) findViewById(R.id.rates_group);
		String[] row_data = StringUtil.split(rates, "\r\n");
		if(null==row_data||row_data.length<=0)
			return;
		rates_group.removeAllViews();
		int screenW = ScreenUtil.getWidth(mActivity);
		int dash = ScreenUtil.dip2px(mActivity, 20.0f)*2;
		for(int i=0;i<row_data.length;i++){
			String[] col_data = StringUtil.split(row_data[i], "|");
			if(null!=col_data&&col_data.length>0){
				View row_linearlayout = mInflater.inflate(R.layout.row_linearlayout, null);
				LinearLayout row_ll = (LinearLayout) row_linearlayout.findViewById(R.id.row_ll);
				//row_ll.removeAllViews();
				for(int j=0;j<col_data.length;j++){
					String content = col_data[j]+"";
					int showW = (screenW-dash)/col_data.length;
					//int showh
					View view = mInflater.inflate(R.layout.table_textview_item, null);
					LinearLayout item_ll = (LinearLayout) view.findViewById(R.id.item_ll);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(showW,LayoutParams.WRAP_CONTENT);
					//item_ll.setLayoutParams(params);
					View line1 = view.findViewById(R.id.line1);
					View line2 = view.findViewById(R.id.line2);
					line1.setVisibility(View.VISIBLE);
					line2.setVisibility(View.VISIBLE);
					if(j==col_data.length-1){
						line1.setVisibility(View.GONE);
					}
					if(i==row_data.length-1){
						line2.setVisibility(View.GONE);
					}
					TextView textview = (TextView) view.findViewById(R.id.textview);
					textview.setLayoutParams(params);
					textview.setText(content);
					row_ll.addView(view);
				}
				rates_group.addView(row_linearlayout);
			}
		}
		//rates_group.setBackgroundResource(R.drawable.white_fangkuai);//fangkuai_grayline_whitebg
	}

	/*
	 * 显示贷款产品详情数据
	 */
	private void showDaikuanDetail(){
		mImgLoad.loadImg(ico_img, icon, R.drawable.default_bank_img);
		product_tese_ll.setVisibility(View.GONE);
		need_text.setText(material+"");
		rates_ll.setVisibility(View.GONE);
		if(StringUtil.checkStr(ratesText)){
			rates_ll.setVisibility(View.VISIBLE);
			rate_text.setText(Html.fromHtml(ratesText));
		}
		daikuan_term_text.setText(term+"个月");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.quick_apply_text:
			if (2 == type) {
				apply_progress();
			} else {
				quick_apply();
			}
			break;
		default:
			break;
		}
	}

	/*
	 * 办理流程
	 */
	private void apply_progress() {
		DialogUtil.showAlertDialog(mActivity, "办理流程", application);
	}

	/*
	 * 立即申请
	 */
	private void quick_apply() {
		Bundle bundle = new Bundle();
		bundle.putInt(ParamsKey.id, id);
		bundle.putString(ParamsKey.name, companyName + "-" + name);
		bundle.putString(ParamsKey.img_url, icon);
		if (StringUtil.checkStr(UserData.userPhone)
				&& StringUtil.checkStr(UserData.userId)) {
			IntentUtil.activityForward(mActivity, ApplyDkActivity.class,
					bundle, false);
		} else {
			IntentUtil.activityForward(mActivity, LoginActivity.class, bundle,
					false);
		}

	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.daikuan_product_detail;
	}
}
