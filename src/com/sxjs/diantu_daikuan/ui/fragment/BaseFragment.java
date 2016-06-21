package com.sxjs.diantu_daikuan.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.img.download.ImageLoad;
import com.sxjs.diantu_daikuan.MyApplication;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.CircularImage;
import com.ui.view.HeadView;
import com.utils.LogUtil;
import com.utils.NetworkUtil;

/*
 * 普通FragmentActivity里Fragment的基类
 */
public abstract class BaseFragment extends Fragment{

	private static final String TAG = "BaseFragment";
	protected Activity mActivity;
	protected MyApplication mContext;
	protected View contentView;
	protected View headLayout;
//	protected MapView mapView;
	
	protected LayoutInflater mInflater;
	//protected Handler mHandler = new Handler();
	protected HeadView mHeadView;
	protected ImageLoad mImgLoad;
	protected int startReq,page;
	@SuppressLint("NewApi") 
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
		mContext = (MyApplication) mActivity.getApplication();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mInflater = inflater;
		LogUtil.d(TAG, "BaseFragment==onCreateView（）");
		startReq=0;
		page=1;
		mImgLoad = mContext.mImgLoad;
		mImgLoad.setNeedLoad(true);
		if(contentView == null){
			contentView = inflater.inflate(setContentView(), null);
			mHeadView = new HeadView(contentView);
			//initTitle();
			//StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build()); 
			initView();
//			if( mapView!= null){
//				mapView.onCreate(savedInstanceState);// 此方法必须重写
//			}
		}else{
			ViewParent viewParent = contentView.getParent();
			if(null!=viewParent){
				((ViewGroup)viewParent).removeView(contentView);
			}
		}
		return contentView;
	}
	
	/*
	 * 子类最好重载，对代码的封装
	 */
	protected abstract void initView() ;
	/*
	 * 加载布局文件
	 */
	protected abstract int setContentView();
	
	/*
	 * 加载布局文件
	 */
	protected View setContentView(LayoutInflater inflater, int resId) {
		return contentView = inflater.inflate(resId, null);
	}
	
	/*
	 * 加载布局文件完后找到一个view对象
	 */
	protected View findViewById(int resId) {
		return contentView.findViewById(resId);
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
		mEmptyView.setVisibility(View.VISIBLE);
		no_data_img.setVisibility(View.VISIBLE);
		loading_txt.setVisibility(View.VISIBLE);
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

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mImgLoad.resetParamsValue();
		mImgLoad.clearCachBitmap();
		mImgLoad.setNeedLoad(false);
	}
}
