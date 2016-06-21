package com.sxjs.diantu_daikuan.ui.fragment;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.constants.DKUserType;
import com.model.OrderInfo;
import com.net.service.BusinessJsonService;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.MyAsyncTask;
import com.utils.ColorUtil;
import com.utils.DatetimeUtil;
import com.utils.StringUtil;

import org.json.JSONObject;

import java.util.ArrayList;

public class ApplyDetailFg extends BaseFragment {

	private BusinessJsonService mBusinessService;
	private int loanId;
	public ApplyDetailFg(int loanId){
		this.loanId = loanId;
	}
	
	@Override
	protected int setContentView() {
		return R.layout.apply_detail_fg;
	}
	
	@Override
	protected void initView() {
		mBusinessService = new BusinessJsonService(mActivity);
		initV();
		loadData();
	}

	private TextView title1_text,title2_key_text,title2_text,title3_text;
	private LinearLayout detail_group;
	private void initV() {
		title1_text = (TextView) findViewById(R.id.title1_text);
		title2_key_text = (TextView) findViewById(R.id.title2_key_text);
		title2_text = (TextView) findViewById(R.id.title2_text);
		title3_text = (TextView) findViewById(R.id.title3_text);
		detail_group = (LinearLayout) findViewById(R.id.detail_group);
	}

	@Override
	protected void loadData() {
		super.loadData();
		new AsyLoadData(mActivity, "").execute();
	}
	
	private class AsyLoadData extends MyAsyncTask{

		protected AsyLoadData(Context context, String title) {
			super(context, title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			mBusinessService.setNeedCach(false);
			return mBusinessService.dk_loan_detail(loanId);
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if(null==result)
				return;
			bindViewData((JSONObject)result);
		}
	}

	private long createTime;
	private int workingIdentity,hasCreditCard,businessLicense,housingFund,
	useType,userId,term,guaranteeWill;
	private String loanCategory,voiceLength,amount,sex,age,city,userMobile, userName,followRemark,workingIdentityStr,
	househodeCity,companyName,operatePeriod,startSchoolTime,socialSecurityAge,housingFundAge,userAge,income,incomeCash,workingAge,
	creditRecord,debtType,guaranteeType,message,lengthOfSchool,loanUsed,grade,identityImg;
	public void bindViewData(JSONObject dataResult) {
		guaranteeWill = dataResult.optInt("guaranteeWill");
		workingIdentity = dataResult.optInt("workingIdentity");
		workingIdentityStr = dataResult.optString("workingIdentityStr");
		househodeCity = dataResult.optString("househodeCity");
		hasCreditCard = dataResult.optInt("hasCreditCard");
		businessLicense = dataResult.optInt("businessLicense");
		operatePeriod = dataResult.optString("operatePeriod");
		housingFund = dataResult.optInt("housingFund");
		loanCategory = dataResult.optString("loanCategory");
		housingFundAge = dataResult.optString("housingFundAge");
		startSchoolTime = dataResult.optString("startSchoolTime");
		userId = dataResult.optInt("userId");
		useType = dataResult.optInt("useType");
		String userAvatar = dataResult.optString("userAvatar");
		userName = dataResult.optString("userName");
		userMobile = dataResult.optString("userMobile");
		voiceLength = dataResult.optString("voiceLength");
		amount = dataResult.optString("amount");           //
		term = dataResult.optInt("term");
		sex = dataResult.optString("sex");
		age = dataResult.optString("age");
		createTime = dataResult.optLong("createTime");
		city = dataResult.optString("city");
		userAge = dataResult.optString("userAge");
		income = dataResult.optString("income");
		incomeCash = dataResult.optString("incomeCash");
		companyName = dataResult.optString("companyName");
		workingAge = dataResult.optString("workingAge");
		socialSecurityAge = dataResult.optString("socialSecurityAge");
		creditRecord = dataResult.optString("creditRecord");
		debtType = dataResult.optString("debtType");
		message = dataResult.optString("message");
		guaranteeType = dataResult.optString("guaranteeType");
		lengthOfSchool = dataResult.optString("lengthOfSchool");
		loanUsed = dataResult.optString("loanUsed");
		grade = dataResult.optString("grade");
		identityImg = dataResult.optString("identityImg");
		if (1 == useType) {
			title3_text.setText("企业经营贷");
		} else {
			if(StringUtil.checkStr(loanCategory)){
				title3_text.setText(loanCategory);
			}else{
				title3_text.setText("个人消费贷");
			}
		}

		title1_text.setText("");
		if(StringUtil.checkStr(amount)){
			title1_text.setText("￥"+amount);
		}
		
		title2_text.setText(term+"");
		showOrederDetail();
	}

