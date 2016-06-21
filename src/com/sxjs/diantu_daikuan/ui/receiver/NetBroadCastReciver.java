package com.sxjs.diantu_daikuan.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

/*
 * 监听网络连接状态的全局广播
 */
public class NetBroadCastReciver extends BroadcastReceiver implements Callback {

	private static final int no_network = -1;
	private static final int mob_connect = 1;
	private static final int wifi_connect = 2;
	private Handler mHandler;

	public NetBroadCastReciver() {
		mHandler = new Handler();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
			ConnectivityManager connectMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectMgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectMgr
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo activeInfo = connectMgr.getActiveNetworkInfo();
			if (null == activeInfo) {
				Message msg = new Message();
				msg.what = no_network;
				mHandler.sendMessage(msg);
			} else if (null != mobNetInfo && mobNetInfo.isConnected()) {
				Message msg = new Message();
				msg.what = mob_connect;
				mHandler.sendMessage(msg);
			} else if (null != wifiNetInfo && wifiNetInfo.isConnected()) {
				Message msg = new Message();
				msg.what = wifi_connect;
				mHandler.sendMessage(msg);
			}
		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case no_network:
			//GlobalFlag.
			break;
		case mob_connect:

			break;
		case wifi_connect:

			break;
		default:
			break;
		}
		return false;
	}

}