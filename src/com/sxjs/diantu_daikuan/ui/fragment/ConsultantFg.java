package com.sxjs.diantu_daikuan.ui.fragment;

import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.constants.UmenClickEvevt;
import com.db.UserData;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.net.service.BusinessJsonService;
import com.openim.ConversationSampleHelper;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.activity.LoginActivity;
import com.sxjs.diantu_daikuan.ui.adapter.ConsultantListAdapter;
import com.utils.AnimationUtil;
import com.utils.IntentUtil;
import com.utils.JSONUtil;
import com.utils.LogUtil;
import com.utils.StringUtil;
import com.utils.ToastUtil;
import com.utils.UmenStatisticsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Consultant顾问咨询
 */
public class ConsultantFg extends BaseFragment implements OnClickListener,OnItemClickListener{

	private static final String TAG = "ConsultantFg";
	private JSONArray cmsExtendList;
	private ArrayList<JSONObject> datalist;
	private BusinessJsonService mBusinessService;
	
	@Override
	protected int setContentView() {
		return R.layout.consultant_fg;
	}
	
	private ConversationSampleHelper mConversationHelper; 
	@Override
	protected void initView() {
		mConversationHelper = mContext.mOpenImUtil.mConversationHelper;
		datalist = new ArrayList<JSONObject>();
		mBusinessService = new BusinessJsonService(mActivity);
		initV();
		loadData();
	}

	private View root;
	private ListView listview;
	private ConsultantListAdapter mAdapter;
	private void initV() {
		root = findViewById(R.id.root);
		findViewById(R.id.city_ll).setVisibility(View.GONE);
		findViewById(R.id.head_right_ll).setVisibility(View.GONE);
		//hideLeftBtn();
		mHeadView.setCentreTextView("顾问咨询");
		//hideRightBtn();
		initPullView(this);
		setViewMode(Mode.BOTH);
		listview = pull_listview.getRefreshableView();
		listview.setAdapter(mAdapter = new ConsultantListAdapter(mActivity));
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
	}

	@Override
	protected void loadData() {
		super.loadData();
		mImgLoad.setNeedLoad(false);
		new AsyLoadata().execute();
	}
	private class AsyLoadata extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Void... params) {
			startReq=1;
			mBusinessService.setNeedCach(false);
			return mBusinessService.dk_expert(page);
		}
		
		@Override
		protected void onPostExecute(JSONObject data) {
			super.onPostExecute(data);
			mImgLoad.setNeedLoad(true);
			startReq=0;
			onRefreshComplete();
			hideLoading();
			LogUtil.d(TAG, "AsyLoadata==data is "+data);
			if(null==data){
				nodataStatus("暂无顾问",R.drawable.tu_zi);
				return;
			}
				
			cmsExtendList = data.optJSONArray("cmsExtendList");
			JSONObject data2 = data.optJSONObject("data");
			if(null==data2){
				nodataStatus("暂无顾问",R.drawable.tu_zi);
				return;
			}
			ArrayList<JSONObject> list = JSONUtil.arrayToList(data2.optJSONArray("list"));
			if(null==list||list.size()<=0){
				if(1==page){
					nodataStatus("暂无顾问",R.drawable.tu_zi);
				}else{
					if(page>2)
						ToastUtil.showToast(mActivity, 0, "已无更多了哦~", true);
				}
				return;
			}
			if(1==page){
				datalist.clear();
			}
			page++;
			datalist.addAll(list);
			refresh_data();
		}
	}
	
	public void refresh_data() {
		mAdapter.setData(cmsExtendList,datalist);
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_right_ll:
			if(StringUtil.checkStr(UserData.userId)){
				mContext.mOpenImUtil.openConversationList(mActivity);
			}else{
				IntentUtil.activityForward(mActivity, LoginActivity.class, null, false);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		AnimationUtil.fadeAnimat(mActivity, root, true);
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(!StringUtil.checkStr(UserData.userId)){
			IntentUtil.activityForward(mActivity, LoginActivity.class, null, false);
			return;
		}
		if (null == datalist || datalist.size() <= 0)
			return;
		UmenStatisticsUtil.onEvent(mActivity, UmenClickEvevt.list_banner_click);
		int index = position-1;
		if(null!=cmsExtendList){
			index = index-1;
		}
		JSONObject obj = datalist.get(index);
		String userId = obj.optString("id");
		String groupId = obj.optString("groupId");
		//mContext.mOpenImUtil.openNewConversation(mActivity, ParamsKey.openim_userid_pre+userId);
		mContext.mOpenImUtil.openCustomerChat(mActivity,groupId);
	}

}
