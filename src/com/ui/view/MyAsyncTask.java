package com.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sxjs.diantu_daikuan.R;
import com.utils.DialogUtil;
import com.utils.StringUtil;

public abstract class MyAsyncTask extends AsyncTask<Object, Object, Object> {
	protected Dialog pdialog;
	protected MyAsyncTask(Context context,String title) {
		if (StringUtil.isEmpty(title))
			title = context.getResources().getString(
					R.string.data_loading_waiting);
		pdialog = DialogUtil.createLoadingDialog(context, title);
	}

	private boolean isShowLoading=true;
	public void setShowLoading(boolean isShowLoading){
		this.isShowLoading = isShowLoading;
	}
	
	protected void onPreExecute() {
		if(isShowLoading){
			pdialog.show();
			pdialog.setCanceledOnTouchOutside(true);
		}
	}
	@Override
	protected abstract Object doInBackground(Object... params);

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (null != pdialog && pdialog.isShowing()) {
			pdialog.dismiss();
			pdialog = null;
		}
	}
}