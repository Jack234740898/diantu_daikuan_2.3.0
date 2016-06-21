package com.sxjs.diantu_daikuan.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.constants.ActivityRecord;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.img.download.ImageLoad;
import com.sxjs.diantu_daikuan.MyApplication;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.CircularImage;
import com.ui.view.HeadView;
import com.umeng.message.PushAgent;
import com.utils.CheckUpdate;
import com.utils.LogUtil;
import com.utils.NetworkUtil;
import com.utils.StringUtil;
import com.utils.UmenStatisticsUtil;

/*
 * 项目基类
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends FragmentActivity {

	private static final String TAG = "BaseActivity";
	protected FragmentActivity mActivity;
	protected MyApplication mContext;

	protected LayoutInflater mInflater;
	protected ImageLoad mImgLoad;
	//private HomeWatcher mHomeWatcher;
	private boolean from_key_home = false;
	protected int page, startReq;
	protected HeadView mHeadView;
	protected View layout_view;
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		page = 1;
		startReq = 0;
		mActivity = this;
		mContext = (MyApplication) this.getApplicationContext();
		PushAgent.getInstance(mActivity).onAppStart();
		
		mInflater = LayoutInflater.from(mActivity);
		mImgLoad = mContext.mImgLoad;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		// 设置屏幕为竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//from_key_home = false;
		//watchHome();
		onInit(bundle);
		if(!(mActivity instanceof MainHomeActivity)){
			ActivityRecord.addActivity(mActivity);
		}
	}

	/*
	 * 每个activity的onCreate()方法的替代品，对view的初始化操作在该方法里执行
	 */
	protected void onInit(Bundle bundle) {
		setContentView(layout_view = mInflater.inflate(setContentView(), null));
		mHeadView = new HeadView(layout_view);
	}

	/*
	 * 加载布局文件
	 */
	protected abstract int setContentView();
	
	/*
	 * 应用退出的处理
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//mHomeWatcher.stopWatch();
		mImgLoad.clearCachBitmap();
		mImgLoad.resetParamsValue();
		startReq = 0;
	}

	@Override
	public void finish() {
		super.finish();
		/*
		 * overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
		 */
		// (mContext.getActivityManager()).popActivity(this);
	}

	protected void showAlert(CharSequence message) {
		LayoutInflater inflater = getLayoutInflater();
		View customerView = inflater.inflate(R.layout.customer_confirm, null);
		TextView titleTextView = (TextView) customerView
				.findViewById(R.id.confirm_title);
		TextView messageTextView = (TextView) customerView
				.findViewById(R.id.confirm_message);
		titleTextView.setVisibility(View.GONE);
		messageTextView.setText(message);

		AlertDialog.Builder builder = new Builder(this);
		builder.setView(customerView);
		builder.setPositiveButton(R.string.btn_cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						doAlertAction();
					}
				});
		builder.show();
	}

	protected void doConfirmAction() {
	}

	protected void doAlertAction() {
	}

	protected void showToast(String message) {
		if (!StringUtil.checkStr(message))
			return;
		View view = getLayoutInflater().inflate(R.layout.customer_toast, null);
		TextView textView = (TextView) view.findViewById(R.id.toast_message);
		textView.setText(message);

		Toast toast = new Toast(this);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);

		toast.show();
	}

	/*
	 * 下拉上拉刷新 listview
	 */
	protected PullToRefreshListView pull_listview;
	protected ListView listview;

	protected void initPullView(View.OnClickListener listener) {
		pull_listview = (PullToRefreshListView) findViewById(R.id.pull_listview);
		listview = pull_listview.getRefreshableView();
		setEmptyView(listener);
		setLodingView();
		setRefreshListener();
		// setViewMode();
	}

	protected View mEmptyView, progressbar;
	protected TextView loading_txt;
	private CircularImage no_data_img;

	protected void setEmptyView(View.OnClickListener listener) {
		// pull_listview.setEmptyView(mEmptyView = findViewById(R.id.empty));
		mEmptyView = findViewById(R.id.empty);
		loading_txt = (TextView) mEmptyView.findViewById(R.id.loading_txt);
		progressbar = mEmptyView.findViewById(R.id.progressbar);
		no_data_img = (CircularImage) mEmptyView.findViewById(R.id.no_data_img);
		mEmptyView.setOnClickListener(listener);
	}

	private void setLodingView() {
		mEmptyView.setVisibility(View.VISIBLE);
		progressbar.setVisibility(View.VISIBLE);
		loading_txt.setVisibility(View.VISIBLE);
		loading_txt.setText("正在为您努力加载中...");
	}

	protected void setViewMode(Mode mode) {
		pull_listview.setMode(mode);
		if (!NetworkUtil.isNetworkAvailable(mActivity)) {
			progressbar.setVisibility(View.GONE);
			loading_txt.setText("网络连接失败，请下拉重试");
		}
	}

	protected void hideLoading() {
		mEmptyView.setVisibility(View.GONE);
		progressbar.setVisibility(View.GONE);
		loading_txt.setVisibility(View.GONE);
	}

	/*
	 * 无数据时的界面状态
	 */
	protected void nodataStatus(String nodata,int resId) {
		no_data_img.setVisibility(View.VISIBLE);
		if (!NetworkUtil.isNetworkAvailable(mActivity)) {
			no_data_img.setBackgroundResource(R.drawable.wifi);
			loading_txt.setText("网络连接失败，请下拉重试");
		} else {
			no_data_img.setBackgroundResource(resId);
			loading_txt.setText(nodata);
		}

		progressbar.setVisibility(View.GONE);
	}

	/*
	 * 下拉刷新动画消失
	 */
	protected void onRefreshComplete() {
		pull_listview.onRefreshComplete();
	}

	private void setRefreshListener() {
		pull_listview.setOnRefreshListener(new OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// 更新
				String label = DateUtils.formatDateTime(
						mActivity.getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// if (!hasShowdata)
				page = 1;
				loadData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// 分页显示更多数据
				loadData();
			}
		});
	}

	protected void loadData() {
	}

	/*
	 * 上拉刷新处理
	 */
	protected SwipeRefreshLayout swipe_refresh_layout;

	protected void refresh_NB() {
		swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
		if (null == swipe_refresh_layout)
			return;
		/*
		 * swipe_refresh_layout.setColorScheme(R.color.refresh_green_color,
		 * R.color.refresh_orange_color, R.color.refresh_brown_color,
		 * R.color.refresh_sky_blue_color);
		 */
		swipe_refresh_layout.setColorScheme(android.R.color.transparent,
				android.R.color.transparent, android.R.color.transparent,
				android.R.color.transparent);
		swipe_refresh_layout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						page = 1;
						swipe_refresh_layout.setRefreshing(false);
						loadData();
					}
				});
	}

	/*
	 * 分页获取更多数据处理
	 */
	// private ListView listview;
	protected View emptyView, empty;// progressbar

	// private TextView loading_txt;
	protected void setListViewScrolListener() {
		listview = (ListView) findViewById(R.id.listview);
		listview.setEmptyView(emptyView = mInflater.inflate(
				R.layout.loading_layout, null));
		empty = emptyView.findViewById(R.id.empty);
		progressbar = emptyView.findViewById(R.id.progressbar);
		loading_txt = (TextView) emptyView.findViewById(R.id.loading_txt);
		listview.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				LogUtil.d(TAG, "onScroll()==firstVisibleItem is "
						+ firstVisibleItem + ",visibleItemCount is "
						+ visibleItemCount + ",totalItemCount is "
						+ totalItemCount);
				// 预加载
				if (startReq == 1)
					return;
				if ((firstVisibleItem + visibleItemCount + 3) >= totalItemCount
						&& totalItemCount >= 10) {
					startReq = 1;
					LogUtil.d(TAG, "分页加载==page is " + page);
					loadData();
				}
			}
		});
	}

	private void watchHome() {
		/*mHomeWatcher = new HomeWatcher(mActivity);
		mHomeWatcher
				.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
					@Override
					public void onHomePressed() {
						LogUtil.d(TAG, "onHomePressed");
						from_key_home = true;
					}

					@Override
					public void onHomeLongPressed() {
						LogUtil.d(TAG, "onHomeLongPressed");
					}
				});
		mHomeWatcher.startWatch();*/
	}

	private void checkUpdate() {
		CheckUpdate checkUpdate = new CheckUpdate(mActivity);
		checkUpdate.setIsShowLatest(false);
		checkUpdate.checkUpdate();
	}

	public void onResume() {
		super.onResume();
		UmenStatisticsUtil.onResume(mActivity);
		mImgLoad.setNeedLoad(true);
	}

	public void onPause() {
		super.onPause();
		UmenStatisticsUtil.onPause(mActivity);
	}
}
