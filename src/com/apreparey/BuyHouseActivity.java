package com.apreparey;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.net.service.BusinessJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.activity.BaseActivity;
import com.utils.ScreenUtil;
import com.utils.ToastUtil;

import org.json.JSONObject;

public class BuyHouseActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {

	private MyCarCreditAdapter mAdapter;
	private ByHouseVpHelper vpHelper;
	private BusinessJsonService mBusinessService;

	@Override
	protected int setContentView() {
		return R.layout.aatest_credit;
	}

	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		startReq = 0;
		page = 1;
		vpHelper = new ByHouseVpHelper(this);
		mBusinessService = new BusinessJsonService(mActivity);
		initView();
	}

	private void initView() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("买房");
		mHeadView.hideRightBtn();
		initPullView(this);
		listview.addHeaderView(vpHelper.getVp());
		setViewMode(Mode.BOTH);
		listview.setDivider(new ColorDrawable(mActivity.getResources()
				.getColor(R.color.page_color)));
		int dividerH = ScreenUtil.dip2px(mActivity, 10.0f);
		listview.setDividerHeight(dividerH);
		listview.setAdapter(mAdapter = new MyCarCreditAdapter(mActivity));
		mAdapter.setImgLoad(mImgLoad);
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
		vpHelper.setData();
		hideLoading();
	}

	@Override
	public void onClick(View v) {
		finish();
	}

	/**
	 * 刷新
	 */
	@Override
	protected void loadData() {
		ToastUtil.showToast(mContext, 0, "fafea", false);
		super.loadData();
		mImgLoad.setNeedLoad(false);
		onRefreshComplete();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	private class AsyLoadata extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Void... params) {
			startReq = 1;
			mBusinessService.setNeedCach(false);
			return mBusinessService.dk_expert(page);
		}

		@Override
		protected void onPostExecute(JSONObject data) {
			onRefreshComplete();
		}
	}

}
