package com.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.constants.ActionString;
import com.constants.ParamsKey;
public class BaiduGpsUtil {

	private LocationClient mLocationClient = null;
    private static final int UPDATE_TIME = 5000;
    private static int LOCATION_COUTNS = 0;
    private Activity mContext;
    private String currCityName="";
    public BaiduGpsUtil(Activity context){
    	this.mContext = context;
    }
    
    public void init(){
    	mLocationClient = new LocationClient(mContext);
        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        //是否打开GPS
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");       //设置返回值的坐标类型。
        //option.set;LocationClientOption. NetWorkFirst //设置定位优先级
        option.setProdName("电兔抢单"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(UPDATE_TIME);    //设置定时定位的时间间隔。单位毫秒
        mLocationClient.setLocOption(option);
         
        //注册位置监听器
        mLocationClient.registerLocationListener(new BDLocationListener() {
             
            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub
                if (location == null) {
                    return;
                }
                StringBuffer sb = new StringBuffer(256);
                sb.append("Time : ");
                sb.append(location.getTime());
                sb.append("\nError code : ");
                sb.append(location.getLocType());
                sb.append("\nLatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nLontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nRadius : ");
                sb.append(location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation){
                    sb.append("\nSpeed : ");
                    sb.append(location.getSpeed());
                    sb.append("\nSatellite : ");
                    sb.append(location.getSatelliteNumber());
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                    sb.append("\nAddress : ");
                    sb.append(location.getAddrStr());
                }
                LOCATION_COUTNS ++;
                sb.append("\n检查位置更新次数：");
                sb.append(String.valueOf(LOCATION_COUTNS));
                //locationInfoTextView.setText(sb.toString());
            }            
             
        });
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
