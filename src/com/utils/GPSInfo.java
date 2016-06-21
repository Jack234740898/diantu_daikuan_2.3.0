package com.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.constants.ActionString;
import com.constants.ParamsKey;
import com.db.UserData;
import com.db.service.GPSDataService;

import java.util.HashMap;
import java.util.Map;

public class GPSInfo {

	private static final String TAG = "GPSInfo";
	private Activity mContext;
	private String currCityName="";
	private static final int UPDATE_TIME = 5000;
	
	private GPSDataService mGPSDataService;
	public GPSInfo(Activity context){
		this.mContext = context;
		mGPSDataService = new GPSDataService(mContext);
		myListener = new MyLocationListener();
		mLocationClient = new LocationClient(context.getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开GPS
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(3000);// 设置发起定位请求的间隔时间为3000ms
		//option.disableCache(false);// 禁止启用缓存定位
		//option.setScanSpan(LocationClientOption.MIN_SCAN_SPAN_NETWORK);// 网络定位优先
		option.setScanSpan(UPDATE_TIME); 
		mLocationClient.setLocOption(option);// 使用设置
		mLocationClient.start();// 开启定位SDK
		mLocationClient.requestLocation();// 开始请求位置
	}
	private String loc = null; // 保存定位信息
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener;
	private TextView textview;
	public void setTextView(TextView textview){
		this.textview = textview;
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			LogUtil.d(TAG, "onReceiveLocation()==");
			if (location != null) {
				StringBuffer sb = new StringBuffer(128);// 接受服务返回的缓冲区
				sb.append(location.getCity());// 获得城市
				loc = sb.toString().trim();
				loc = loc.substring(0,loc.length()-1);
				LogUtil.d(TAG, "onReceiveLocation()==loc is "+loc);
				currCityName = loc;
				if(StringUtil.checkStr(currCityName)&&!(currCityName.equals(UserData.local_city))){
					Map<String, String> map = new HashMap<String, String>();
					map.put(ParamsKey.gps_local, currCityName);
					mGPSDataService.saveData(map);
					UserData.local_city = currCityName;
					showDialog();
				}
				stopListener();
			} 
		}
	}

	public String getCurCityNAme(){
		return currCityName;
	}
	
	/**
	 * 停止，减少资源消耗
	 */
	public void stopListener() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();// 关闭定位SDK
			mLocationClient = null;
		}
	}
	
	private AlertDialog mAlertDialog;
	private void showDialog(){
		if(!StringUtil.checkStr(currCityName))
			return;
		mAlertDialog = DialogUtil.showConfirmCancleDialog(mContext, "当前已为您自动定位到"+currCityName, "确定", new ConfirmListener());
	}
	
	private class ConfirmListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			if(null!=textview){
				if(StringUtil.checkStr(currCityName)){
					textview.setText(currCityName+"");
				}
			}
			sendBroadcastCity();
			mAlertDialog.dismiss();
		}
		
	}
	private void sendBroadcastCity(){
		if(!StringUtil.checkStr(currCityName))
			return;
		Intent intent = new Intent(ActionString.choose_city_action);
		Bundle bundle = new Bundle();
		bundle.putString(ParamsKey.city_name, currCityName);
		intent.putExtras(bundle);
		mContext.sendBroadcast(intent);
	}
}