	private void showOrederDetail() {
		ArrayList<OrderInfo> oiList = getOrderInfoList();
		if(null==oiList)
			return;
		int size = oiList.size();
		if(size<=0)
			return;
		detail_group.removeAllViews();
		boolean isOushu=false;;
		for (int i = 0; i < size; i++) {
			View view = mInflater.inflate(R.layout.order_detail_key_value, null);
			View item_root = view.findViewById(R.id.item_root);
			TextView key_text = (TextView) view.findViewById(R.id.key_text);
			TextView value_text = (TextView) view.findViewById(R.id.value_text);
			String name = oiList.get(i).name;
			String value = oiList.get(i).value;
			key_text.setText(name+"");
			value_text.setText("无");
			if (StringUtil.checkStr(value)) {
				value_text.setText(value);
			}
			if (i % 2 == 0) {
				// 偶数
				isOushu = true;
				item_root.setBackgroundColor(ColorUtil.getColor(mContext,
						R.color.page_color));
			} else {
				// 奇数
				isOushu = false;
				item_root.setBackgroundColor(ColorUtil.getColor(mContext,
						R.color.white));
			}
			detail_group.addView(view);
		}
		//添加证件资料照片
		/*if(StringUtil.isHttpUrl(identityImg)){
			View img_view = mInflater.inflate(R.layout.order_detail_img_view, null);
			View item_root = img_view.findViewById(R.id.item_root);
			if (isOushu) {
				item_root.setBackgroundColor(ColorUtil.getColor(mContext,
						R.color.white));
			}else{
				item_root.setBackgroundColor(ColorUtil.getColor(mContext,
						R.color.page_color));
			}
			TextView key_text = (TextView) img_view.findViewById(R.id.key_text);
			ImageView value_img = (ImageView) img_view.findViewById(R.id.value_img);
			key_text.setText("证件照");
			mImgLoad.loadImg(value_img, identityImg, R.drawable.cd_shili);
			detail_group.addView(img_view);
		}*/
	}
	
	private ArrayList<OrderInfo> getOrderInfoList(){
		ArrayList<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
		orderInfoList.add(getOrderInfo("申请时间", DatetimeUtil.getYMDTimeLocal2(createTime)));
		if(StringUtil.checkStr(loanCategory)){
			orderInfoList.add(getOrderInfo("贷款类型",loanCategory ));
		}
		if(term>0){
			orderInfoList.add(getOrderInfo("贷款期限(月)",term+"" ));
		}
		if(StringUtil.checkStr(userName))
			orderInfoList.add(getOrderInfo("姓名", userName));
		if(StringUtil.checkStr(age)){
			orderInfoList.add(getOrderInfo("年龄",age));
		}
		if(StringUtil.checkStr(sex)){
			orderInfoList.add(getOrderInfo("性别", sex));
		}
		if(StringUtil.checkStr(city)){
			orderInfoList.add(getOrderInfo("所在城市",city));
		}
		
		if(StringUtil.checkStr(househodeCity)){
			orderInfoList.add(getOrderInfo("户籍城市", househodeCity));
		}
		if(StringUtil.checkStr(workingIdentityStr)){
			orderInfoList.add(getOrderInfo("工作类型", workingIdentityStr));
		}
		addincome(orderInfoList);
		addincomeCash(orderInfoList);
		if(DKUserType.gongwuyuan_type==workingIdentity||
				DKUserType.shangbanzhu_type==workingIdentity){
			if(StringUtil.checkStr(companyName)){
				orderInfoList.add(getOrderInfo("工作单位", companyName));
			}
			if(StringUtil.checkStr(workingAge)){
				orderInfoList.add(getOrderInfo("工作年限", workingAge));
			}
			if(StringUtil.checkStr(socialSecurityAge)){
				orderInfoList.add(getOrderInfo("社保年限", socialSecurityAge));
			}
			if(StringUtil.checkStr(housingFundAge)){//
				orderInfoList.add(getOrderInfo("公积金年限", housingFundAge));
			}
			if(housingFund>0){
				orderInfoList.add(getOrderInfo("公积金月缴纳金额(元/月)", String.valueOf(housingFund)));
			}
			addHasCreditCard(orderInfoList);
			addDebetGuaranteeType(orderInfoList);
		}else if(DKUserType.qiyezhu_type==workingIdentity){
			if(StringUtil.checkStr(companyName)){
				orderInfoList.add(getOrderInfo("企业全称",companyName));
			}
			addOperatePeriod(orderInfoList);
			addHasCreditCard(orderInfoList);
			addDebetGuaranteeType(orderInfoList);
		}else if(DKUserType.getihu_type==workingIdentity){
			addOperatePeriod(orderInfoList);
			addBusinessLicense(orderInfoList);
			addHasCreditCard(orderInfoList);
			addDebetGuaranteeType(orderInfoList);
		}else if(DKUserType.student_type==workingIdentity){
			if(StringUtil.checkStr(companyName)){
				orderInfoList.add(getOrderInfo("学校名称", companyName));
			}
			if(StringUtil.checkStr(lengthOfSchool)){
				orderInfoList.add(getOrderInfo("学制", lengthOfSchool));
			}
			if(StringUtil.checkStr(grade)){
				orderInfoList.add(getOrderInfo("年级", grade));
			}
			
			if(StringUtil.checkStr(startSchoolTime)){
				if(startSchoolTime.contains("-")){
					String[] aa = startSchoolTime.split("-");
					StringBuilder sb = new StringBuilder();
					if(aa.length>=2){
						for(int i=0;i<2;i++){
							String bb = aa[i];
							if(0==i){
								startSchoolTime = bb+"年";
							}else{
								startSchoolTime = startSchoolTime+bb+"月";
							}
						}
					}
				}
				orderInfoList.add(getOrderInfo("入学时间", startSchoolTime));
			}
			addHasCreditCard(orderInfoList);
		}else if(DKUserType.otherwork_type==workingIdentity){
			addHasCreditCard(orderInfoList);
			addDebetGuaranteeType(orderInfoList);
		}
		
		if(0!=guaranteeWill){
			String guaranteeWillStr = "否";
			if(1==guaranteeWill){
				guaranteeWillStr = "是";
			}
			orderInfoList.add(getOrderInfo("是否可做抵押", guaranteeWillStr));
		}
		if(StringUtil.checkStr(loanUsed)){
			orderInfoList.add(getOrderInfo("贷款用途", loanUsed));
		}
		
		if(StringUtil.checkStr(message)){
			orderInfoList.add(getOrderInfo("我的留言", message));
		}
		return orderInfoList;
	}

