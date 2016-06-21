package com.sxjs.diantu_daikuan.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.constants.DKUserType;
import com.constants.GlobalFlag;
import com.constants.ParamsKey;
import com.db.UserData;
import com.db.service.UserDataService;
import com.example.doubledatepicker.ChooseDateTime;
import com.net.service.UserJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.service.IMLoginService;
import com.ui.view.MyAsyncTask;
import com.ui.view.PopuApplyLoan;
import com.utils.DialogUtil;
import com.utils.IntentUtil;
import com.utils.JSONUtil;
import com.utils.KeyboardUtil;
import com.utils.LogUtil;
import com.utils.StringUtil;
import com.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * 申请贷款
 */
public class ApplyDkActivity extends BaseActivity implements OnClickListener,
		PopuApplyLoan.ResultListener,ChooseDateTime.DateTimeListener{

	private static final String TAG = "ApplyDkActivity";
	private static final int daikuan_type = 3;
	private static final int sex_type = 4;
	private static final int income_type = 5;
	private static final int socialSecurityAge_type = 6;
	private static final int credit_record_type = 7;
	private static final int debet_type = 8;
	private static final int diya_type = 9;
	private static final int age_type = 10;
	private static final int public_phone_type = 11;
	private static final int income_cash_type = 12;
	private static final int work_type = 13;// 工作类型
	private static final int work_term_type = 14;// 工作期限类型
	private static final int housing_found_year_type = 15;
	private static final int has_credit_card_type = 16;
	private static final int businessLicense_type = 17;
	private static final int operatePeriod_type = 18;

	private static final int guaranteeWill_type = 19;
	private static final int eductional_systme_type = 20;
	private static final int grade_type = 21;
	
	private UserJsonService mUserJsonService;
	private int userType = 0;

	

	@Override
	protected int setContentView() {
		return R.layout.apply_dk;
	}
	
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mUserJsonService = new UserJsonService(mActivity);
		initParams();
		if(workingIdentity<=0)
			workingIdentity = DKUserType.shangbanzhu_type;
		initStatistics();
		initView();
		/*if(StringUtil.checkStr(UserData.local_city)){
			city = UserData.local_city;
			househodeCity = UserData.local_city;
			city_text.setText(city);
			huji_city_text.setText(househodeCity);
			setTextColor(city_text,true);
			setTextColor(huji_city_text,true);
		}*/
		showFieldByWorkType();
		loadData();
	}

	private int id, guaranteeWill, workingIdentity,loanCategory;//
	private String workingIdentityName,loanCategoryName;
	private int specialStatus = -1;
	private void initParams() {
		loanCategoryName = GlobalFlag.loanCategoryName;

		GlobalFlag.loanCategoryName = null;
		Bundle bundle = getIntent().getExtras();
		if (null == bundle)
			return;
		userType = bundle.getInt(ParamsKey.userType);
		id = bundle.getInt(ParamsKey.pd_id);
		guaranteeWill = bundle.getInt(ParamsKey.guaranteeWill);
		workingIdentity = bundle.getInt(ParamsKey.workingIdentity);			
		loanCategory = bundle.getInt(ParamsKey.loanCategory);
		workingIdentityName = bundle.getString(ParamsKey.workingIdentityName);
	}

	private EditText daikuan_salary_edit, name_edit, company_name_edit,
			phone_edit, verify_code_edit, message_edit,
			housing_found_salary_edit,dk_used_edit;
	private TextView title_text, desc_text, jiekuan_type_text, city_text,
			huji_city_text, sex_text, society_security_text,
			credit_record_text, debet_text, diya_text, verify_text,
			jiekuan_term_text, income_text, age_text, income_name_text,
			public_phone_text, income_cash_text, work_type_text,
			company_name_text, work_term_name_text, work_term_text,
			housing_found_year_text,has_credit_text,business_license_text,choose_diya_text
			,grade_text,eductional_systme_text,school_name_text;
	private View body, society_security_ll, verify_phone_code_ll,
			income_cash_ll, company_name_LL, work_term_ll, income_ll,school_name_rl,
			housing_found_year_ll, housing_found_salary_ll,guarantee_ll,debet_ll,business_license_LL,diya_ll
			,eductional_systme_ll,grade_ll,credit_record_ll;
	private CheckBox agreement_checkbox;
	private void initView() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("申请贷款");
		mHeadView.hideRightBtn();
		body = findViewById(R.id.body);
		body.setVisibility(View.INVISIBLE);
		title_text = (TextView) findViewById(R.id.title_text);
		desc_text = (TextView) findViewById(R.id.desc_text);

		school_name_rl = findViewById(R.id.school_name_rl);
		school_name_rl.setOnClickListener(this);
		school_name_rl.setVisibility(View.GONE);
		verify_phone_code_ll = findViewById(R.id.verify_phone_code_ll);
		income_name_text = (TextView) findViewById(R.id.income_name_text);
		daikuan_salary_edit = (EditText) findViewById(R.id.daikuan_salary_edit);
		jiekuan_term_text = (TextView) findViewById(R.id.jiekuan_term_text);
		name_edit = (EditText) findViewById(R.id.name_edit);
		age_text = (TextView) findViewById(R.id.age_text);
		society_security_ll = findViewById(R.id.society_security_ll);
		company_name_edit = (EditText) findViewById(R.id.company_name_edit);
		phone_edit = (EditText) findViewById(R.id.phone_edit);
		verify_code_edit = (EditText) findViewById(R.id.verify_code_edit);
		jiekuan_type_text = (TextView) findViewById(R.id.jiekuan_type_text);
		city_text = (TextView) findViewById(R.id.city_text);
		huji_city_text = (TextView) findViewById(R.id.huji_city_text);
		sex_text = (TextView) findViewById(R.id.sex_text);
		income_text = (TextView) findViewById(R.id.income_text);
		society_security_text = (TextView) findViewById(R.id.society_security_text);
		credit_record_text = (TextView) findViewById(R.id.credit_record_text);
		debet_text = (TextView) findViewById(R.id.debet_text);
		diya_text = (TextView) findViewById(R.id.diya_text);
		verify_text = (TextView) findViewById(R.id.verify_text);
		verify_text.setOnClickListener(this);
		work_type_text = (TextView) findViewById(R.id.work_type_text);
		company_name_text = (TextView) findViewById(R.id.company_name_text);
		work_term_name_text = (TextView) findViewById(R.id.work_term_name_text);
		work_term_text = (TextView) findViewById(R.id.work_term_text);
		findViewById(R.id.work_type_rl).setOnClickListener(this);
		findViewById(R.id.jiekuan_type_rl).setOnClickListener(this);
		findViewById(R.id.city_rl).setOnClickListener(this);
		findViewById(R.id.huji_city_rl).setOnClickListener(this);
		company_name_LL = findViewById(R.id.company_name_LL);
		work_term_ll = findViewById(R.id.work_term_ll);
		work_term_ll.setOnClickListener(this);
		findViewById(R.id.age_rl).setOnClickListener(this);
		findViewById(R.id.sex_rl).setOnClickListener(this);
		findViewById(R.id.income_rl).setOnClickListener(this);
		findViewById(R.id.society_security_rl).setOnClickListener(this);
		findViewById(R.id.credit_record_rl).setOnClickListener(this);
		findViewById(R.id.debet_rl).setOnClickListener(this);
		findViewById(R.id.diya_rl).setOnClickListener(this);
		findViewById(R.id.commit_apply_text).setOnClickListener(this);
		findViewById(R.id.public_phone_rl).setOnClickListener(this);
		has_credit_text = (TextView) findViewById(R.id.has_credit_text);
		findViewById(R.id.has_record_ll).setOnClickListener(this);
		public_phone_text = (TextView) findViewById(R.id.public_phone_text);
		income_ll = findViewById(R.id.income_ll);
		income_cash_ll = findViewById(R.id.income_cash_ll);
		guarantee_ll = findViewById(R.id.guarantee_ll);
		debet_ll = findViewById(R.id.debet_ll);
		business_license_LL = findViewById(R.id.business_license_LL);
		business_license_text = (TextView) findViewById(R.id.business_license_text);
		business_license_LL.setOnClickListener(this);
		// ,
		housing_found_year_ll = findViewById(R.id.housing_found_year_ll);
		housing_found_year_ll.setOnClickListener(this);
		housing_found_salary_ll = findViewById(R.id.housing_found_salary_ll);
		housing_found_year_text = (TextView) findViewById(R.id.housing_found_year_text);
		housing_found_salary_edit = (EditText) findViewById(R.id.housing_found_salary_edit);
		findViewById(R.id.income_cash_rl).setOnClickListener(this);
		income_cash_text = (TextView) findViewById(R.id.income_cash_text);
		message_edit = (EditText) findViewById(R.id.message_edit);
		work_type_text.setText("上班族");
		if(StringUtil.checkStr(workingIdentityName)){
			work_type_text.setText(workingIdentityName+"");
		}
		setTextColor(work_type_text,true);
		diya_ll = findViewById(R.id.diya_ll);
		choose_diya_text = (TextView) findViewById(R.id.choose_diya_text);
		diya_ll.setOnClickListener(this);
		
		findViewById(R.id.agreement_ll).setOnClickListener(this);
		agreement_checkbox = (CheckBox) findViewById(R.id.agreement_checkbox);
		agreement_checkbox.setChecked(true);
		
		eductional_systme_ll = findViewById(R.id.eductional_systme_ll);
		eductional_systme_ll.setOnClickListener(this);
		grade_ll = findViewById(R.id.grade_ll);
		grade_ll.setOnClickListener(this);
		grade_text = (TextView) findViewById(R.id.grade_text);
		eductional_systme_text = (TextView) findViewById(R.id.eductional_systme_text);
		
		dk_used_edit = (EditText) findViewById(R.id.dk_used_edit);
		if(StringUtil.checkStr(loanCategoryName)){
			dk_used_edit.setText(loanCategoryName);
		}
		
		school_name_text = (TextView) findViewById(R.id.school_name_text);
		
		credit_record_ll = findViewById(R.id.credit_record_ll);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (StringUtil.checkStr(GlobalFlag.work_city)) {
			city = GlobalFlag.work_city;
			city_text.setText(city);
			GlobalFlag.work_city = null;
			setTextColor(city_text,true);
		}

		if (StringUtil.checkStr(GlobalFlag.huji_city)) {
			househodeCity = GlobalFlag.huji_city;
			huji_city_text.setText(househodeCity);
			GlobalFlag.huji_city = null;
			setTextColor(huji_city_text,true);
		}
		verify_phone_code_ll.setVisibility(View.VISIBLE);
		if (StringUtil.checkStr(UserData.userId)) {
			verify_phone_code_ll.setVisibility(View.GONE);
		}
		
		if(StringUtil.checkStr(UserData.universityName)){
			universityName = UserData.universityName;
			companyName = universityName;
			school_name_text.setText(""+universityName);
			setTextColor(school_name_text,true);
			UserData.universityName = null;
		}
		if(GlobalFlag.specialStatus>=0){
			specialStatus = 0;
			GlobalFlag.specialStatus = -1;
		}
	}

	@Override
	protected void loadData() {
		super.loadData();
		new Asyoption_list(mActivity,"").execute();
	}

	private JSONArray daikuan_type_array, age_array, sex_array,
			socialSecurityAge_array, creditRecord_array, debtType_array,
			guaranteeType_array, enterpriseAnnualWater_array, wagesPunch_array,
			workingIdentity_array, workingAge_array,housingFundAge_array,guaranteeWill_array,
			hasCreditCard_array,operatePeriod_array,businessLicense_array,lengthOfSchool_array,grade_array;
	private String agreementLink;
	private class Asyoption_list extends MyAsyncTask {

		protected Asyoption_list(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			mUserJsonService.setNeedCach(false);
			return mUserJsonService.option_list(1);
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			body.setVisibility(View.VISIBLE);
			if (null == result)
				return;
			JSONObject data = (JSONObject) result;
			agreementLink = data.optString("agreementLink");
			LogUtil.d(TAG, "onPostExecute==agreementLink is "+agreementLink);
			JSONObject options = data.optJSONObject("options");
			if(null==options)
				return;
			String agreementLink11 = options.optString("agreementLink");
			LogUtil.d(TAG, "onPostExecute==agreementLink11 is "+agreementLink11);
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

			JSONObject creditRecord1 = options.optJSONObject("creditRecord1");
			if (null != creditRecord1)
				creditRecord_array = creditRecord1.optJSONArray("list");

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
			// 工作身份
			JSONObject workingIdentity = options
					.optJSONObject("workingIdentity");
			if (null != workingIdentity)
				workingIdentity_array = workingIdentity.optJSONArray("list");
			// 工作期限
			JSONObject workingAge = options.optJSONObject("workingAge");
			if (null != workingAge)
				workingAge_array = workingAge.optJSONArray("list");
			//公积金年限  housingFundAge_array
			JSONObject housingFundAge = options.optJSONObject("housingFundAge");
			if (null != housingFundAge)
				housingFundAge_array = housingFundAge.optJSONArray("list");
			//,
			//,
			//企业经营年限
			JSONObject operatePeriod = options.optJSONObject("operatePeriod");
			if (null != operatePeriod)
				operatePeriod_array = operatePeriod.optJSONArray("list");
			//是否抵押
			guaranteeWill_array = new JSONArray();
			String[] name ={"不抵押","可抵押",};
			String[] value ={"-1","1"};
			guaranteeWill_array = JSONUtil.getStringArrayToJSONArray(name, value);
			String[] name2 ={"有信用卡","无信用卡"};
			String[] value2 ={"1","0"};
			hasCreditCard_array = JSONUtil.getStringArrayToJSONArray(name2, value2);
			String[] name3 ={"有营业执照","无营业执照"};
			String[] value3 ={"1","0"};
			businessLicense_array = JSONUtil.getStringArrayToJSONArray(name3, value3);
			//学制lengthOfSchool，grade
			JSONObject lengthOfSchool = options.optJSONObject("lengthOfSchool");
			if (null != lengthOfSchool)
				lengthOfSchool_array = lengthOfSchool.optJSONArray("list");
			//年级
			JSONObject grade = options.optJSONObject("grade");
			if (null != grade)
				grade_array = grade.optJSONArray("list");
		}
	}

	/*
	 * 根据工作类型显示相应的表单字段
	 */
	private void showFieldByWorkType() {
		society_security_ll.setVisibility(View.VISIBLE);
		income_cash_ll.setVisibility(View.VISIBLE);
		income_ll.setVisibility(View.VISIBLE);
		work_term_ll.setVisibility(View.VISIBLE);
		company_name_LL.setVisibility(View.VISIBLE);
		housing_found_salary_ll.setVisibility(View.VISIBLE);
		housing_found_year_ll.setVisibility(View.VISIBLE);
		guarantee_ll.setVisibility(View.VISIBLE);
		diya_ll.setVisibility(View.VISIBLE);
		debet_ll.setVisibility(View.VISIBLE);
		business_license_LL.setVisibility(View.GONE);
		company_name_text.setText("工作单位");
		work_term_name_text.setText("工作年限");
		income_name_text.setText("月打卡工资(元)");
		company_name_edit.setHint("请输入工作单位");
		company_name_edit.setVisibility(View.VISIBLE);
		school_name_rl.setVisibility(View.GONE);
		grade_ll.setVisibility(View.GONE);
		eductional_systme_ll.setVisibility(View.GONE);
		credit_record_ll.setVisibility(View.VISIBLE);
		if (DKUserType.qiyezhu_type == workingIdentity || DKUserType.getihu_type == workingIdentity
				|| DKUserType.student_type == workingIdentity
				|| DKUserType.otherwork_type == workingIdentity) {
			// 经营年限
			income_name_text.setText("年流水(万元)");
			society_security_ll.setVisibility(View.GONE);
			income_cash_ll.setVisibility(View.GONE);
			housing_found_year_ll.setVisibility(View.GONE);
			housing_found_salary_ll.setVisibility(View.GONE);
			company_name_text.setText("企业全称");
			work_term_name_text.setText("经营年限");
			if (DKUserType.getihu_type == workingIdentity) {
				company_name_LL.setVisibility(View.GONE);
				business_license_LL.setVisibility(View.VISIBLE);
			}
			if (DKUserType.student_type == workingIdentity) {
				grade_ll.setVisibility(View.VISIBLE);
				eductional_systme_ll.setVisibility(View.VISIBLE);
				company_name_text.setText("就读学校");
				work_term_ll.setVisibility(View.GONE);
				income_ll.setVisibility(View.GONE);
				guarantee_ll.setVisibility(View.GONE);
				diya_ll.setVisibility(View.GONE);
				debet_ll.setVisibility(View.GONE);
				company_name_edit.setVisibility(View.GONE);
				school_name_rl.setVisibility(View.VISIBLE);
				credit_record_ll.setVisibility(View.GONE);
			}
			if (DKUserType.otherwork_type == workingIdentity) {
				income_cash_ll.setVisibility(View.VISIBLE);
				income_ll.setVisibility(View.GONE);
				work_term_ll.setVisibility(View.GONE);
				company_name_LL.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		KeyboardUtil.hideBoard(mActivity, (ViewGroup) body);
		daikuan_salary_edit.clearFocus();
		name_edit.clearFocus();
		switch (v.getId()) {
		case R.id.leftImg:
			exit_dialog();
			break;
		case R.id.commit_apply_text:
			commit_apply_loan();
			break;
		case R.id.jiekuan_type_rl:
			// 贷款类型
			PopuApplyLoan(daikuan_type_array, daikuan_type);
			break;
		case R.id.city_rl:
			// 选择工作城市
			choose_city(ParamsKey.work_city);
			break;
		case R.id.huji_city_rl:
			// 选择户籍城市
			choose_city(ParamsKey.huji_city);
			break;
		case R.id.age_rl:
			// 选择年龄
			PopuApplyLoan(age_array, age_type);
			break;
		case R.id.sex_rl:
			// 选择性别
			PopuApplyLoan(sex_array, sex_type);
			break;
		case R.id.work_type_rl:
			// 选择工作类型
			PopuApplyLoan(workingIdentity_array, work_type);
			break;
		case R.id.work_term_ll:
			LogUtil.d(TAG, "work_term_ll==workingIdentity is "+workingIdentity);
			// 选择工作期限
			if(DKUserType.student_type == workingIdentity){
				startYearTime();
			}else{
				if(DKUserType.qiyezhu_type==workingIdentity||DKUserType.getihu_type==workingIdentity){
					PopuApplyLoan(operatePeriod_array, operatePeriod_type);
				}else if(DKUserType.shangbanzhu_type==workingIdentity||DKUserType.gongwuyuan_type==workingIdentity){
					PopuApplyLoan(workingAge_array, work_term_type);
				}
				
			}
			break;
		case R.id.income_rl:
			// 选择月收入
			if (DKUserType.qiyezhu_type == workingIdentity
					|| DKUserType.getihu_type == workingIdentity) {
				PopuApplyLoan(enterpriseAnnualWater_array, income_type);
			} else {
				PopuApplyLoan(wagesPunch_array, income_type);
			}
			break;
		case R.id.income_cash_rl:
			// 选择月收入现金工资
			PopuApplyLoan(wagesPunch_array, income_cash_type);
			break;
		case R.id.society_security_rl:
			// 社保
			PopuApplyLoan(socialSecurityAge_array, socialSecurityAge_type);
			break;
		case R.id.credit_record_rl:
			// 信用记录
			/*if(student_type==workingIdentity){
				PopuApplyLoan(hasCreditCard_array, has_credit_card_type);
			}else{
				PopuApplyLoan(creditRecord_array, credit_record_type);
			}*/
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
		case R.id.verify_text:
			sendMsgCode();
			break;
		case R.id.public_phone_rl:
			choose_public_phone();
			break;
		case R.id.housing_found_year_ll:
			// 选择公积金年限
			PopuApplyLoan(housingFundAge_array, housing_found_year_type);
			break;
		case R.id.has_record_ll:
			PopuApplyLoan(hasCreditCard_array, has_credit_card_type);
			break;
		case R.id.business_license_LL:
			PopuApplyLoan(businessLicense_array, businessLicense_type);
			break;
			
		case R.id.diya_ll:
			//选择是否抵押
			PopuApplyLoan(guaranteeWill_array, guaranteeWill_type);
			break;
		case R.id.agreement_ll:
			//电兔贷款用户服务协议
			Bundle bundle = new Bundle();
			bundle.putString(ParamsKey.head_name, "电兔贷款用户服务协议");
			bundle.putString(ParamsKey.web_url,agreementLink);
			LogUtil.d(TAG, "R.id.agreement_ll==agreementLink is "+agreementLink);
			IntentUtil.activityForward(mActivity, WebviewActivity.class, bundle, false);
			break;
		case R.id.school_name_rl:
			//选择学校
			IntentUtil.activityForward(mActivity, ProvinceActivity.class, null, false);
			break;
		case R.id.eductional_systme_ll:
			//选择学制
			PopuApplyLoan(lengthOfSchool_array, eductional_systme_type);
			break;
		case R.id.grade_ll:
			//选择年级
			PopuApplyLoan(grade_array, grade_type);
			break;
		default:
			break;
		}
	}

	/*
	 * 入学时间
	 */
	private ChooseDateTime mChooseDateTime;
	private void startYearTime() {
		if(null==mChooseDateTime)
			mChooseDateTime = new ChooseDateTime(mActivity);
		mChooseDateTime.setDateTimeListener(this);
		mChooseDateTime.chooseYearMonth();
	}

	private void choose_city(int city_type) {
		Bundle bundle = new Bundle();
		//bundle.putString(ParamsKey.cur_city_name, UserData.local_city);
		bundle.putInt(ParamsKey.city_type, city_type);
		IntentUtil.activityForward(mActivity, ChooseCityActivity.class, bundle,
				false);
	}

	private void choose_public_phone() {
		String[] label = { "公开,抢单经理能看到", "不公开,消息可能不及时" };
		String[] value = { "1", "0" };
		JSONArray array = new JSONArray();
		for (int i = 0; i < label.length; i++) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("label", label[i]);
				obj.put("value", value[i]);
				array.put(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		PopuApplyLoan(array, public_phone_type);
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

	    //studentGrade,hasCreditCard,operatePeriod,annualIncome
	@Override
	public void onResult(int type, String name, String value) {
		if (type == daikuan_type) {
			jiekuan_type_text.setText(name + "");
			if (StringUtil.isIntNum(value))
				userType = Integer.parseInt(value);
			setTextColor(jiekuan_type_text,true);
		} else if (type == sex_type) {
			sex_text.setText(name + "");
			sex = value;
			setTextColor(sex_text,true);
		} else if (type == socialSecurityAge_type) {
			society_security_text.setText(name + "");
			socialSecurityAge = value;
			setTextColor(society_security_text,true);
			societyBehaviorList.add(name);
		} else if (type == credit_record_type) {
			credit_record_text.setText(name + "");
			creditRecord = value;
			setTextColor(credit_record_text,true);
			creditRecordList.add(name + "");
		} else if (type == debet_type) {
			debet_text.setText(name + "");
			debtType = value;
			setTextColor(debet_text,true);
			debtBehaviorList.add(name);
		} else if (type == diya_type) {
			diya_text.setText(name + "");
			guaranteeType = value;
			setTextColor(diya_text,true);
			guaranteeTypeList.add(name + "");
		} else if (type == income_type) {
			income_text.setText(name + "");
			income = value;
			setTextColor(income_text,true);
			incomeBehaviorList.add(name);
		} else if (type == age_type) {
			age_text.setText(name + "");
			age = value;
			setTextColor(age_text,true);
		} else if (type == public_phone_type) {
			public_phone_text.setText(name + "");
			if (StringUtil.isIntNum(value)) {
				mobileFlag = Integer.parseInt(value);
			}
			setTextColor(public_phone_text,true);
			
		} else if (type == income_cash_type) {
			income_cash_text.setText(name + "");
			incomeCash = value;
			setTextColor(income_cash_text,true);
		} else if (type == work_type) {
			income_text.setText("请选择");
			income = null;
			operatePeriod = null;
			work_term_text.setText("请选择");
			setTextColor(income_text, false);
			setTextColor(work_term_text, false);
			// 工作类型
			if (StringUtil.isIntNum(value)) {
				workingIdentity = Integer.parseInt(value);
				work_type_text.setText(name + "");
				setTextColor(work_type_text,true);
				showFieldByWorkType();
			}
		} else if (type == work_term_type) {
			if (StringUtil.isIntNum(value)) {
				workingAge = Integer.parseInt(value);
				work_term_text.setText(name + "");
				setTextColor(work_term_text,true);
			}
		}else if(operatePeriod_type==type){
			operatePeriod = value;
			work_term_text.setText(name + "");
			setTextColor(work_term_text,true);
		}else if (type == housing_found_year_type) {
			// 公积金年限
			housingFundAge = value;
			housing_found_year_text.setText("" + name);
			setTextColor(housing_found_year_text,true);
		} else if(type == has_credit_card_type){
			//是否有信用卡
			hasCreditCard = value;
			has_credit_text.setText(""+name);
			setTextColor(has_credit_text,true);
		} else if(type == businessLicense_type){
			//是否有营业执照
			businessLicense = value;
			business_license_text.setText(""+name);
			setTextColor(business_license_text,true);
		}else if(type==guaranteeWill_type){
			//是否抵押
			guaranteeWill = Integer.parseInt(value);
			choose_diya_text.setText(""+name);
			setTextColor(choose_diya_text,true);
		}else if(type==eductional_systme_type){
			//学制
			lengthOfSchool = Integer.parseInt(value);
			eductional_systme_text.setText(""+name);
			setTextColor(eductional_systme_text,true);
		}else if(type==grade_type){
			//年级
			grade = Integer.parseInt(value);
			grade_text.setText(""+name);
			setTextColor(grade_text,true);
		}
	}

	/*
	 * 发送短信验证码
	 */
	private void sendMsgCode() {
		mobile = phone_edit.getText().toString();
		if (!StringUtil.isMobileNO(mobile)) {
			ToastUtil.showToast(mActivity, 0, "请输入正确的手机号码哟~", true);
			return;
		}
		new AsySendMsgCode(mActivity,"").execute();
		startCountTime();
	}

	private class AsySendMsgCode extends MyAsyncTask {

		protected AsySendMsgCode(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			return mUserJsonService.userRegister_sendRegCode(mobile);
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			JSONObject dataResult = (JSONObject) result;
			if (null == dataResult)
				return;
			int code = dataResult.optInt("code");
			JSONObject data = dataResult.optJSONObject("data");
			String message = data.optString("message");
			ToastUtil.showToast(mActivity, 0, message + " ", true);
		}
	}

	private MyCount myCount;
	private void startCountTime() {
		myCount = new MyCount(60000, 1000);
		myCount.start();
	}

	private class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			verify_text.setText("获取验证码");
			verify_text.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			LogUtil.d(TAG, "倒计时==millisUntilFinished is " + millisUntilFinished);
			verify_text.setText(millisUntilFinished / 1000 + "秒后可再发");
			verify_text.setClickable(false);
		}
	}

	private void setTextColor(TextView textview,boolean isSelected) {
		if(isSelected){
			textview.setTextColor(mActivity.getResources().getColor(
					R.color.apply_loan_value_color));
		}else{
			textview.setTextColor(mActivity.getResources().getColor(
					R.color.apply_loan_hint_value_color));
		}
		
	}

	// 0未公开 1公开
	private int workingAge = -1;
	//private int studentGrade = -1;
	private int loan_id=-1;;
	private String userName, amount, term, city, househodeCity, companyName,
			income, incomeCash, socialSecurityAge, creditRecord, debtType,
			guaranteeType, age, sex, mobile, verifyCode, riskRaw, message,operatePeriod,startSchoolTime,
			housingFundAge,housingFund,hasCreditCard,businessLicense,universityName,loanUsed;
	private int mobileFlag = 1,lengthOfSchool=-1,grade=-1;
	private void commit_apply_loan() {
		String amountStr = daikuan_salary_edit.getText().toString();
		if (!StringUtil.isIntNum(amountStr)||(Integer.parseInt(amountStr))<500) {
			ToastUtil.showToast(mActivity, 0, "请输入您的贷款金额,500元起", true);
			return;
		}
		amount = amountStr;
		
		String termStr = jiekuan_term_text.getText().toString();
		if (!StringUtil.isIntNum(termStr)) {
			ToastUtil.showToast(mActivity, 0, "请输入您的贷款期限", true);
			return;
		}
		term = termStr;

		if (userType < 0) {
			ToastUtil.showToast(mActivity, 0, "请选择您的贷款类型", true);
			return;
		}
		if (!StringUtil.checkStr(city)) {
			ToastUtil.showToast(mActivity, 0, "请选择您的工作城市", true);
			return;
		}
		if (!StringUtil.checkStr(househodeCity)) {
			ToastUtil.showToast(mActivity, 0, "请选择您的户籍城市", true);
			return;
		}
		String name = name_edit.getText().toString();
		if (!StringUtil.checkStr(name)||StringUtil.isContainsNum(name)) {
			ToastUtil.showToast(mActivity, 0, "请输入您的真实姓名", true);
			return;
		}
		userName = name;

		if (!StringUtil.isIntNum(age)) {
			ToastUtil.showToast(mActivity, 0, "请选择您的年龄", true);
			return;
		}

		if (!StringUtil.isIntNum(sex)) {
			ToastUtil.showToast(mActivity, 0, "请选择您的性别", true);
			return;
		}
		
		
        if(DKUserType.gongwuyuan_type==workingIdentity||DKUserType.shangbanzhu_type==workingIdentity||
        		DKUserType.qiyezhu_type==workingIdentity){
        	String company_name_str = company_name_edit.getText().toString();
    		if (!StringUtil.checkStr(company_name_str)) {
    			String msg = "请输入企业名称";
    			ToastUtil.showToast(mActivity, 0,msg, true);
    			return;
    		}
    		companyName = company_name_str;
		}
        
        if(DKUserType.shangbanzhu_type==workingIdentity||DKUserType.gongwuyuan_type==workingIdentity){
        	if(workingAge<0){
        		ToastUtil.showToast(mActivity, 0, "请选择工作年限", true);
        		return;
        	}
        }else if(DKUserType.qiyezhu_type==workingIdentity||DKUserType.getihu_type==workingIdentity){
        	if(!StringUtil.checkStr(operatePeriod)){
        		ToastUtil.showToast(mActivity, 0, "请选择经营年限", true);
        		return;
        	}
        }else if(DKUserType.student_type==workingIdentity){
        	if(!StringUtil.checkStr(companyName)){
        		ToastUtil.showToast(mActivity, 0, "请选择就读学校", true);
        		return;
        	}
        	if(lengthOfSchool<0){
        		ToastUtil.showToast(mActivity, 0, "请选择学制", true);
        		return;
        	}
        	if(grade<0){
        		ToastUtil.showToast(mActivity, 0, "请选择年级", true);
        		return;
        	}
        }
        
        if(DKUserType.getihu_type == workingIdentity){
        	if(!StringUtil.checkStr(businessLicense)){
        		ToastUtil.showToast(mActivity, 0, "请选择营业执照", true);
            	return;
            }
        }
		if (DKUserType.shangbanzhu_type == workingIdentity
				|| DKUserType.gongwuyuan_type == workingIdentity) {
			// 上班族或公务员
			// 月现金收入
			if (!StringUtil.isIntNum(income)
					|| !StringUtil.isIntNum(incomeCash)) {
				ToastUtil.showToast(mActivity, 0, "请选择您的月打卡工资或月现金收入", true);
				return;
			}
		} else {
			if (DKUserType.qiyezhu_type == workingIdentity
					|| DKUserType.getihu_type == workingIdentity) {
				if (!StringUtil.isIntNum(income)) {
					ToastUtil.showToast(mActivity, 0, "请选择您的年流水", true);
					return;
				}
			}
			if (DKUserType.otherwork_type == workingIdentity) {
				if (!StringUtil.isIntNum(incomeCash)) {
					ToastUtil.showToast(mActivity, 0, "请选择您的月现金收入", true);
					return;
				}
			}
		}

		if(DKUserType.shangbanzhu_type == workingIdentity || DKUserType.gongwuyuan_type == workingIdentity){
			if (!StringUtil.isIntNum(socialSecurityAge)) {
				ToastUtil.showToast(mActivity, 0, "请选择您的社保", true);
				return;
			}
			if(!StringUtil.checkStr(housingFundAge)){
				ToastUtil.showToast(mActivity, 0, "请选择您缴纳公积金年限", true);
				return;
			}
			housingFund = housing_found_salary_edit.getText().toString();
		}
		
		if(!StringUtil.checkStr(hasCreditCard)){
			ToastUtil.showToast(mActivity, 0, "请选择您是否有信用卡", true);
			return;
		}
		
		if(DKUserType.student_type!=workingIdentity){
			if (!StringUtil.isIntNum(creditRecord)) {
				ToastUtil.showToast(mActivity, 0, "请选择您的信用记录", true);
				return;
			}
			if (!StringUtil.isIntNum(debtType)) {
				ToastUtil.showToast(mActivity, 0, "请选择您的负债情况", true);
				return;
			}
			if (!StringUtil.isIntNum(guaranteeType)) {//guaranteeWill
				ToastUtil.showToast(mActivity, 0, "请选择您的抵押情况", true);
				return;
			}
		}
		
		// mobile, verifyCode
		if (!StringUtil.checkStr(UserData.userId)) {
			if (!StringUtil.isMobileNO(mobile)) {
				ToastUtil.showToast(mActivity, 0, "请输入有效的手机号", true);
				return;
			}
			verifyCode = verify_code_edit.getText().toString();
			if (!StringUtil.isIntNum(verifyCode)) {
				ToastUtil.showToast(mActivity, 0, "请输入正确的验证码", true);
				return;
			}
		}
		message = message_edit.getText().toString();
		loanUsed = dk_used_edit.getText().toString();
		if(!agreement_checkbox.isChecked()){
			ToastUtil.showToast(mActivity, 0, "请先同意《电兔贷款用户服务协议》", true);
			return;
		}
			
		new AsyCommitApplyLoan(mActivity,"").execute();
	}

	private boolean isVerifySuc = true;// 手机号验证是否失败

	private class AsyCommitApplyLoan extends MyAsyncTask {

		protected AsyCommitApplyLoan(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			isVerifySuc = true;
			mUserJsonService.setNeedCach(false);
			if (!StringUtil.checkStr(UserData.userId)) {
				JSONObject obj = mUserJsonService.userRegister_register(mobile,
						verifyCode);
				String msg = "";
				if (null != obj) {
					int code = obj.optInt("code");
					JSONObject data = obj.optJSONObject("data");
					if (200 == code) {
						mUserJsonService.saveLoginInfo(data);
						msg = "验证成功";
					}
				}
				if (!StringUtil.checkStr(msg)) {
					isVerifySuc = false;
					return null;
				}
			}
			riskRaw = getriskRawValue();
			return mUserJsonService.applyLoan_add(id, userName,  amount, term, city,
					 househodeCity,  companyName, income, incomeCash, socialSecurityAge,
					 creditRecord, workingAge, debtType,  guaranteeType, age, sex,
					 mobileFlag, riskRaw, workingIdentity, loanCategory, guaranteeWill,
					 message, hasCreditCard, operatePeriod,
					 startSchoolTime, housingFund, housingFundAge,businessLicense,specialStatus,lengthOfSchool, grade,loanUsed);
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			LogUtil.d(TAG, "提交申请==result111 is " + result);
			if (!isVerifySuc) {
				ToastUtil.showToast(mActivity, 0, "抱歉，手机号校验失败", true);
				return;
			}
			if (null == result)
				return;
			JSONObject data = (JSONObject) result;
			loan_id = data.optInt("id");
			String token = data.optString("token");
			String openIMUserId = data.optString("openIMUserId");
			String openIMPassword = data.optString("openIMPassword");
			LogUtil.d(TAG, "提交申请==loan_id is " + loan_id);
			if (loan_id > 0) {
				clearRisk();
				LogUtil.d(TAG, "提交申请成功==token is " + token
						+ ",openIMUserId is " + openIMUserId
						+ ",openIMPassword is " + openIMPassword);
				ToastUtil.showToast(mActivity, 0, "提交申请成功", true);
				if (StringUtil.checkStr(openIMUserId)) {
					UserData.userToken = token;
					GlobalFlag.global_token = token;
					UserDataService userService = new UserDataService(mActivity);
					Map<String, String> map = new HashMap<String, String>();
					map.put(ParamsKey.user_token, token);
					map.put(ParamsKey.openIMUserId, openIMUserId);
					map.put(ParamsKey.openIMPassword, openIMPassword);
					userService.saveData(map);
					new UserData(mActivity).setUserData();
					mActivity.startService(new Intent(mActivity, IMLoginService.class));
				}
				Bundle bundle = new Bundle();
				bundle.putInt(ParamsKey.workingIdentity, workingIdentity);
				bundle.putInt(ParamsKey.loanId, loan_id);
				IntentUtil.activityForward(mActivity,UploadInfoActivity.class, bundle, true);
			} else {
				String message = data.optString("message");
				if (!StringUtil.checkStr(message)) {
					message = "网络或服务器出问题了，稍后再试试吧";
				}
				ToastUtil.showToast(mActivity, 0, message, true);
			}
		}
	}

	public String getriskRawValue() {// salaryBehaviorSet,cityBehaviorSet,incomeBehaviorSet,
		// societyBehaviorSet,debtBehaviorSet;
		JSONArray cityArray = JSONUtil.getStringJSONArray(cityBehaviorList);
		JSONArray userNameArray = JSONUtil.getStringJSONArray(nameBehaviorList);
		JSONArray amountArray = JSONUtil.getStringJSONArray(salaryBehaviorList);
		JSONArray incomeArray = JSONUtil.getStringJSONArray(incomeBehaviorList);
		JSONArray social_security_ageArray = JSONUtil
				.getStringJSONArray(societyBehaviorList);
		JSONArray creditRecordArray = JSONUtil
				.getStringJSONArray(creditRecordList);
		JSONArray debtTypeArray = JSONUtil.getStringJSONArray(debtBehaviorList);
		JSONArray guaranteeTypeArray = JSONUtil
				.getStringJSONArray(guaranteeTypeList);
		String riskValue = JSONUtil.getriskRawValue(cityArray, userNameArray,
				amountArray, incomeArray, social_security_ageArray,
				creditRecordArray, debtTypeArray, guaranteeTypeArray);
		return riskValue;
	}

	private ArrayList<String> salaryBehaviorList, nameBehaviorList,
			cityBehaviorList, incomeBehaviorList, societyBehaviorList,
			debtBehaviorList, guaranteeTypeList, creditRecordList;

	private void initStatistics() {
		salaryBehaviorList = new ArrayList<String>();
		nameBehaviorList = new ArrayList<String>();
		cityBehaviorList = new ArrayList<String>();
		incomeBehaviorList = new ArrayList<String>();
		societyBehaviorList = new ArrayList<String>();
		debtBehaviorList = new ArrayList<String>();
		guaranteeTypeList = new ArrayList<String>();
		creditRecordList = new ArrayList<String>();
	}

	private void clearRisk() {
		salaryBehaviorList.clear();
		nameBehaviorList.clear();
		cityBehaviorList.clear();
		incomeBehaviorList.clear();
		societyBehaviorList.clear();
		debtBehaviorList.clear();
		guaranteeTypeList.clear();
		creditRecordList.clear();
	}

	private boolean canExit = false;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exit_dialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	AlertDialog dialog;
	private void exit_dialog(){
		dialog = DialogUtil.showConfirmCancleDialog(mActivity, "您确定返回，不继续填写贷款申请吗？", "确定", 
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						finish();
					}
				});
	}
	
	@Override
	public void onDateTime(int year, int month, int day) {
		LogUtil.d(TAG, "year is "+year+",month is "+month+",day is "+day);
		month = month+1;
		work_term_text.setText(year+"年"+month+"月"+day+"日");
		startSchoolTime  = year+"-"+month;
		setTextColor(work_term_text,true);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(null!=myCount){
			myCount.cancel();
			myCount = null;
		}
			
	}
}
