package com.umeng.push.utils;

import android.app.Application;

import com.umeng.message.PushAgent;
import com.umeng.message.proguard.k.e;

import org.json.JSONException;

public class UmengPushAlias {

	private static final String TAG = "UmengPushAlias";

	public static void addAlias(final Application context,
			final String user_id, final String type) {
		PushAgent mPushAgent = PushAgent.getInstance(context);
		if (null != user_id) {
			try {
				mPushAgent.removeAlias(user_id, type);
				mPushAgent.addAlias(user_id, type);
			} catch (e e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*mPushAgent.enable(new IUmengRegisterCallback() {
			@Override
			public void onRegistered(String arg0) {
				Log.d(TAG, "addAlias()==设备token is "+arg0);
			}
		});*/
		//mPushAgent.setExclusiveAlias(user_id, type);
	}

	public static void removeAlias(final Application context,
			final String user_id, final String type) {
		new Thread() {
			public void run() {
				try {// PushAgent.getInstance(context)
					if (null != user_id) {
						PushAgent mPushAgent = PushAgent.getInstance(context);
						mPushAgent.removeAlias(user_id, type);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}
}
