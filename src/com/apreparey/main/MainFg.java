package com.apreparey.main;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.constants.ParamsKey;
import com.db.UserData;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.net.service.BusinessJsonService;
import com.openim.ConversationSampleHelper;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.activity.ChooseCityActivity;
import com.sxjs.diantu_daikuan.ui.activity.LoginActivity;
import com.sxjs.diantu_daikuan.ui.fragment.BaseFragment;
import com.ui.view.MyAsyncTask;
import com.ui.view.UnTouchListView;
import com.utils.GPSInfo;
import com.utils.IntentUtil;
import com.utils.LogUtil;
import com.utils.StringUtil;

import org.json.JSONObject;

public class MainFg extends BaseFragment implements OnClickListener{

	private static final String TAG = "HomeFg";
	private BusinessJsonService mBusinessService;
	private String city;
	private ConversationSampleHelper mConversationHelper;
	private boolean isShowLoading;
	private MainFgTopHelper topHelper ;
	private MainFgBannerVpHelper bannerVpHelper ;
	
	@Override
	protected int setContentView() {
		return R.layout.aatest_mainfg;
	}

	private GPSInfo mGPSInfo;

	@Override
	protected void initView() {
//		mapView = (MapView) findViewById(R.id.map);
//		new MapHelper(mapView,mActivity);
		mConversationHelper = mContext.mOpenImUtil.mConversationHelper;
		mBusinessService = new BusinessJsonService(mActivity);
		mGPSInfo = new GPSInfo(mActivity);
		topHelper = new MainFgTopHelper(mActivity);
		bannerVpHelper = new MainFgBannerVpHelper(mActivity);
		initV();
		city = UserData.local_city;
		isShowLoading = true;
		loadData();
		topHelper.setData();
		bannerVpHelper.setData();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// AnimationUtil.fadeAnimat(mActivity, root, true);
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
	}

	private View body;
	private TextView city_name_text, tips_text;
	private PullToRefreshScrollView pull_refresh_scrollview;

	private void initV() {
		
		findViewById(R.id.city_ll).setOnClickListener(this);
		findViewById(R.id.textview).setVisibility(View.INVISIBLE);
		tips_text = (TextView) findViewById(R.id.tips_text);

		findViewById(R.id.head_right_ll).setOnClickListener(this);
		pull_refresh_scrollview = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		setPullToRefreshScrollView();
		body = findViewById(R.id.body);
		body.setVisibility(View.INVISIBLE);
		city_name_text = (TextView) findViewById(R.id.city_name_text);
		mGPSInfo.setTextView(city_name_text);
		//贷款Gv
		GridView loanGv = (GridView) findViewById(R.id.gridView_loan);
		loanGv.setAdapter(new MainFgGvLoanAdapter(mActivity));
		//垂直滚动LV
		UnTouchListView lv_auto_scroll = (UnTouchListView) findViewById(R.id.lv_auto_scroll);
		lv_auto_scroll.setAdapter(new MyLvAdapter(mActivity));
		lv_auto_scroll.startSroll();
		//工具Gv
		GridView gridView_tools = (GridView) findViewById(R.id.gridView_tools);
		gridView_tools.setAdapter(new MainFgGvLoanAdapter(mActivity));
		//贷款产品推荐 
		ListView lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(new MainFgGvLoanAdapter(mActivity));
		topHelper.setConvertView(body);
		bannerVpHelper.setConvertView(body);
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
						ILoadingLayout loadingLayout = refreshView
								.getLoadingLayoutProxy();
						loadingLayout.setLoadingDrawable(mActivity
								.getResources().getDrawable(
										R.drawable.home_refresh_icon));
						// loadingLayout.set
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
			pull_refresh_scrollview.setBackgroundColor(mActivity.getResources()
					.getColor(R.color.gray));
			body.setVisibility(View.VISIBLE);
			bindviewData((JSONObject) result);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtil.d(TAG, "onActivityCreated()====");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, "onDestroy()====");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtil.d(TAG, "onDestroyView()====");
		mGPSInfo.stopListener();
	}

	/*
	 * 数据绑定
	 */
	private int personNum;
	public void bindviewData(JSONObject data) {
		personNum = data.optInt("personNum");
		if (personNum < 10000) {
			personNum = 12037;
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
		default:
			break;
		}
	}

}