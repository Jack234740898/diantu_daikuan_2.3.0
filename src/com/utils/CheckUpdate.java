package com.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;

import com.net.service.UserJsonService;

import org.json.JSONObject;

public class CheckUpdate {

	private Context mContext;
	private UserJsonService mUserJsonService;
	private String ver;

	public CheckUpdate(Context context) {
		this.mContext = context;
		mUserJsonService = new UserJsonService(mContext);
	}

	private boolean isShowLatest=true;
	public void setIsShowLatest(boolean isShowLatest){
		this.isShowLatest = isShowLatest;
	}
	public void checkUpdate() {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					mContext.getPackageName(), 0);
			ver = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (!StringUtil.checkStr(ver)) {
			ToastUtil.showToast(mContext, 0, "获取版本信息失败~", true);
			return;
		}
		new AsyCheckUpdate().execute();
	}

	private class AsyCheckUpdate extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Void... params) {
			return null;//mUserJsonService.check_global_android(ver);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (null == result)
				return;
			JSONObject data = result.optJSONObject("data");
			if (null == data) {

			} else {
				JSONObject check_global_android = data
						.optJSONObject("check_global_android");
				if (null == check_global_android)
					return;
				JSONObject version = check_global_android
						.optJSONObject("version");
				if (null == version)
					return;
				boolean upgrade = version.optBoolean("upgrade");
				boolean force = version.optBoolean("force");
				int ver = version.optInt("ver");
				String title = version.optString("title");
				String features = version.optString("features");
				String url = version.optString("url");
				if(upgrade||force){
					showAlertDialog(title, features, url);
				}else{
					if(isShowLatest)
					ToastUtil.showToast(mContext, 0, "您当前已是最新版本~", true);
				}
			}
		}
	}

	private void showAlertDialog(String title, String features, final String url) {
		new AlertDialog.Builder(mContext)
				.setTitle(title + "")
				.setMessage(features + "")
				.setPositiveButton("立即升级",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Uri uri = Uri.parse(url + "");
								Intent it = new Intent(Intent.ACTION_VIEW, uri);
								it.setClassName("com.android.browser","com.android.browser.BrowserActivity");
								mContext.startActivity(it);
							}
						})
				.setNegativeButton("暂不升级",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).create().show();
	}
}
