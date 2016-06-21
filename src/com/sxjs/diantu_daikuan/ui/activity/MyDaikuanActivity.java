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

import com.constants.GlobalFlag;
import com.constants.ParamsKey;
import com.db.UserData;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.net.service.UserJsonService;
import com.openim.ConversationSampleHelper;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.MyDaikuanAdapter;
import com.utils.IntentUtil;
import com.utils.ScreenUtil;
import com.utils.StringUtil;

import org.json.JSONObject;

import java.util.ArrayList;

/*
 * 我的贷款
 */
public class MyDaikuanActivity extends BaseActivity implements OnClickListener,OnItemClickListener{

	private UserJsonService mUserJsonService;
	private ArrayList<JSONObject> datalist;
	private ConversationSampleHelper mConversationHelper; 
	
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mConversationHelper = mContext.mOpenImUtil.mConversationHelper;
		mUserJsonService = new UserJsonService(mActivity);
		datalist = new ArrayList<JSONObject>();
		initView();
		loadData();
	}

	private TextView tips_text;
	private MyDaikuanAdapter mAdapter;
	private View no_order_manager_root;
	private void initView() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("我的贷款");
		findViewById(R.id.head_right_ll).setOnClickListener(this);
		no_order_manager_root = findViewById(R.id.no_order_manager_root);
		no_order_manager_root.setVisibility(View.GONE);
		findViewById(R.id.apply_loan_text).setOnClickListener(this);
		no_order_manager_root.setVisibility(View.GONE);
		tips_text = (TextView) findViewById(R.id.tips_text);
		initPullView(this);
		setViewMode(Mode.DISABLED);
		listview.setDivider(new ColorDrawable(mActivity.getResources().getColor(R.color.page_color)));
		int dividerH = ScreenUtil.dip2px(mActivity, 10.0f);
		listview.setDividerHeight(dividerH);
		listview.setAdapter(mAdapter = new MyDaikuanAdapter(mActivity));
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
					//loadData();
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

	private class AsyLoaddata extends AsyncTask<Void, Void, ArrayList<JSONObject>>{

		@Override
		protected ArrayList<JSONObject> doInBackground(Void... params) {
			startReq=1;
			mUserJsonService.setNeedCach(false);
			return mUserJsonService.applyLoan_list();
		}
		@Override
		protected void onPostExecute(ArrayList<JSONObject> result) {
			super.onPostExecute(result);
			mImgLoad.setNeedLoad(true);
			onRefreshComplete();
			hideLoading();
			if(null==result||result.size()<=0){
				if(1==page){
					//nodataStatus("暂无贷款记录",R.drawable.tu_zi);
					listview.setVisibility(View.GONE);
					no_order_manager_root.setVisibility(View.VISIBLE);
				}else{
					//ToastUtil.showToast(mActivity, 0, "已无更多了哦~", true);
				}
				return;
			}
			startReq = 0;
			if(1==page){
				datalist.clear();
			}
			listview.setVisibility(View.VISIBLE);
			no_order_manager_root.setVisibility(View.GONE);
			//page++;
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
		case R.id.head_right_ll:
			if(StringUtil.checkStr(UserData.userId)){
				tips_text.setVisibility(View.INVISIBLE);
				mContext.mOpenImUtil.openConversationList(mActivity);
			}else{
				IntentUtil.activityForward(mActivity, LoginActivity.class, null, false);
			}
			break;
		case R.id.apply_loan_text:
			go_to_apply();
			break;
		default:
			break;
		}
	}

	private void go_to_apply() {
		/*Intent intent = new Intent(ActionString.click_apply_loan_action);
		intent.putExtra(ParamsKey.userType, ParamsKey.person_daikuan_type);
		GlobalFlag.userType = ParamsKey.person_daikuan_type;
		mActivity.sendBroadcast(intent);*/
		GlobalFlag.have_no_apply_loan = true;
		mActivity.finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int index = position-1;
		JSONObject obj = datalist.get(index);
		int status = obj.optInt("status");
		int productId = obj.optInt("productId");
		Bundle bundle = new Bundle();
		bundle.putInt(ParamsKey.loanId, obj.optInt("id"));
		bundle.putInt(ParamsKey.order_status, status);
		bundle.putInt(ParamsKey.pd_id, productId);
		IntentUtil.activityForward(mActivity, ApplyLoanDetailActivity.class, bundle, false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		int total = mConversationHelper.getTotalUnreadCount();
		tips_text.setVisibility(View.INVISIBLE);
		if(total>0){
			tips_text.setVisibility(View.VISIBLE);
			tips_text.setText(String.valueOf(total));
		}
	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.my_daikuan;
	}
}
