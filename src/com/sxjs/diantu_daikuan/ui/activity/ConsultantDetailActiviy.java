package com.sxjs.diantu_daikuan.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.constants.ParamsKey;
import com.constants.UmenClickEvevt;
import com.db.UserData;
import com.net.service.BusinessJsonService;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.CircularImage;
import com.ui.view.MyAsyncTask;
import com.utils.DialogUtil;
import com.utils.IntentUtil;
import com.utils.LogUtil;
import com.utils.PhoneUtil;
import com.utils.StringUtil;
import com.utils.ToastUtil;
import com.utils.UmenStatisticsUtil;

import org.json.JSONObject;

/*
 * 顾问详情
 */
public class ConsultantDetailActiviy extends BaseActivity implements OnClickListener{

	private static final String TAG = "XindaiUserDetailActiviy";
	private BusinessJsonService mBusinessService;
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mBusinessService = new BusinessJsonService(mActivity);
		initParams();
		initView();
		loadData();
	}

	private int id;
	private void initParams() {
		Bundle bundle = getIntent().getExtras();
		if(null==bundle)
			return;
		id = bundle.getInt(ParamsKey.id);
	}

	private View root,body;
	private CircularImage head_img;
	private TextView name_text,verify_text,area_text,company_name_text,person_desc_text,deal_volume_text,
	total_lending_text,lending_time_text;
	private ImageView verify_img;
	private void initView() {
		root = findViewById(R.id.root);
		findViewById(R.id.left_img).setOnClickListener(this);
		findViewById(R.id.call_phone_text).setOnClickListener(this);
		findViewById(R.id.send_msg_text).setOnClickListener(this);
		body = findViewById(R.id.body);
		body.setVisibility(View.INVISIBLE);
		head_img = (CircularImage) findViewById(R.id.head_img);
		name_text = (TextView) findViewById(R.id.name_text);
		verify_text = (TextView) findViewById(R.id.verify_text);
		area_text = (TextView) findViewById(R.id.area_text);
		company_name_text = (TextView) findViewById(R.id.company_name_text);
		person_desc_text = (TextView) findViewById(R.id.person_desc_text);
		deal_volume_text = (TextView) findViewById(R.id.deal_volume_text);
		total_lending_text = (TextView) findViewById(R.id.total_lending_text);
		lending_time_text = (TextView) findViewById(R.id.lending_time_text);
		verify_img = (ImageView) findViewById(R.id.verify_img);
		verify_img.setSelected(true);
	}
	
	@Override
	protected void loadData() {
		super.loadData();
		new AsyLoadData(mActivity,"").execute();
	}
	
	private class AsyLoadData extends MyAsyncTask{

		protected AsyLoadData(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			mBusinessService.setNeedCach(false);
			return mBusinessService.dk_user_getInvestUserV2(id);
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			LogUtil.d(TAG, "id is "+id+",result is "+result);
			if(null==result){
				return;
			}
			JSONObject user = ((JSONObject)result).optJSONObject("user");
			if(null==user)
				return;
			body.setVisibility(View.VISIBLE);
			bindviewData(user);
		}
	}

	private int groupId;
	private String userId,mobile,wxname;
	private int type;
	public void bindviewData(JSONObject user) {
		String userName = user.optString("userName");
		userId = user.optString("id");
		mobile = user.optString("mobile");
		wxname = user.optString("wxName");
		type = user.optInt("type");
		String avatar = user.optString("avatar");
		String city = user.optString("city");
		String companyName = user.optString("companyName");
		String loanDays = user.optString("loanDays");
		String description = user.optString("description");
		String investSuccCount = user.optString("investSuccCount");
		String investTotalAmount = user.optString("investTotalAmount");
		groupId = user.optInt("groupId");
		mImgLoad.loadImg(head_img, avatar, R.drawable.default_head);
		name_text.setText("");
		if(StringUtil.checkStr(userName)){
			name_text.setText(userName);
		}
		area_text.setText("");
		if(StringUtil.checkStr(city)){
			area_text.setText(city);
		}
		company_name_text.setText("");
		if(StringUtil.checkStr(companyName)){
			company_name_text.setText(companyName);
		}
		person_desc_text.setText("");
		if(StringUtil.checkStr(description)){
			person_desc_text.setText(description);
		}
		if(StringUtil.checkStr(investSuccCount))
			deal_volume_text.setText(investSuccCount+"单");
		if(StringUtil.checkStr(investTotalAmount)){
			total_lending_text.setText(investTotalAmount+"万元");
		}
		if(StringUtil.checkStr(loanDays)){
			lending_time_text.setText(loanDays+"天");
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_img:
			finish();
			break;
		case R.id.send_msg_text:
			send_msg();
			break;
		case R.id.call_phone_text:
			UmenStatisticsUtil.onEvent(mActivity, UmenClickEvevt.call_to_manager);
			call_phone();
			break;
		default:
			break;
		}
	}

	/*
	 * 打电话
	 */
	private AlertDialog mAlertDialog;
	private void call_phone() {
		if(!StringUtil.checkStr(mobile)){
			ToastUtil.showToast(mActivity,0,"没有此人的电话哦~", true);
			return;
		}
		mAlertDialog = DialogUtil.showConfirmCancleDialog(mActivity, "拨打电话"+mobile, "确定", new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
				PhoneUtil.callPhone(mActivity,mobile);
				mAlertDialog.dismiss();
			}
		});
	}

	private void send_msg() {
		if(!StringUtil.checkStr(UserData.userId)){
			IntentUtil.activityForward(mActivity, LoginActivity.class, null, false);
			return;
		}
		//mContext.mOpenImUtil.openNewConversation(mActivity, userId);
		if(2==type){//INVSET("出资用户", 2),   EXPERT("电兔顾问", 3),
			mContext.mOpenImUtil.openNewConversation(mActivity, userId);
		}else{
			mContext.mOpenImUtil.openCustomerChat(mActivity,wxname);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//AnimationUtil.fadeAnimat(mActivity, root, false);
	}

	@Override
	protected int setContentView() {
		return R.layout.consultant_detail;
	}
}
