package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.constants.ActivityRecord;
import com.constants.ParamsKey;
import com.net.service.CityJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.ProvinceAdapter;
import com.ui.view.MyAsyncTask;
import com.utils.IntentUtil;

import org.json.JSONObject;

import java.util.ArrayList;

/*
 * 选择省份
 */
public class ProvinceActivity extends BaseActivity implements OnClickListener,OnItemClickListener{

	@Override
	protected int setContentView() {
		return R.layout.province;
	}

	private CityJsonService mCityService;
	private ArrayList<JSONObject> provinceList;
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mCityService = new CityJsonService(mActivity);
		ActivityRecord.province_school_list.add(mActivity);
		initview();
		loadData();
	}

	private ProvinceAdapter mAdapter;
	private ListView listview;
	private void initview() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("就读学校");
		mHeadView.hideRightBtn();
		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter = new ProvinceAdapter(mActivity));
		listview.setOnItemClickListener(this);
	}
	
	@Override
	protected void loadData() {
		super.loadData();
		new AsyLoaddata(mActivity, "").execute();
	}
	
	private class AsyLoaddata extends MyAsyncTask{

		protected AsyLoaddata(Context context, String title) {
			super(context, title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			return mCityService.option_province();
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if(null==result)
				return;
			provinceList = (ArrayList<JSONObject>) result;
			mAdapter.setData(provinceList);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(null==provinceList||provinceList.size()<=0)
			return;
		JSONObject obj = provinceList.get(position);
		int provinceId = obj.optInt("provinceId");
		Bundle bundle = new Bundle();
		bundle.putInt(ParamsKey.provinceId, provinceId);
		IntentUtil.activityForward(mActivity, SchoolActivity.class, bundle, false);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImg:
			finish();
			break;
		default:
			break;
		}
	}
}
