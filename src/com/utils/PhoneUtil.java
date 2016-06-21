package com.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PhoneUtil {

	public static void callPhone(Context context, String tel) {
		if (!StringUtil.checkStr(tel))
			return;
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel)); // 创建一个意图
		context.startActivity(intent);// 开始意图(及拨打电话)
	}

	public static void sendMsg(Context context, String smsBody) {
		Uri smsToUri = Uri.parse("smsto:10000");

		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

		intent.putExtra("sms_body", smsBody);

		context.startActivity(intent);
	}
}
