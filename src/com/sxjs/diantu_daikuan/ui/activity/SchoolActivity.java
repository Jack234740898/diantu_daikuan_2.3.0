package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.constants.ActivityRecord;
import com.constants.ParamsKey;
import com.db.UserData;
import com.net.service.CityJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.SchoolAdapter;
import com.ui.view.MyAsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;

/*
 * 选择学校
 */
public class SchoolActivity extends BaseActivity implements OnClickListener,OnItemClickListener{

	@Override
	protected int setContentView() {
		return R.layout.school;
	}

	private CityJsonService mCityService;
	private ArrayList<JSONObject> universityList;
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mCityService = new CityJsonService(mActivity);
		ActivityRecord.province_school_list.add(mActivity);
		initparams();
		initview();
		loadData();
	}

	private int provinceId;
	private void initparams() {
		Bundle bundle = getIntent().getExtras();
		if(null==bundle)
			return;
		provinceId = bundle.getInt(ParamsKey.provinceId);
	}

	private SchoolAdapter mAdapter;
	private EditText school_name_edit;
	private ListView listview;
	private void initview() {
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("就读学校");
		mHeadView.hideRightBtn();
		school_name_edit = (EditText) findViewById(R.id.school_name_edit);
		findViewById(R.id.confirm_text).setOnClickListener(this);
		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter = new SchoolAdapter(mActivity));
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
			return mCityService.option_province_university(provinceId);
		}
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if(null==result)
				return;
			universityList = (ArrayList<JSONObject>) result;
			mAdapter.setData(universityList);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(null==universityList||universityList.size()<=0)
			return;
		JSONObject obj = universityList.get(position);
		String universityName = obj.optString("universityName");
		ImageView school_checked = (ImageView) view.findViewById(R.id.school_checked);
		school_checked.setBackgroundResource(R.drawable.school_checked);
		UserData.universityName = universityName;
		ActivityRecord.activityFinish(ActivityRecord.province_school_list);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImg:
			finish();
			break;
		case R.id.confirm_text:
			//确定
			String school_name = school_name_edit.getText().toString();
			UserData.universityName = school_name;
			ActivityRecord.activityFinish(ActivityRecord.province_school_list);
			break;
		default:
			break;
		}
	}
}
