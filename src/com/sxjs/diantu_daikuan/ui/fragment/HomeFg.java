package com.sxjs.diantu_daikuan.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.constants.ActionString;
import com.constants.CateData;
import com.constants.GlobalFlag;
import com.constants.ParamsKey;
import com.constants.UmenClickEvevt;
import com.db.UserData;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.net.service.BusinessJsonService;
import com.openim.ConversationSampleHelper;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.activity.ChooseCityActivity;
import com.sxjs.diantu_daikuan.ui.activity.ConsultantDetailActiviy;
import com.sxjs.diantu_daikuan.ui.activity.DK_PD_DetailActivity;
import com.sxjs.diantu_daikuan.ui.activity.DaikuanProductDetailActivity;
import com.sxjs.diantu_daikuan.ui.activity.HotDaikuanCashInstallmentsActivity;
import com.sxjs.diantu_daikuan.ui.activity.LoginActivity;
import com.sxjs.diantu_daikuan.ui.activity.WebviewActivity;
import com.sxjs.diantu_daikuan.ui.adapter.HomeCateAdapter;
import com.sxjs.diantu_daikuan.ui.adapter.ViewPagerCycleAdapter;
import com.ui.view.CircularImage;
import com.ui.view.CountTextView;
import com.ui.view.ImgScrollPointView;
import com.ui.view.MyAsyncTask;
import com.utils.AnimationUtil;
import com.utils.DatetimeUtil;
import com.utils.GPSInfo;
import com.utils.IntentUtil;
import com.utils.LogUtil;
import com.utils.ScreenUtil;
import com.utils.StringUtil;
import com.utils.UmenStatisticsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class HomeFg extends BaseFragment implements OnClickListener, Callback {

	private static final String TAG = "HomeFg";
	private BusinessJsonService mBusinessService;
	private static final int Interval_Time = 3 * 1000;
	private String city;

	private int screenW;
	private int[] top_left_textIds = { R.id.top_person_num1_text,
			R.id.top_person_num2_text, R.id.top_person_num3_text,
			R.id.top_person_num4_text, R.id.top_person_num5_text };
	private int[] top_right_textIds = { R.id.top_company_num1_text,
			R.id.top_company_num2_text, R.id.top_company_num3_text,
			R.id.top_company_num4_text };
	private Handler mHandler;
	private ConversationSampleHelper mConversationHelper;

	private boolean isShowLoading;
	@Override
	protected int setContentView() {
		return R.layout.home_fg;
	}

	private GPSInfo mGPSInfo;
	@Override
	protected void initView() {
		mConversationHelper = mContext.mOpenImUtil.mConversationHelper;
		mHandler = new Handler(this);
		mBusinessService = new BusinessJsonService(mActivity);
		mGPSInfo = new GPSInfo(mActivity);
		screenW = ScreenUtil.getWidth(mActivity);
		registerCeiver();
		initV();
		city = UserData.local_city;
		isShowLoading = true;
		loadData();
	}

	@Override
	public void onResume() {
		super.onResume();
		//AnimationUtil.fadeAnimat(mActivity, root, true);
		city = UserData.local_city;
		if (StringUtil.checkStr(city)) {
			city_name_text.setText(city);
		}
		int total = mConversationHelper.getTotalUnreadCount();
		tips_text.setVisibility(View.INVISIBLE);
		if (total > 0) {
			tips_text.setVisibility(View.VISIBLE);
			tips_text.setText(String.valueOf(total));
		}
		show_cloud_anima();
	}

	private void initTopLeftTexts(String personNumStr) {
		char[] aa = personNumStr.toCharArray();
		for (int i = 0; i < 4; i++) {
			CountTextView num_text = (CountTextView) findViewById(top_left_textIds[i]);
			char a = aa[i];
			int number = Integer.parseInt(a + "");
			num_text.setNumber(number);
			num_text.showNumberWithAnimation(number);
			// num_text.setText(aa[i]+"");
		}
	}

	private void initTopRightTexts(String enterpriseNumStr) {
		char[] bb = enterpriseNumStr.toCharArray();
		for (int i = 0; i < 4; i++) {
			CountTextView num_text = (CountTextView) findViewById(top_right_textIds[i]);
			// num_text.setText(bb[i]+"");
			char b = bb[i];
			int number = Integer.parseInt(b + "");
			num_text.setNumber(number);
			num_text.showNumberWithAnimation(number);
		}
	}

	private View body, banner_ll, root;
	private TextView city_name_text, tips_text;
	private AutoScrollViewPager view_pager;
	private LinearLayout img_point_viewgroup, hotdai_group;
	private PullToRefreshScrollView pull_refresh_scrollview;
	private ImgScrollPointView mImgScrollPointView;
	private HomeCateAdapter mCateAdapter;
	private LinearLayout consant_group,dk_cate_group;
	private ImageView left_cloud_img,right_cloud_img;
	private float left_cloud_imgX,right_cloud_imgX;
	private void initV() {
		root = findViewById(R.id.root);
		findViewById(R.id.city_ll).setOnClickListener(this);
		findViewById(R.id.textview).setVisibility(View.INVISIBLE);
		TextView textview = (TextView) findViewById(R.id.textview);
		tips_text = (TextView) findViewById(R.id.tips_text);

		findViewById(R.id.head_right_ll).setOnClickListener(this);
		left_cloud_img = (ImageView) findViewById(R.id.left_cloud_img);
		right_cloud_img = (ImageView) findViewById(R.id.right_cloud_img);
		left_cloud_imgX = left_cloud_img.getRight();
		right_cloud_imgX = right_cloud_img.getLeft();
		
		consant_group = (LinearLayout) findViewById(R.id.consant_group);
		dk_cate_group = (LinearLayout) findViewById(R.id.dk_cate_group);
		pull_refresh_scrollview = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		//pull_refresh_scrollview.get
		setPullToRefreshScrollView();
		body = findViewById(R.id.body);
		body.setVisibility(View.INVISIBLE);
		findViewById(R.id.person_apply_ll).setOnClickListener(this);
		findViewById(R.id.company_apply_ll).setOnClickListener(this);
		findViewById(R.id.bottom_apply_text).setOnClickListener(this);
		banner_ll = findViewById(R.id.banner_ll);
		findViewById(R.id.city_ll).setOnClickListener(this);
		findViewById(R.id.more_hot_ll).setOnClickListener(this);
		hotdai_group = (LinearLayout) findViewById(R.id.hotdai_group);
		
		city_name_text = (TextView) findViewById(R.id.city_name_text);
		mGPSInfo.setTextView(city_name_text);
		img_point_viewgroup = (LinearLayout) findViewById(R.id.img_point_viewgroup);
		view_pager = (AutoScrollViewPager) findViewById(R.id.view_pager);
		RelativeLayout banner_rl = (RelativeLayout) findViewById(R.id.banner_rl);// screenW*164/750
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenW,screenW*200/750);
		banner_rl.setLayoutParams(params);
		view_pager.setInterval(Interval_Time);
		view_pager.setCycle(true);
		view_pager.startAutoScroll();
		view_pager.setBorderAnimation(true);
		view_pager.setStopScrollWhenTouch(true);
		mImgScrollPointView = new ImgScrollPointView(mActivity,
				img_point_viewgroup);
		int showW = screenW*2/3;
		int showH = showW*24/81;
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(showW,showH);//810*240
		param.gravity=Gravity.CENTER_HORIZONTAL;
		ImageView bottom_apply_img = (ImageView) findViewById(R.id.bottom_apply_img);
		bottom_apply_img.setLayoutParams(param);
	}

	private void show_cloud_anima() {
		AnimationUtil.showTranslateAnim(mActivity, left_cloud_imgX, left_cloud_imgX+screenW, left_cloud_img);//-80
		AnimationUtil.showTranslateAnim(mActivity, right_cloud_imgX, right_cloud_imgX-screenW, right_cloud_img);//+70
	}

	private void setPullToRefreshScrollView() {
		pull_refresh_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		pull_refresh_scrollview
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// 更新
						String label = DateUtils.formatDateTime(
								mActivity.getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						// Update the LastUpdatedLabel
						ILoadingLayout loadingLayout = refreshView.getLoadingLayoutProxy();
						loadingLayout.setLoadingDrawable(mActivity.getResources().getDrawable(R.drawable.home_refresh_icon));
						//loadingLayout.set
						loadingLayout.setLastUpdatedLabel(label);
						isShowLoading = false;
						loadData();
					}
				});
	}

	@Override
	protected void loadData() {
		super.loadData();
		mImgLoad.setNeedLoad(false);
		AsyLoadData fetch = new AsyLoadData(mActivity, "");
		fetch.setShowLoading(isShowLoading);
		fetch.execute();
	}

	private class AsyLoadData extends MyAsyncTask {

		protected AsyLoadData(Context context, String title) {
			super(context, title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			mBusinessService.setNeedCach(false);
			return mBusinessService.dk_index(city);
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			mImgLoad.setNeedLoad(true);
			pull_refresh_scrollview.onRefreshComplete();
			if (null == result)
				return;
			pull_refresh_scrollview.setBackgroundColor(mActivity.getResources().getColor(R.color.orange_red_bg));
			body.setVisibility(View.VISIBLE);
			bindviewData((JSONObject) result);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtil.d(TAG, "onActivityCreated()====");
		registerCeiver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, "onDestroy()====");
		unRegisterReceiver();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtil.d(TAG, "onDestroyView()====");
		mGPSInfo.stopListener();
		//
	}

	/*
	 * 数据绑定
	 */
	private int personNum, enterpriseNum;
	private JSONArray consultArrays;

	public void bindviewData(JSONObject data) {
		personNum = data.optInt("personNum");
		if (personNum < 10000) {
			personNum = 12037;
		}
		enterpriseNum = data.optInt("enterpriseNum");
		consultArrays = data.optJSONArray("users");
		JSONArray articles = data.optJSONArray("articles");
		JSONArray cmsExtendList = data.optJSONArray("cmsExtendList");
		addTopScoreView();
		addDiantuConsult();
		addDKCate();
		addDKGonglve(articles);
		addBannerData(cmsExtendList);
	}

	private void addDKCate() {
		dk_cate_group.removeAllViews();
		int[] imgsId = CateData.imgsId;
		String[] data = mActivity.getResources().getStringArray(R.array.home_cate_name);
		for(int i=0;i<imgsId.length;i++){
			final int position = i;
			final String name = data[position];
			View view = mInflater.inflate(R.layout.home_cate_grid_item, null);
			View item_root = view.findViewById(R.id.item_root);
			ImageView imgview = (ImageView) view.findViewById(R.id.imgview);
			TextView textview = (TextView) view.findViewById(R.id.textview);
			imgview.setBackgroundResource(imgsId[i]);
			textview.setText(name);
			
			item_root.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					GlobalFlag.loanCategoryName = name;
					send_apply_loan_broad(ParamsKey.person_daikuan_type, position + 1);
				}
			});
			dk_cate_group.addView(view);
		}
	}

	private void addDiantuConsult() {
		//ArrayList<JSONObject> data = JSONUtil.arrayToList(consultArrays);
		if(null==consultArrays||consultArrays.length()<=0)
			return;
		consant_group.removeAllViews();
		for(int i=0;i<consultArrays.length();i++){
			JSONObject obj = consultArrays.optJSONObject(i);
			View view = mInflater.inflate(R.layout.home_consult_item, null);
			CircularImage head_img = (CircularImage) view.findViewById(R.id.head_img);
			TextView status_text = (TextView) view.findViewById(R.id.status_text);
			TextView name_text = (TextView) view.findViewById(R.id.name_text);
			TextView desc_text = (TextView) view.findViewById(R.id.desc_text);
			View divider_img = view.findViewById(R.id.divider_img);
			View item_root = view.findViewById(R.id.item_root);
			if(i==consultArrays.length()-1){
				divider_img.setVisibility(View.INVISIBLE);
			}else{
				divider_img.setVisibility(View.VISIBLE);
			}
			final int id = obj.optInt("id");
			String userName = obj.optString("userName");
			String advantage = obj.optString("advantage");
			String description = obj.optString("description");
			String avatar = obj.optString("avatar");
			int authFlag = obj.optInt("onlineFlag");
			if(authFlag>0){
				status_text.setBackgroundResource(R.drawable.busy_small_ico);
			}else{
				status_text.setBackgroundResource(R.drawable.online_small_ico);
			}
			mImgLoad.loadImg(head_img, avatar, R.drawable.default_head);
			if(StringUtil.checkStr(userName)){
				name_text.setText(userName);
			}
			if(StringUtil.checkStr(advantage)){
				desc_text.setText(advantage);
			}
			head_img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putInt(ParamsKey.id, id); 
					IntentUtil.activityForward(mActivity, ConsultantDetailActiviy.class, bundle, false);
				}
			});
			final int position =i;
			item_root.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					consanlt_chat(position);
				}
			});
			consant_group.addView(view);
		}
	}

	/*
	 * 顶部计分数
	 */
	private void addTopScoreView() {
		// String personNumStr = StringUtil.getFourDigit(personNum);
		// String enterpriseNumStr = StringUtil.getFourDigit(enterpriseNum);
		initTopLeftTexts(StringUtil.getRandomFourDigit2(true));
		initTopRightTexts(StringUtil.getRandomFourDigit2(false));
	}

	/*
	 * 添加贷款攻略
	 */
	private void addDKGonglve(JSONArray articles) {
		LogUtil.d(TAG, "添加热门贷款攻略==" + articles);
		if (null == articles || articles.length() <= 0)
			return;
		hotdai_group.removeAllViews();
		for (int i = 0; i < articles.length(); i++) {
			View view = mInflater.inflate(R.layout.hot_daikuan_item, null);
			hotdai_group.addView(view);
			final JSONObject obj = articles.optJSONObject(i);
			final String title = obj.optString("title");
			String summary = obj.optString("summary");
			String image = obj.optString("image");
			long createTime = obj.optLong("createTime");
			int readCount = obj.optInt("readCount");
			
			ImageView img = (ImageView) view.findViewById(R.id.img);
			TextView text1 = (TextView) view.findViewById(R.id.text1);
			TextView text2 = (TextView) view.findViewById(R.id.text2);
			TextView text3 = (TextView) view.findViewById(R.id.text3);
			mImgLoad.loadImg(img, image, R.drawable.default_bank_img);
			text1.setText(summary+"");
			text2.setText(DatetimeUtil.getTimeLocal(createTime));
			text3.setText(readCount+"阅读");
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String link = obj.optString("link");
					Bundle bundle = new Bundle();
					bundle.putString(ParamsKey.head_name, "贷款攻略详情");
					bundle.putString(ParamsKey.web_url, link);
					IntentUtil.activityForward(mActivity,
							WebviewActivity.class, bundle, false);
				}
			});
		}
	}

	private ViewPagerCycleAdapter mViewPagerCycleAdapter;

	private void addBannerData(JSONArray cmsList) {
		banner_ll.setVisibility(View.GONE);
		if (null == cmsList || cmsList.length() <= 0)
			return;
		banner_ll.setVisibility(View.VISIBLE);
		List<View> viewLists = new ArrayList<View>();
		viewLists.clear();
		for (int i = 0; i < cmsList.length(); i++) {
			final JSONObject cmsExtend = cmsList.optJSONObject(i);
			String image = cmsExtend.optString("image");
			View view = mInflater.inflate(R.layout.banner_item, null);//LayoutParams.WRAP_CONTENT
			ImageView imageView = (ImageView) view.findViewById(R.id.imgview);//750*164   50：11 screenW*164/50
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenW,screenW*200/750);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			mImgLoad.loadImg(imageView, image, 0);
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					LogUtil.d(TAG, "addBannerData==onClick==cmsExtend is "+cmsExtend);
					banner_forward(cmsExtend);
				}
			});
			/*view_pager.setOnSingleTouchListener(new AutoScrollViewPager.OnSingleTouchListener() {
				@Override
				public void onSingleTouch() {
					LogUtil.d(TAG, "addBannerData==onClick==cmsExtend is "+cmsExtend);
					banner_forward(cmsExtend);
				}
			});*/
			viewLists.add(view);
		}
		
		mImgScrollPointView.addImgScrollPoint(viewLists.size());
		view_pager.setAdapter(mViewPagerCycleAdapter = new ViewPagerCycleAdapter(
						viewLists));
		view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageSelected(int arg0) {
						mImgScrollPointView.movePoint(arg0);
					}

				});
	}

	/*
	 * type; //0 外链 1银行贷款 2现金分期 3银行服务 4信用卡服务
	 */
	protected void banner_forward(JSONObject cmsExtend) {
		LogUtil.d(TAG, "banner_forward==cmsExtend is " + cmsExtend);
		UmenStatisticsUtil.onEvent(mActivity, UmenClickEvevt.home_banner_click);
		int type = cmsExtend.optInt("type");
		int id = cmsExtend.optInt("id");
		Bundle bundle = new Bundle();
		bundle.putInt(ParamsKey.id, id);
		String link = cmsExtend.optString("link");
		String title = cmsExtend.optString("title");
		bundle.putString(ParamsKey.head_name, title);
		switch (type) {
		case 0:
			bundle.putString(ParamsKey.web_url, link);
			if (StringUtil.checkStr(link) && link.startsWith("http")) {//
				IntentUtil.activityForward(mActivity, WebviewActivity.class,
						bundle, false);
			}
			break;
		case 1:
			IntentUtil.activityForward(mActivity,
					DaikuanProductDetailActivity.class, bundle, false);
			break;
		case 2:
			bundle.putString(ParamsKey.head_name, "现金分期详情");
			IntentUtil.activityForward(mActivity,
					DaikuanProductDetailActivity.class, bundle, false);
			break;
		case 5:
			if (StringUtil.checkStr(link)) {
				String pd_id_str = link.substring(link.lastIndexOf("=") + 1);
				if (StringUtil.isIntNum(pd_id_str)) {
					int pd_id = Integer.parseInt(pd_id_str);
					bundle.putInt(ParamsKey.pd_id, pd_id);
					IntentUtil.activityForward(mActivity,
							DK_PD_DetailActivity.class, bundle, false);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View view) {
		Bundle bundle = new Bundle();
		switch (view.getId()) {
		case R.id.city_ll:
			// 选择城市
			bundle.putString(ParamsKey.cur_city_name, mGPSInfo.getCurCityNAme());
			IntentUtil.activityForward(mActivity, ChooseCityActivity.class,
					bundle, false);
			break;
		case R.id.head_right_ll:
			if (StringUtil.checkStr(UserData.userId)) {
				mContext.mOpenImUtil.markReaded(true, "");
				tips_text.setVisibility(View.INVISIBLE);
				mContext.mOpenImUtil.openConversationList(mActivity);
			} else {
				IntentUtil.activityForward(mActivity, LoginActivity.class,
						null, false);
			}
			break;
		case R.id.more_hot_ll:
			// 更多 -现金分期
			bundle.putInt(ParamsKey.companyType, 2);
			bundle.putInt(ParamsKey.weight, 1);
			bundle.putString(ParamsKey.head_name, "现金分期");
			IntentUtil.activityForward(mActivity,
					HotDaikuanCashInstallmentsActivity.class, bundle, false);
			break;
		case R.id.company_apply_ll:
			// 企业贷
			// bundle.putInt(ParamsKey.daikuan_type, 1);
			// IntentUtil.activityForward(mActivity, ApplyLoanActivity.class,
			// bundle, false);
			send_apply_loan_broad(ParamsKey.company_daikuan_type, 0);
			break;
		case R.id.person_apply_ll:
			// 个人贷
			// bundle.putInt(ParamsKey.daikuan_type, 0);
			send_apply_loan_broad(ParamsKey.person_daikuan_type, 0);
			// IntentUtil.activityForward(mActivity, ApplyLoanActivity.class,
			// bundle, false);
			break;
		case R.id.bottom_apply_text:
			send_apply_loan_broad(ParamsKey.person_daikuan_type, 0);
			break;
		default:
			break;
		}
	}

	/*
	 * 广播通知跳转
	 */
	private void send_more_consult_broad() {
		Intent intent = new Intent(ActionString.click_more_consultant_action);
		mActivity.sendBroadcast(intent);
	}

	/*
	 * 记分牌申请贷款
	 */
	private void send_apply_loan_broad(int userType, int loanCategory ) {
		LogUtil.d(TAG, "申请贷款==userType is " + userType);
		Intent intent = new Intent(ActionString.click_apply_loan_action);
		intent.putExtra(ParamsKey.userType, userType);
		GlobalFlag.userType = userType;
		//GlobalFlag.loanCategory = loanCategory;
		mActivity.sendBroadcast(intent);
	}

	private void consanlt_chat(int position) {
		if (!StringUtil.checkStr(UserData.userId)) {
			IntentUtil.activityForward(mActivity, LoginActivity.class, null,
					false);
			return;
		}
		if (null == consultArrays || consultArrays.length() <= 0)
			return;
		UmenStatisticsUtil
				.onEvent(mActivity, UmenClickEvevt.home_consuant_chat);
		JSONObject obj = consultArrays.optJSONObject(position);
		// String userId = obj.optString("id");
		String groupId = obj.optString("groupId");
		mContext.mOpenImUtil.openCustomerChat(mActivity, groupId);
	}

	private MyReceiver mMyReceiver;

	private void registerCeiver() {
		mMyReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter(ActionString.choose_city_action);
		mActivity.registerReceiver(mMyReceiver, filter);
	}

	private void unRegisterReceiver() {
		if (null != mMyReceiver) {
			mActivity.unregisterReceiver(mMyReceiver);
			mMyReceiver = null;
		}
	}

	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtil.d(TAG, "onReceive==选择城市" + intent.getAction());
			if (ActionString.choose_city_action.equals(intent.getAction())) {
				Bundle bundle = intent.getExtras();
				Message msg = new Message();
				msg.what = 111;
				msg.obj = bundle;
				mHandler.sendMessage(msg);
			}
		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 111:
			Bundle bundle = (Bundle) msg.obj;
			LogUtil.d(TAG, "handleMessage==bundle is " + bundle);
			city = bundle.getString(ParamsKey.city_name);
			if (StringUtil.checkStr(city))
				city_name_text.setText(city);
			pull_refresh_scrollview.setRefreshing(true);
			// loadData();
			break;
		default:
			break;
		}
		return false;
	}
}
