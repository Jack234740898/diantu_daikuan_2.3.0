package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.constants.ActionString;
import com.constants.ParamsKey;
import com.db.UserData;
import com.net.service.UserJsonService;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.MyAsyncTask;
import com.ui.view.PopuApplyLoan;
import com.utils.EdittextUtil;
import com.utils.IntentUtil;
import com.utils.KeyboardUtil;
import com.utils.LogUtil;
import com.utils.StringUtil;
import com.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * 我的资料页面
 */
public class MyInfoActivity extends BaseActivity implements OnClickListener,PopuApplyLoan.ResultListener{

	private static final String TAG = "MyInfoActivity";
	private UserJsonService mUserJsonService;
	private JSONObject option_list;
	private boolean isEdit;
	private static final String modify_str="修改";
	private static final String save_str="保存";
	private int userType;
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		isEdit = true;
		mUserJsonService = new UserJsonService(mActivity);
		initView();
		setEditTextEdit();
		loadData();
	}

	private View body;
	private EditText name_edit;
	private TextView city_text,age_text,sex_text,society_security_text,credit_record_text,
	debet_text,diya_text,rightText;
	private void initView() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("我的资料");
		rightText = (TextView) findViewById(R.id.rightText);
		rightText.setText(save_str);
		rightText.setOnClickListener(this);
		body = findViewById(R.id.body);
		name_edit = (EditText) findViewById(R.id.name_edit);
		city_text = (TextView) findViewById(R.id.city_text);
		age_text = (TextView) findViewById(R.id.age_text);
		sex_text = (TextView) findViewById(R.id.sex_text);
		society_security_text = (TextView) findViewById(R.id.society_security_text);
		credit_record_text = (TextView) findViewById(R.id.credit_record_text);
		debet_text = (TextView) findViewById(R.id.debet_text);
		diya_text = (TextView) findViewById(R.id.diya_text);
		if(StringUtil.checkStr(UserData.local_city)){
			city_text.setText(""+UserData.local_city);
			city=UserData.local_city;
			setTextColor(city_text);
		}
		findViewById(R.id.city_rl).setOnClickListener(this);
		findViewById(R.id.age_rl).setOnClickListener(this);
		findViewById(R.id.sex_rl).setOnClickListener(this);
		findViewById(R.id.society_security_rl).setOnClickListener(this);
		findViewById(R.id.credit_record_text).setOnClickListener(this);
		findViewById(R.id.debet_rl).setOnClickListener(this);
		findViewById(R.id.diya_rl).setOnClickListener(this);

		
	}
	
	private JSONArray daikuan_type_array, age_array, sex_array,
	socialSecurityAge_array, creditRecord_array, debtType_array,
	guaranteeType_array, enterpriseAnnualWater_array, wagesPunch_array;
	@Override
	protected void loadData() {
		super.loadData();
		new AsyQuery(mActivity,"").execute();
	}
	
	private class AsyQuery extends MyAsyncTask{

		protected AsyQuery(Context context,String title) {
			super(mActivity,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			option_list = mUserJsonService.option_list(1);
			return mUserJsonService.applyLoan_getUserLoanInfo();
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if(null==option_list)
				return;
			JSONObject options = (JSONObject) result;

			JSONObject income1 = options.optJSONObject("income1");
			if (null != income1)
				wagesPunch_array = income1.optJSONArray("list");

			JSONObject income2 = options.optJSONObject("income2");
			if (null != income2)
				enterpriseAnnualWater_array = income2.optJSONArray("list");

			JSONObject socialSecurityAge = options
					.optJSONObject("socialSecurityAge");
			if (null != socialSecurityAge)
				socialSecurityAge_array = socialSecurityAge
						.optJSONArray("list");

			JSONObject creditRecord = options.optJSONObject("creditRecord");
			if (null != creditRecord)
				creditRecord_array = creditRecord.optJSONArray("list");

			JSONObject useType = options.optJSONObject("useType");
			if (null != useType)
				daikuan_type_array = useType.optJSONArray("list");

			JSONObject debtType = options.optJSONObject("debtType");
			if (null != debtType)
				debtType_array = debtType.optJSONArray("list");

			JSONObject guaranteeType = options.optJSONObject("guaranteeType");
			if (null != guaranteeType)
				guaranteeType_array = guaranteeType.optJSONArray("list");

			JSONObject age = options.optJSONObject("age");
			if (null != age)
				age_array = age.optJSONArray("list");

			JSONObject sex = options.optJSONObject("sex");
			if (null != sex)
				sex_array = sex.optJSONArray("list");
			if(null==result)
				return;
			JSONObject user = (JSONObject) result;
			bindviewData(user);
		}
	}
	@Override
	public void onClick(View v) {
		KeyboardUtil.hideBoard(mActivity, (ViewGroup)body);
		switch (v.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.rightText:
			if(modify_str.equals(rightText.getText().toString())){
				isEdit=true;
				rightText.setText(save_str);
				setEditTextEdit();
			}else{
				commit();
			}
			break;
		case R.id.jiekuan_type_rl:
			// 贷款类型
			PopuApplyLoan(daikuan_type_array, daikuan_type);
			break;
		case R.id.city_rl:
			// 选择城市
			// 选择城市
			Bundle bundle = new Bundle();
			bundle.putString(ParamsKey.cur_city_name,
					city = UserData.local_city);
			IntentUtil.activityForward(mActivity, ChooseCityActivity.class,
					bundle, false);
			break;
		case R.id.age_rl:
			// 选择年龄
			PopuApplyLoan(age_array, age_type);
			break;
		case R.id.sex_rl:
			// 选择性别
			PopuApplyLoan(sex_array, sex_type);
			break;
		case R.id.income_rl:
			// 选择月收入
			if (userType == ParamsKey.company_daikuan_type) {
				PopuApplyLoan(enterpriseAnnualWater_array, income_type);
			} else {
				PopuApplyLoan(wagesPunch_array, income_type);
			}
			break;
		case R.id.society_security_rl:
			// 社保
			PopuApplyLoan(socialSecurityAge_array, socialSecurityAge_type);
			break;
		case R.id.credit_record_rl:
			// 信用记录
			PopuApplyLoan(creditRecord_array, credit_record_type);
			break;
		case R.id.debet_rl:
			// 负债
			PopuApplyLoan(debtType_array, debet_type);
			break;
		case R.id.diya_rl:
			// 抵押
			PopuApplyLoan(guaranteeType_array, diya_type);
			break;
		default:
			break;
		}
	}
	
	private void commit() {
		String work_city_str = city_text.getText().toString();
		if(!StringUtil.checkStr(work_city_str)){
			ToastUtil.showToast(mActivity, 0, "请选择城市~", true);
			return;
		}
		city = work_city_str;
		
		String user_name = name_edit.getText().toString();
		if(!StringUtil.checkStr(user_name)){
			ToastUtil.showToast(mActivity, 0, "请输入您的姓名~", true);
			return;
		}
		userName = user_name;
		
		if (!StringUtil.isIntNum(age)) {
			ToastUtil.showToast(mActivity, 0, "请选择您的年龄", true);
			return;
		}

		if (!StringUtil.isIntNum(sex)) {
			ToastUtil.showToast(mActivity, 0, "请选择您的性别", true);
			return;
		}
		
		if (!StringUtil.isIntNum(socialSecurityAge)) {
			ToastUtil.showToast(mActivity, 0, "请选择您的社保", true);
			return;
		}
		if (!StringUtil.isIntNum(creditRecord)) {
			ToastUtil.showToast(mActivity, 0, "请选择您的信用记录", true);
			return;
		}
		if (!StringUtil.isIntNum(debtType)) {
			ToastUtil.showToast(mActivity, 0, "请选择您的负债情况", true);
			return;
		}
		if (!StringUtil.isIntNum(guaranteeType)) {
			ToastUtil.showToast(mActivity, 0, "请选择您的抵押情况", true);
			return;
		}
		new AsyCommit(mActivity,"").execute();
	}

	private class AsyCommit extends MyAsyncTask{

		protected AsyCommit(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			return mUserJsonService.applyLoan_updateUserLoanInfo(id, city, compnanyName, "", income, "", socialSecurityAge, "", creditRecord, "", "", userName);
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if(null==result)
				return;
			JSONObject data = (JSONObject) result;
			int code = data.optInt("code");
			//id = data.optInt("id");
			String msg = "提交失败";
			if(200==code){
				msg = "提交成功";
				rightText.setText(modify_str);
				isEdit=false;
				setEditTextEdit();
			}
			ToastUtil.showToast(mActivity, 0, msg, true);
		}
	}
	private String userName, amount, term, city, compnanyName, income,
	socialSecurityAge, creditRecord, useType, debtType, guaranteeType,
	age, sex, mobile, verifyCode;
	private int id;
	public void bindviewData(JSONObject user) {
		id = user.optInt("id");
		if(id>0){
			isEdit = false;
			rightText.setText(modify_str);
			setEditTextEdit();
		}
			
		income = user.optString("income");
		socialSecurityAge = user.optString("socialSecurityAge");
		creditRecord = user.optString("creditRecord");
		city = user.optString("city");
		compnanyName = user.optString("companyName");
		userName = user.optString("userName");
		String houseText = user.optString("houseText");
		
		if(StringUtil.checkStr(userName)){
			name_edit.setText(userName);
		}
		if(StringUtil.checkStr(city)){
		    city_text.setText(city);
			setTextColor(city_text);
		}
		/*if(StringUtil.checkStr(companyName)){
			company_name_edit.setText(companyName);
		}*/
		
		String socialSecurityAgeText = user.optString("socialSecurityAgeText");
		if(StringUtil.checkStr(socialSecurityAgeText)){
			society_security_text.setText(socialSecurityAgeText);
			setTextColor(society_security_text);
		}
		String creditRecordText = user.optString("creditRecordText");
		if(StringUtil.checkStr(creditRecordText)){
			credit_record_text.setText(creditRecordText);
			setTextColor(credit_record_text);
		}
	}

	private static final int daikuan_type = 3;
	private static final int sex_type = 4;
	private static final int income_type = 5;
	private static final int socialSecurityAge_type = 6;
	private static final int credit_record_type = 7;
	private static final int debet_type = 8;
	private static final int diya_type = 9;
	private static final int age_type = 10;
	
	private void setEditTextEdit(){
		EdittextUtil.setEditTextEditable(name_edit, isEdit);
		
	}
	
	private PopuApplyLoan mPopuApplyLoan;
	private void PopuApplyLoan(JSONArray array, int type) {
		LogUtil.d(TAG, "PopuApplyLoan==type is " + type + ",array is " + array);
		if (null == mPopuApplyLoan)
			mPopuApplyLoan = new PopuApplyLoan(mActivity);
		mPopuApplyLoan.setData(array, type);
		mPopuApplyLoan.setResultListener(this);
		mPopuApplyLoan.showPopupWindow(body);
	}
	
	private void setTextColor(TextView textview){
		textview.setTextColor(mActivity.getResources().getColor(R.color.apply_loan_value_color));
	}
	
	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent data) {
		super.onActivityResult(requestcode, resultcode, data);
		if(ActionString.choose_city_resultcode==resultcode){
			city = data.getStringExtra(ParamsKey.cur_city_name);
			if(StringUtil.checkStr(city)){
				city_text.setText(city);
				setTextColor(city_text);
			}
		}
	}

	@Override
	public void onResult(int type, String name, String value) {
		if (type == daikuan_type) {
			//jiekuan_type_text.setText(name + "");
			//if (StringUtil.isIntNum(value))
				//userType = Integer.parseInt(value);
			//setTextColor(jiekuan_type_text);
			//showDaikuanType();
		} else if (type == sex_type) {
			sex_text.setText(name + "");
			sex = value;
			setTextColor(sex_text);
		} else if (type == socialSecurityAge_type) {
			society_security_text.setText(name + "");
			socialSecurityAge = value;
			setTextColor(society_security_text);
		} else if (type == credit_record_type) {
			credit_record_text.setText(name + "");
			creditRecord = value;
			setTextColor(credit_record_text);
		} else if (type == debet_type) {
			debet_text.setText(name + "");
			debtType = value;
			setTextColor(debet_text);
		} else if (type == diya_type) {
			diya_text.setText(name + "");
			guaranteeType = value;
			setTextColor(diya_text);
		}else if (type == income_type) {
			//income_text.setText(name + "");
			income = value;
			//setTextColor(income_text);
		} else if (type == age_type) {
			age_text.setText(name + "");
			age = value;
			setTextColor(age_text);
		}
	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.my_info;
	}
}
