package com.sxjs.diantu_daikuan.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.constants.ParamsKey;
import com.constants.UmenClickEvevt;
import com.db.UserData;
import com.net.service.BusinessJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.ManagerInfoFgAdapter;
import com.ui.view.CircularImage;
import com.ui.view.MyAsyncTask;
import com.utils.DialogUtil;
import com.utils.IntentUtil;
import com.utils.LogUtil;
import com.utils.PhoneUtil;
import com.utils.ScreenUtil;
import com.utils.StringUtil;
import com.utils.ToastUtil;
import com.utils.UmenStatisticsUtil;
import com.utils.UserHeadShowUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * 信贷经理详情
 */
public class CreditManagerDetailActiviy extends BaseActivity implements OnClickListener{

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

	private int[] textId = { R.id.textview1, R.id.textview2 };
	private int[] imgId = {R.id.imgview1,R.id.imgview2};
	private LinearLayout horizon_group;
	private View root,body;
	private CircularImage head_img;
	private TextView name_text,verify_text,company_name_text,textview2,head_text;
	private ImageView verify_img;
	private ViewPager viewpager;
	private void initView() {
		root = findViewById(R.id.root);
		findViewById(R.id.left_img).setOnClickListener(this);
		findViewById(R.id.call_phone_text).setOnClickListener(this);
		findViewById(R.id.send_msg_text).setOnClickListener(this);
		textview2 = (TextView) findViewById(R.id.textview2);
		body = findViewById(R.id.body);
		body.setVisibility(View.INVISIBLE);
		head_img = (CircularImage) findViewById(R.id.head_img);
		head_text = (TextView) findViewById(R.id.head_text);
		name_text = (TextView) findViewById(R.id.name_text);
		verify_text = (TextView) findViewById(R.id.verify_text);
		company_name_text = (TextView) findViewById(R.id.company_name_text);
		verify_img = (ImageView) findViewById(R.id.verify_img);
		verify_img.setSelected(true);
		horizon_group = (LinearLayout) findViewById(R.id.horizon_group);
		viewpager = (ViewPager) findViewById(R.id.viewpager);//android.support.v4.app.FragmentManager
	}

	private void setPagerAdapter() {
		viewpager.setAdapter(new ManagerInfoFgAdapter(mActivity.getSupportFragmentManager(),invest,score));
		viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				controBottomTab(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	private void controBottomTab(int curIndex) {
		for (int i = 0; i < textId.length; i++) {
			TextView tab_text = (TextView) findViewById(textId[i]);
			ImageView imgview = (ImageView) findViewById(imgId[i]);
			tab_text.setTag(i);
			tab_text.setOnClickListener(new MyClickListener());
			tab_text.setSelected(false);
			imgview.setVisibility(View.INVISIBLE);
			if (curIndex == i){
				tab_text.setSelected(true);
				imgview.setVisibility(View.VISIBLE);
			}
		}
	}
	
	private class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			viewpager.setCurrentItem((Integer) v.getTag());
		}
	}
	
	private void show_manager_info(String city,int investSuccCount,double loanDays) {
		horizon_group.removeAllViews();
		int screenW = ScreenUtil.getWidth(mActivity);
		int height = ScreenUtil.dip2px(mActivity, 50.0f);
		int w = screenW/3;
		for(int i=0;i<3;i++){
			View view = mInflater.inflate(R.layout.manager_info_item, null);
			TextView key_text = (TextView) view.findViewById(R.id.key_text);
			TextView value_text = (TextView) view.findViewById(R.id.value_text);
			View right_line = view.findViewById(R.id.right_line);
			RelativeLayout item_root = (RelativeLayout) view.findViewById(R.id.item_root);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w,height);
			if(0==i){
				key_text.setText("地区");
				value_text.setText("全国");
				if(StringUtil.checkStr(city)){
					value_text.setText(city);
				}
			}else if(1==i){
				key_text.setText("累计成交");
				value_text.setText(investSuccCount+"单");
			}else if(2==i){//0 个人消费贷 1 企业经营贷
				key_text.setText("平均放款时间");
				value_text.setText(loanDays+"天");
				right_line.setVisibility(View.INVISIBLE);
			}
			item_root.setLayoutParams(params);
			horizon_group.addView(view);
		}
	}

	@Override
	protected void loadData() {
		super.loadData();
		new AsyLoadData(mActivity,"").execute();
	}
	
	private int comment_count;
	private JSONObject user;
	private JSONArray invest,score;
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
			JSONObject data = (JSONObject) result;
			String message = data.optString("message");
			if(StringUtil.checkStr(message)){
				ToastUtil.showToast(mActivity, 0, message, true);
			}
			score = data.optJSONArray("score");
			invest = data.optJSONArray("invest");
			if(null!=score){
				comment_count = score.length();
			}
			textview2.setText("最近评论("+comment_count+")");
			JSONObject user = data.optJSONObject("user");
			if(null==user)
				return;
			body.setVisibility(View.VISIBLE);
			bindviewData(user);
			setPagerAdapter();
		}
	}

	private int groupId;
	private String userId,mobile,wxname;
	private int type;
	public void bindviewData(JSONObject user) {
		if(null==user)
			return;
		String userName = user.optString("userName");
		userId = user.optString("id");
		mobile = user.optString("mobile");
		wxname = user.optString("wxname");
		type = user.optInt("type");
		String avatar = user.optString("avatar");
		String city = user.optString("city");
		String companyName = user.optString("companyName");
		double loanDays = user.optDouble("loanDays");
		String description = user.optString("description");
		int investSuccCount = user.optInt("investSuccCount");
		String investTotalAmount = user.optString("investTotalAmount");
		groupId = user.optInt("groupId");
		UserHeadShowUtil.showHead(mImgLoad, head_img, head_text, avatar, userName);
		name_text.setText("");
		if(StringUtil.checkStr(userName)){
			name_text.setText(userName);
		}
		
		company_name_text.setText("");
		if(StringUtil.checkStr(companyName)){
			company_name_text.setText(companyName);
		}
		show_manager_info(city, investSuccCount,loanDays);
		controBottomTab(0);
		
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
		return R.layout.credit_manager_detail;
	}
}
