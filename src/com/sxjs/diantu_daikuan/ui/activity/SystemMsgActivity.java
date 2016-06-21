package com.sxjs.diantu_daikuan.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.net.service.UserJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.SystemMsgAdapter;
import com.utils.LogUtil;

import org.json.JSONObject;

import java.util.ArrayList;

/*
 * 系统消息
 */
public class SystemMsgActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = "SystemMsgActivity";
	private UserJsonService mUserJsonService;
	private ArrayList<JSONObject> dataList;
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mUserJsonService = new UserJsonService(mActivity);
		dataList = new ArrayList<JSONObject>();
		initView();
		loadData();
	}

	private SystemMsgAdapter mAdapter;
	private void initView() {	
		mHeadView.setLeftBtnBg(R.drawable.back_down, this);
		mHeadView.setCentreTextView("系统消息");
		mHeadView.hideRightTextView();
		initPullView(this);
		setViewMode(Mode.BOTH);
		listview.setAdapter(mAdapter = new SystemMsgAdapter(mActivity));
		mAdapter.setImgLoad(mImgLoad);
		listview.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(startReq==1)
					return;
				int curCount = firstVisibleItem+visibleItemCount;
				int totalCount = mAdapter.getCount();
				if(curCount>=totalCount-3&&curCount>5){
					loadData();
				}
			}
		});
	}

	@Override
	protected void loadData() {
		super.loadData();
		mImgLoad.setNeedLoad(false);
		startReq = 1;
		new AsyLoadData().execute();
	}
	private class AsyLoadData extends AsyncTask<Void, Void, ArrayList<JSONObject>>{

		@Override
		protected ArrayList<JSONObject> doInBackground(Void... arg0) {
			return mUserJsonService.message_system(page);
		}
		@Override
		protected void onPostExecute(ArrayList<JSONObject> result) {
			super.onPostExecute(result);
			mImgLoad.setNeedLoad(true);
			startReq = 0;
			onRefreshComplete();
			LogUtil.d(TAG, "result is "+result);
			if(null==result||result.size()<=0){
				if(1==page){
					nodataStatus("暂无系统消息",R.drawable.xiaoxi);
				}
				return;
			}
			if(1==page){
				dataList.clear();
			}
			page++;
			hideLoading();
			dataList.addAll(result);
			mAdapter.setData(dataList);
			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.empty:
			page=1;
			loadData();
			break;
		default:
			break;
		}
	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.system_msg;
	}
}
