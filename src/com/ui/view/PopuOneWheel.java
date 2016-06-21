package com.ui.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.MyWheelViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

/*
 * 贷款期限
 */
public class PopuOneWheel implements OnClickListener,OnWheelChangedListener{

	private static final String TAG = "PopuHuankuanWay";
	private Activity mContext;
	private LayoutInflater mInflater;
	public PopuOneWheel(Activity context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		poup = initPopuptWindow();
		poup.setWidth(LayoutParams.MATCH_PARENT);
		poup.setHeight(LayoutParams.MATCH_PARENT);
	}
	private int value=0;
	private String name="";
	private JSONArray array;
	private int type=0;
	private String title="";
	public void setArray(JSONArray array,int type,String title){
		this.array = array;
		this.type = type;
		this.title = title;
	}
	/*
	 * 监听最终选择的数据
	 */
	public interface ResultListener {
		public void onResult(String name, int value,int type);
	}

	private ResultListener mResultListener;
	public void setResultListener(ResultListener l) {
		this.mResultListener = l;
	}

	private WheelView wheelView1;
	private TextView title_text;
	private PopupWindow initPopuptWindow() {
		View popupWindow_view = LayoutInflater.from(mContext).inflate(
				R.layout.one_wheelview, null);
		popupWindow_view.findViewById(R.id.confirm_text).setOnClickListener(
				this);
		title_text = (TextView) popupWindow_view.findViewById(R.id.title_text);
		wheelView1 = (WheelView) popupWindow_view.findViewById(R.id.wheelView1);
		wheelView1.setWheelBackground(R.drawable.wheel_bg_holo);
		wheelView1.setWheelForeground(R.drawable.wheel_val_holo);
		wheelView1.setShadowColor(0x88FFFFFF, 0x88FFFFFF, 0x88FFFFFF);
		// 使用下面的构造方法
		PopupWindow popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		// 这里注意 必须要有一个背景 ，有了背景后
		// 当你点击对话框外部的时候或者按了返回键的时候对话框就会消失，当然前提是使用的构造函数中Focusable为true
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		return popupWindow;
	}

	PopupWindow poup;

	public void showPopupWindow(View root) {
		title_text.setText(title);
		poup.showAtLocation(root, Gravity.BOTTOM, 0, 0);
		bindViewData();
		final OnClickListener l = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (poup.isShowing())
					poup.dismiss();
			}
		};
	}
	private MyWheelViewAdapter mArrayWheelAdapter1;
	private void bindViewData() {
		mArrayWheelAdapter1 = new MyWheelViewAdapter(mContext, array);
		wheelView1.setViewAdapter(mArrayWheelAdapter1);
		wheelView1.addChangingListener(this);
		wheelView1.setVisibleItems(3);
		wheelView1.setCurrentItem(0);
		
		name = getLabel(array, 0);
		value = getValue(array, 0);
	}

	private void dissPoup() {
		if (null != poup && poup.isShowing())
			poup.dismiss();
	}

	@Override
	public void onClick(View view) {
		dissPoup();
		switch (view.getId()) {
		case R.id.confirm_text:
			// 完成
			confirm();
			break;

		default:
			break;
		}
	}

	private void confirm() {
		if (null != mResultListener)
			mResultListener.onResult(name, value,type);
		dispissDialog();
	}

	private void dispissDialog(){
		if(null!=poup)
			poup.dismiss();
	}
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if(wheel.getId() == R.id.wheelView1){
			value = getValue(array, newValue);
			name = getLabel(array, newValue);
			//textviewMaps1.get(newValue).setTextColor(ColorUtil.getColor(mContext, R.color.title_text_color));
			mArrayWheelAdapter1.setSelection(newValue);
			//mArrayWheelAdapter1.notifyAll();
			wheelView1.invalidateWheel(true);;
			View v = wheelView1.getItemView(newValue);
			TextView wheel_name = (TextView) v.findViewById(R.id.wheel_name);
			if(null!=wheel_name){
				wheel_name.setTextColor(Color.BLACK);
			}
		}
	}
	
	private int getValue(JSONArray array, int index) {
		if (null == array || array.length() <= 0)
			return 0;
		JSONObject obj = array.optJSONObject(index);
		return obj.optInt("value");
	}

	private String getLabel(JSONArray array, int index) {
		if (null == array || array.length() <= 0)
			return "";
		JSONObject obj = array.optJSONObject(index);
		return obj.optString("label");
	}
}
