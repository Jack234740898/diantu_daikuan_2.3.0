package com.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.constants.ParamsKey;
import com.sxjs.diantu_daikuan.R;
import com.umeng.common.message.Log;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;
import com.utils.LogUtil;
import com.utils.StringUtil;

import org.android.agoo.client.BaseConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Developer defined push intent service. Remember to call
 * {@link com.umeng.message.PushAgent#setPushIntentServiceClass(Class)}.
 * 
 * @author lucas
 * 
 */
public class MyPushIntentService extends UmengBaseIntentService {
	private static final String TAG = "MyPushIntentService";
	private NotificationManager mNotificationManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}
	
	@Override
	protected void onMessage(Context context, Intent intent) {
		super.onMessage(context, intent);
		try {
			String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
			if(StringUtil.checkStr(message)){
				UMessage msg = new UMessage(new JSONObject(message));
				Map<String, String> extra = msg.extra;
				LogUtil.d(TAG, "message=" + message);
				LogUtil.d(TAG, "custom=" + msg.custom);
				LogUtil.d(TAG, "extra=" + extra);
				showNotifaction(context,message);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void showNotifaction(Context context,String message) {
		if(!StringUtil.checkStr(message))
			return;
		try {
			JSONObject obj = new JSONObject(message);
			JSONObject body = obj.optJSONObject("body");
			JSONObject extra = obj.optJSONObject("extra");
			if(null==body)
				return;
			String title = body.optString("title");
			String summary = body.optString("text");
			int order_id = extra.optInt("loanId");
			Notification notification = new Notification(R.drawable.app_logo,
					title, System.currentTimeMillis());
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.defaults = Notification.DEFAULT_ALL;
			// onclick
			int id = (int) Math.round(Math.random() * 100);
			Intent intent = new Intent();//context,OrderDetailActivity.class
			Bundle bundle = new Bundle();
			bundle.putInt(ParamsKey.order_id, order_id);
			bundle.putBoolean("isFromPush", true);
			intent.putExtras(bundle);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent contentIntent = PendingIntent.getActivity(this, id,
					intent, PendingIntent.FLAG_CANCEL_CURRENT);
			// Set the info for the views that show in the notification panel.
			notification.setLatestEventInfo(this, title, summary, contentIntent);
			// Send the notification.
			// We use a layout id because it is a unique number. We use it later to
			// cancel.
			mNotificationManager.notify(title.hashCode(), notification);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
