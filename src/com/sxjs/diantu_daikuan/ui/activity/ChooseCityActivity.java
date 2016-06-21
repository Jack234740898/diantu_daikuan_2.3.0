package com.sxjs.diantu_daikuan.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.constants.ActionString;
import com.constants.GlobalFlag;
import com.constants.ParamsKey;
import com.db.UserData;
import com.db.service.GPSDataService;
import com.net.service.CityJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.CityGridAdapter;
import com.ui.view.MyAsyncTask;
import com.ui.view.MyGridView;
import com.ui.view.MyLetterListView;
import com.ui.view.MyLetterListView.OnTouchingLetterChangedListener;
import com.utils.DialogUtil;
import com.utils.LogUtil;
import com.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
 * 选择城市页面
 */
public class ChooseCityActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	private static final String TAG = "ChooseCityActivity";
	private CityJsonService mCityJsonService;
	private JSONArray hotCities,openCities;
	@Override
	protected void onInit(Bundle bundle) {
		super.onInit(bundle);
		mCityJsonService = new CityJsonService(mActivity);
		initParams();
		initView();
		//AnimationUtil.fadeAnimat(mActivity, root, true);
		loadData();
	}
	
	private int city_type;
	private String currGPSCityName="";
	private boolean apply_city_dialog;
	private void initParams() {
		Bundle b = getIntent().getExtras();
		if(null==b)
			return;
		currGPSCityName = b.getString(ParamsKey.cur_city_name);
		city_type = b.getInt(ParamsKey.city_type);
		apply_city_dialog = b.getBoolean(ParamsKey.apply_city_dialog, false);
	}
	private ListView list_view;
	private MyLetterListView cityLetterListView;
	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;
	private Handler handler;
	private View root;
	private OverlayThread overlayThread;

	private CityAdapter mCityAdapter;
	private void initView() {
		root = findViewById(R.id.root);
		mHeadView.setLeftBtnBg(R.drawable.back_gray, this);
		mHeadView.setCentreTextView("选择城市");
		if(StringUtil.checkStr(currGPSCityName)){
			mHeadView.setCentreTextView("当前城市-"+currGPSCityName);
		}
		mHeadView.hideRightBtn();

		list_view = (ListView) findViewById(R.id.list_view);
		list_view.setAdapter(mCityAdapter = new CityAdapter());
		list_view.setOnItemClickListener(this);
		
		alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		overlayThread = new OverlayThread();
		initOverlay();
		cityLetterListView = (MyLetterListView) findViewById(R.id.cityLetterListView);
		cityLetterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
	}

	@Override
	protected void loadData() {
		super.loadData();
		new AsyLoaddata(mActivity,"").execute();
	}

	private class AsyLoaddata extends MyAsyncTask {

		protected AsyLoaddata(Context context,String title) {
			super(context,title);
		}

		@Override
		protected Object doInBackground(Object... params) {
			mCityJsonService.setNeedCach(true);
			JSONObject obj = mCityJsonService.option_city();
			if (null == obj) {
				mCityJsonService.setNeedCach(false);
				obj = mCityJsonService.option_city();
			}
			return obj;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			//绑定数据
			if(null==result)
				return;
			JSONObject data = (JSONObject) result;
			hotCities = data.optJSONArray("hotCities");
			openCities = data.optJSONArray("openCities");
			LogUtil.d(TAG, "openCities is "+openCities+"hotCities is "+hotCities);
			mCityAdapter.setData();
			mCityAdapter.notifyDataSetChanged();
		}
	}

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			LogUtil.d(TAG, "触摸字母表==s is " + s);
			if(alphaIndexer.containsKey(s)){
				if (alphaIndexer.get(s) != null) {
					int position = alphaIndexer.get(s);
					LogUtil.d(TAG, "触摸字母表==position is " + position);
					int pos=position;
					if(StringUtil.checkStr(currGPSCityName)){
						pos+=2;
					}else{
						pos+=1;
					}
					list_view.setSelection(pos);
					overlay.setText(sections[position]);
					overlay.setVisibility(View.VISIBLE);
					handler.removeCallbacks(overlayThread);
					handler.postDelayed(overlayThread, 1500);
				}
			}
			
		}
	}

	private WindowManager windowManager;
	private TextView overlay;
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);

	}

	private void removeView() {
		if (windowManager == null)
			return;
		windowManager.removeView(overlay);
	}

	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}

	private class CityAdapter extends BaseAdapter {

		public void setData() {
			if(null==openCities||openCities.length()<=0)
				return;
			//alphaIndexer
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[openCities.length()];
			for (int i = 0; i < openCities.length(); i++) {
				JSONObject obj = openCities.optJSONObject(i);
				String key = obj.optString("key");
				alphaIndexer.put(key, i);
				sections[i] = key;
			}
		}

		@Override
		public int getCount() {
			//hotCities,openCities
			int count=0;
			if(StringUtil.checkStr(currGPSCityName))
				count++;
			if(null!=hotCities&&hotCities.length()>0)
				count++;
			return openCities==null?count:count+openCities.length();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(StringUtil.checkStr(currGPSCityName)){
				if(0==position){
					convertView = mInflater.inflate(R.layout.cur_city_item, null);
					TextView cur_city_text = (TextView) convertView.findViewById(R.id.cur_city_text);
					cur_city_text.setText(currGPSCityName);
					cur_city_text.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							sendBroadcastCity(currGPSCityName);
						}
					});
				}else if(1==position){
					//热门
					convertView = showHotcity(convertView);
				}else{
					//全部
					int index = position-2;
					convertView = showAllcity(index,convertView);
				}
			}else{
				//hotCities,openCities
				if(0==position){
					//热门
					convertView = showHotcity(convertView);
				}else{
					//全部
					int index = position-1;
					convertView = showAllcity(index,convertView);
				}
			}
			return convertView;
		}

		private View showHotcity(View convertView) {
			convertView = mInflater.inflate(R.layout.hot_city_item, null);
			MyGridView hot_city_gridview = (MyGridView) convertView.findViewById(R.id.hot_city_gridview);
			hot_city_gridview.setAdapter(new CityGridAdapter(mActivity, hotCities));
			hot_city_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					String name = hotCities.optString(position);
					sendBroadcastCity(name);
				}
			});
			return convertView;
		}
	}
	static final class ViewHolder {
		TextView head_text;
		LinearLayout city_group;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		/*if(R.id.list_view!=parent.getId())
			return;
		String name="";
		if(StringUtil.checkStr(currGPSCityName)){
			if(0==position){
				name = currGPSCityName;
			}else if(position>1){
				int index = position-2;
				name = openCities.optString(index);
			}
		}else {
			if(position>0){
				int index = position-1;
				name = openCities.optString(index);
			}
		}
		sendBroadcastCity(name);*/
	}

	private AlertDialog mDialog;
	private void show_tips_dialog(final String name) {
		String title = "您选择的城市和当前定位城市不同";
		String desc = "您的贷款申请将由"+name+"的经理受理。确定选择城市"+name+"吗？";
		mDialog = DialogUtil.showConfirmCancleDialog2(mActivity, title, desc, "确定", new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendBroadcastCity(name);
				mDialog.dismiss();
			}
		});
	}

	public View showAllcity(int position, View convertView) {
		ViewHolder vh = null;
		if (null == convertView||!(convertView.getTag() instanceof ViewHolder)) {
			vh = new ViewHolder();
			convertView = mInflater.inflate(R.layout.city_item, null);
			vh.head_text = (TextView) convertView
					.findViewById(R.id.head_text);
			vh.city_group = (LinearLayout) convertView.findViewById(R.id.city_group);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		bindViewData(position, vh);
		return convertView;
	}

	private void bindViewData(int position, ViewHolder vh) {
		JSONObject obj = openCities.optJSONObject(position);
		String key = obj.optString("key");
		vh.head_text.setText(key+"");
		final JSONArray value = obj.optJSONArray("value");
		LogUtil.d("bindViewData", "value is "+value);
		if(null!=value){
			vh.city_group.removeAllViews();
			LogUtil.d("bindViewData111", "bindViewData111");
			for(int i=0;i<value.length();i++){
				View view = mInflater.inflate(R.layout.city_group_textview_item, null);
				final TextView city_textview = (TextView) view.findViewById(R.id.city_textview);
				View line_img = view.findViewById(R.id.line_img);
				line_img.setVisibility(View.VISIBLE);
				if(i==(value.length()-1)){
					line_img.setVisibility(View.GONE);
				}
				city_textview.setTag(i);
				String name = value.optString(i);
				city_textview.setText(name);
				city_textview.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						int index = (Integer) city_textview.getTag();
						String name2 = value.optString(index);
						sendBroadcastCity(name2);
					}
				});
				vh.city_group.addView(view);
				LogUtil.d("bindViewData222", "bindViewData222==name is "+name);
			}
		}
	}

	private void sendBroadcastCity(String name){
		LogUtil.d(TAG, "sendBroadcastCity==name is "+name);
		if(!StringUtil.checkStr(name))
			return;
		if(city_type==ParamsKey.huji_city){
			GlobalFlag.huji_city = name;
		}else if(city_type==ParamsKey.work_city){
			GlobalFlag.work_city = name;
		}
		GlobalFlag.cur_choose_city = name;
		GPSDataService mGPSDataService = new GPSDataService(mActivity);
		Map<String, String> map = new HashMap<String, String>();
		map.put(ParamsKey.gps_local, name);
		mGPSDataService.saveData(map);
		UserData.local_city = name;
		Intent intent = new Intent(ActionString.choose_city_action);
		Bundle bundle = new Bundle();
		bundle.putString(ParamsKey.city_name, name);
		intent.putExtras(bundle);
		mActivity.sendBroadcast(intent);
		finish();
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeView();
	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.choose_city;
	}
}
