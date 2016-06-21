package com.sxjs.diantu_daikuan.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.constants.ParamsKey;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.net.service.BusinessJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.HotDaikuanAdapter;
import com.utils.IntentUtil;
import com.utils.ScreenUtil;
import com.utils.StringUtil;
import com.utils.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;

/*
 * 热门贷款|现金分期列表页面
 */
public class HotDaikuanCashInstallmentsActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	private BusinessJsonService mBusinessJsonService;
	private ArrayList<JSONObject> datalist;

	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mBusinessJsonService = new BusinessJsonService(mActivity);
		datalist = new ArrayList<JSONObject>();
		initParams();
		initView();
		loadData();
	}

	private String head_name;
	private int companyType, weight;
	private boolean isFromDaikuan;
	private void initParams() {
		Bundle bundle = getIntent().getExtras();
		if (null == bundle)
			return;
		companyType = bundle.getInt(ParamsKey.companyType);
		weight = bundle.getInt(ParamsKey.weight);
		head_name = bundle.getString(ParamsKey.head_name);
		isFromDaikuan = bundle.getBoolean(ParamsKey.isFromDaikuan);
	}

	private HotDaikuanAdapter mAdapter;
	private TextView title_text;
	private void initView() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("热门贷款");
		if (StringUtil.checkStr(head_name)) {
			mHeadView.setCentreTextView(head_name);
		}
		title_text = (TextView) findViewById(R.id.title_text);
		if(isFromDaikuan){
			title_text.setText("贷款金额：10万  贷款期限：12个月");
		}
		mHeadView.hideRightBtn();
		initPullView(this);
		setViewMode(Mode.BOTH);
		listview.setDividerHeight(ScreenUtil.dip2px(mActivity, 10.0f));
		listview.setDivider(new ColorDrawable(mActivity.getResources().getColor(R.color.page_color)));
		listview.setAdapter(mAdapter = new HotDaikuanAdapter(mActivity));
		mAdapter.setImgLoad(mImgLoad);
		mAdapter.setType(companyType);
		listview.setOnItemClickListener(this);
		listview.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (startReq == 1)
					return;
				int curCount = firstVisibleItem + visibleItemCount;
				int totalCount = mAdapter.getCount();
				if (curCount >= totalCount - 3 && curCount > 7) {
					loadData();
				}
			}
		});
	}

	@Override
	protected void loadData() {
		super.loadData();
		mImgLoad.setNeedLoad(false);
		new AsyLoaddata().execute();
	}

	private class AsyLoaddata extends
			AsyncTask<Void, Void, ArrayList<JSONObject>> {

		@Override
		protected ArrayList<JSONObject> doInBackground(Void... params) {
			startReq=1;
			return mBusinessJsonService.productHot_list(companyType, 1, page);
		}

		@Override
		protected void onPostExecute(ArrayList<JSONObject> result) {
			super.onPostExecute(result);
			mImgLoad.setNeedLoad(true);
			onRefreshComplete();
			if (null == result || result.size() <= 0) {
				if (1 == page) {
					nodataStatus("暂无数据", R.drawable.tu_zi);
				} else {
					if(page>2)
						ToastUtil.showToast(mActivity, 0, "已无更多了哦~", true);
				}
				return;
			}
			startReq = 0;
			if (1 == page) {
				datalist.clear();
			}
			page++;
			hideLoading();
			datalist.addAll(result);
			refresh_data();
		}
	}

	public void refresh_data() {
		mAdapter.setData(datalist);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.empty:
			page = 1;
			pull_listview.setRefreshing(true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long vid) {
		final JSONObject obj = datalist.get(position - 1);
		Bundle bundle = new Bundle();
		int id = obj.optInt("id");
		bundle.putInt(ParamsKey.id, id);
		bundle.putString(ParamsKey.head_name, "现金分期详情");
		IntentUtil.activityForward(mActivity,
				DaikuanProductDetailActivity.class, bundle, false);
	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.hot_daikuan;
	}
}
