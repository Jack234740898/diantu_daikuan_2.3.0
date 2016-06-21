package com.ui.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.PopuApplyLoanAdapter;
import com.utils.DialogUtil;
import com.utils.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * 贷款期限
 */
public class PopuApplyLoan implements OnClickListener,OnItemClickListener {

	private static final String TAG = "PopuApplyLoan";
	private LayoutInflater mInflater;
	private Activity mContext;
	private PopupWindow mPopupWindow;
	public PopuApplyLoan(Activity context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mPopupWindow = initPopuptWindow();
	}

	private JSONArray array;
	private int type;
	public void setData(JSONArray array,int type) {
		this.array = array;
		this.type = type;
	}

	/*
	 * 监听最终选择的数据
	 */
	public interface ResultListener {
		public void onResult(int type,String name, String value);
	}

	private ResultListener mResultListener;
	public void setResultListener(ResultListener l) {
		this.mResultListener = l;
	}

	private PopuApplyLoanAdapter mAdapter;
	private ListView listview;
	private PopupWindow initPopuptWindow() {
		View popupWindow_view = LayoutInflater.from(mContext).inflate(
				R.layout.popu_apply_loan, null);
		popupWindow_view.findViewById(R.id.cancle_text).setOnClickListener(this);
		listview = (ListView) popupWindow_view.findViewById(R.id.listview);
		listview.setAdapter(mAdapter = new PopuApplyLoanAdapter(mContext));
		listview.setOnItemClickListener(this);
		// 使用下面的构造方法
		PopupWindow popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		// 这里注意 必须要有一个背景 ，有了背景后
		// 当你点击对话框外部的时候或者按了返回键的时候对话框就会消失，当然前提是使用的构造函数中Focusable为true
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		return popupWindow;
	}

	public void showPopupWindow(View root) {
		mPopupWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
		bindViewData();
		final OnClickListener l = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPopupWindow.isShowing())
					mPopupWindow.dismiss();
			}
		};
	}

	

	private void bindViewData() {
		mAdapter.setData(JSONUtil.arrayToList(array));
		mAdapter.notifyDataSetChanged();
	}


	private void dissPoup() {
		if (null != mPopupWindow && mPopupWindow.isShowing())
			mPopupWindow.dismiss();
	}

	@Override
	public void onClick(View view) {
		dissPoup();
		switch (view.getId()) {
		case R.id.cancle_text:
			break;

		default:
			break;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(null==array||array.length()<=0)
			return;
		JSONObject obj = array.optJSONObject(position);
		String label = obj.optString("label");
		String value = obj.optString("value");
		if(11==type&&("0".equals(value))){
			show_dialog(label,value);
		}else{
			mResultListener.onResult(type,label, value);
		}
		dissPoup();		
	}

	/*
	 * 弹框提示
	 */
	private AlertDialog mDialog;
	private void show_dialog(final String label,final String value) {
		String title = "您确定不公开电话吗？";
		String desc = "不公开电话，经理可能无法及时联系您。您需要主动关注个人中心贷款进度。";
		mDialog = DialogUtil.showConfirmCancleDialog2(mContext, title,desc, "确定", new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
				mResultListener.onResult(type,label, value);
				mDialog.dismiss();
			}
		});
	}
	
}