	/*
	 * income月打卡工资或流水
	 */
	private void addincome(ArrayList<OrderInfo> orderInfoList){
		if(StringUtil.checkStr(income)){
			if(DKUserType.qiyezhu_type==workingIdentity||DKUserType.getihu_type==workingIdentity){
				orderInfoList.add(getOrderInfo("年流水(万元)", income));
			}else if(DKUserType.gongwuyuan_type==workingIdentity||DKUserType.shangbanzhu_type==workingIdentity){
				orderInfoList.add(getOrderInfo("月打卡工资(元)", income));
			}
		}
	}
	
	/*
	 * incomeCash月现金工资
	 */
	private void addincomeCash(ArrayList<OrderInfo> orderInfoList){
		if(StringUtil.checkStr(incomeCash)){
			orderInfoList.add(getOrderInfo("月现金工资(元)", incomeCash));
		}
	}
	
	/*
	 * 经营年限
	 */
	private void addOperatePeriod(ArrayList<OrderInfo> orderInfoList){
		if(StringUtil.checkStr(operatePeriod))
			orderInfoList.add(getOrderInfo("经营年限", operatePeriod));
	}
	/*
	 * 是否有信用卡
	 */
	private void addHasCreditCard(ArrayList<OrderInfo> orderInfoList) {
		if(hasCreditCard>=0){
			String hasCreditCardStr = "无";
			if(hasCreditCard>0)
				hasCreditCardStr = "有";
			orderInfoList.add(getOrderInfo("是否有信用卡", hasCreditCardStr));
		}
		if(StringUtil.checkStr(creditRecord)){
			orderInfoList.add(getOrderInfo("信用记录", creditRecord));
		}
	}
	
	/*
	 * 是否有businessLicense营业执照
	 */
	private void addBusinessLicense(ArrayList<OrderInfo> orderInfoList) {
		if(businessLicense>=0){
			String businessLicenseStr = "无";
			if(businessLicense>0)
				businessLicenseStr = "有";
			orderInfoList.add(getOrderInfo("是否有营业执照", businessLicenseStr));
		}
	}
	
	private void addDebetGuaranteeType(ArrayList<OrderInfo> orderInfoList){
		if(StringUtil.checkStr(debtType)){
			orderInfoList.add(getOrderInfo("负债",debtType ));
		}
		if(StringUtil.checkStr(guaranteeType)){
			orderInfoList.add(getOrderInfo("资产", guaranteeType));
		}
	}
	private OrderInfo getOrderInfo(String name,String value){
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.name = name;
		orderInfo.value = value;
		return orderInfo;
	}
}
