package com.sxjs.diantu_daikuan.ui.fragment;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.mobileim.conversation.IYWConversationService;
import com.constants.GlobalFlag;
import com.net.service.BusinessJsonService;
import com.sxjs.diantu_daikuan.AppConfig;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.OrderManagerAdapter;
import com.ui.view.MyAsyncTask;
import com.utils.DialogUtil;

import org.json.JSONObject;

import java.util.ArrayList;

public class OrderManagerFg extends BaseFragment implements
		OnItemClickListener, OnClickListener {

	private ArrayList<JSONObject> dataList;
	private int loanId;

	public OrderManagerFg(int loanId) {
		this.loanId = loanId;
	}

	@Override
	protected int setContentView() {
		return R.layout.order_manager_fg;
	}

	private BusinessJsonService mBusinessService;
	
	@Override
	protected void initView() {
		mBusinessService = new BusinessJsonService(mActivity);
		initV();
		loadData();
	}

	private IYWConversationService conversationService;
	private ListView listview;
	private OrderManagerAdapter mAdapter;
	private View no_order_manager_root;

	private void initV() {
		conversationService = mContext.mOpenImUtil.getIMKit()
				.getConversationService();
		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter = new OrderManagerAdapter(mActivity));
		mAdapter.setYWConversation(conversationService);
		mAdapter.setImgLoad(mImgLoad);
		listview.setOnItemClickListener(this);
		no_order_manager_root = findViewById(R.id.no_order_manager_root);
		no_order_manager_root.setVisibility(View.GONE);
		findViewById(R.id.contact_customer_text).setOnClickListener(this);
		findViewById(R.id.notice_text2).setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		super.loadData();
		new AsyLoadData(mActivity, "").execute();
	}

	private class AsyLoadData extends MyAsyncTask {

		protected AsyLoadData(Context context, String title) {
			super(context, title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			mBusinessService.setNeedCach(false);
			return mBusinessService.dk_loan_listInvestor(loanId);
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			dataList = (ArrayList<JSONObject>) result;
			if (null == dataList || dataList.size() <= 0) {
				// ToastUtil.showToast(mActivity, 0, "暂无信贷经理接你的单", true);
				no_order_manager_root.setVisibility(View.VISIBLE);
				listview.setVisibility(View.GONE);
				return;
			}
			no_order_manager_root.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			mAdapter.setData(dataList);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (null == dataList || dataList.size() <= 0)
			return;
		JSONObject obj = dataList.get(position);
		String userId = obj.optString("userId");
		mContext.mOpenImUtil.openNewConversation(mActivity, userId);
		/*
		 * int type = obj.optInt("type"); if(2==type){//INVSET("出资用户", 2),
		 * EXPERT("电兔顾问", 3),
		 * mContext.mOpenImUtil.openNewConversation(mActivity,
		 * ParamsKey.openim_userid_pre+userId); }else{
		 * mContext.mOpenImUtil.openCustomerChat(mActivity,0); }
		 */

	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(GlobalFlag.scoreFlagMaps.size()>0){
			mAdapter.setscoreFlagMaps(GlobalFlag.scoreFlagMaps);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.notice_text2:
			DialogUtil.showCauseAlertDialog(mActivity);
			break;
		case R.id.contact_customer_text:
			mContext.mOpenImUtil.openCustomerChat(mActivity, AppConfig.default_groupId);
			break;
		default:
			break;
		}
	}

}
