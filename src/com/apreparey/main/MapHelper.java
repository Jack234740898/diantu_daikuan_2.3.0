//package com.apreparey.main;
//
//import android.content.Context;
//import android.location.Location;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.AMap.OnMyLocationChangeListener;
//import com.amap.api.maps.AMapUtils;
//import com.amap.api.maps.CameraUpdate;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.AMap.OnCameraChangeListener;
//import com.amap.api.maps.AMap.OnMapLoadedListener;
//import com.amap.api.maps.AMap.OnMarkerClickListener;
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.CameraPosition;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.Marker;
//import com.amap.api.maps.model.MarkerOptions;
//
//public class MapHelper implements AMapLocationListener, OnMarkerClickListener,
//		OnMapLoadedListener, OnCameraChangeListener ,OnMyLocationChangeListener {
//	private MapView mapView;
//	private AMap aMap;
//	private Context mContext;
//
//	public MapHelper(MapView mapView, Context context) {
//		this.aMap = aMap;
//		this.mapView = mapView;
//		mContext = context;
//		init();
//		initLocationListener();
//	}
//
//	/**
//	 * 初始化AMap对象
//	 */
//	private void init() {
//		if (aMap == null) {
//			aMap = mapView.getMap();
//			// addMarkersToMap();
//			aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
//			aMap.setOnMapLoadedListener(this);
//			aMap.setOnCameraChangeListener(this);
//			// UiSettings mUiSettings = aMap.getUiSettings();
//			// // 比例尺是否显示
//			// mUiSettings.setScaleControlsEnabled(true);
//			// // 设置地图默认缩放按钮是否线显示
//			// mUiSettings.setZoomControlsEnabled(false);
//			// // 设置地图默认指南针是否线显示
//			// mUiSettings.setCompassEnabled(false);
//			// // 设置地图默认的定位按钮是否显示
//			// mUiSettings.setMyLocationButtonEnabled(true);
//			// aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位
//			// // 设置地图是否可以手势滑动
//			// mUiSettings.setScrollGesturesEnabled(true);
//			// // 设置地图是否可以手势缩放大小
//			// mUiSettings.setZoomGesturesEnabled(false);
//			// // 设置地图是否可以倾斜
//			// mUiSettings.setTiltGesturesEnabled(false);
//			// // 设置地图是否可以旋转
//			// mUiSettings.setRotateGesturesEnabled(false);
//		}
//	}
//	private AMapLocationClient locationClient = null;
//	private AMapLocationClientOption locationOption = null;
//
//	private void initLocationListener() {
//		locationClient = new AMapLocationClient(mContext);
//		locationOption = new AMapLocationClientOption();
//		// 设置定位模式为高精度模式
//		locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
//		// 设置定位监听
//		locationClient.setLocationListener(this);
//		initOption();
//		// 设置定位参数
//		locationClient.setLocationOption(locationOption);
//		// 启动定位
//		locationClient.startLocation();
//	}
//
//	// 根据控件的选择，重新设置定位参数
//	private void initOption() {
//		// 设置是否需要显示地址信息
//		locationOption.setNeedAddress(true);
//		/**
//		 * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位 注意：只有在高精度模式下的单次定位有效，其他方式无效
//		 */
//		locationOption.setGpsFirst(true);
//		// 设置是否开启缓存
//		// locationOption.setLocationCacheEnable(true);
//		String strInterval = "1000";
//		if (!TextUtils.isEmpty(strInterval)) {
//			// 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
//			locationOption.setInterval(Long.valueOf(strInterval));
//		}
//	}
//
//	@Override
//	public void onLocationChanged(AMapLocation loc) {
//		// TODO Auto-generated method stub 定位回调
//		// 定位回调
//		if (null != loc) {
//			Message msg = mHandler.obtainMessage();
//			msg.obj = loc;
//			msg.what = MSG_LOCATION_FINISH;
//			mHandler.sendMessage(msg);
//		}
//	}
//
//	/**
//	 * 开始定位
//	 */
//	public final static int MSG_LOCATION_START = 0;
//	// MarkerOptions marker;
//	
//	private Marker mMarker ;
//	/**
//	 * 定位完成
//	 */
//	public final static int MSG_LOCATION_FINISH = 1;
//	Handler mHandler = new Handler() {
//		public void dispatchMessage(android.os.Message msg) {
//			switch (msg.what) {
//			// 定位完成
//			case MSG_LOCATION_FINISH:
//				// 停止定位
//				locationClient.stopLocation();
//				// 设置定位到指定位置
//				AMapLocation loc = (AMapLocation) msg.obj;
//				Double geoLat = loc.getLatitude();
//				Double geoLng = loc.getLongitude();
//				LatLng latLng = new LatLng(geoLat, geoLng);
//				// CameraUpdate zoom =
//				// CameraUpdateFactory.newLatLngZoom(Constants.BEIJING,
//				// 15);
//				CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(latLng,
//						15);
//				aMap.moveCamera(zoom);
//				LatLng temp = new LatLng(39.993606, 116.331835);
//				mMarker = aMap.addMarker(new MarkerOptions()
//						.position(temp)
//						.icon(BitmapDescriptorFactory
//								.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))// BitmapDescriptorFactory.HUE_AZURE
//				// .draggable(true);
//				);
//				break;
//			default:
//				break;
//			}
//		};
//	};
//
//	@Override
//	public boolean onMarkerClick(Marker marker) {
//		marker.setIcon(BitmapDescriptorFactory
//				.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//		// growInto(marker);
//		return false;
//	}
//
//	@Override
//	public void onMapLoaded() {
//		// TODO Auto-generated method stub
//	}
//	
//	/**
//	 * 参数就是屏幕中心坐标
//	 */
//	@Override
//	public void onCameraChange(CameraPosition position) {
//	}
//
//	@Override
//	public void onCameraChangeFinish(CameraPosition arg0) {
//
//	}
//
//	@Override
//	public void onMyLocationChange(Location arg0) {
//	}
//
//}
