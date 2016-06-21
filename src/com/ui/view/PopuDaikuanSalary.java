package com.ui.view;

import android.app.Activity;
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
import com.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

/*
 * 贷款期限
 */
public class PopuDaikuanSalary implements OnClickListener,
		OnWheelChangedListener {

	private static final String TAG = "PopuDaikuanTerm";
	// private LayoutInflater mInflater;
	private WheelView wheelView1, wheelView2;
	private TextView title_text;
	// private HashMap<Integer, TextView> textviewMaps1,textviewMaps2;
	private Activity mContext;

	public PopuDaikuanSalary(Activity context) {
		this.mContext = context;
		// mInflater = LayoutInflater.from(mContext);
	}

	private JSONArray array1, array2;

	public void setData(JSONArray array1, JSONArray array2) {
		this.array1 = array1;
		this.array2 = array2;
	}

	private TextView textview;

	public void setTextView(TextView textview) {
		this.textview = textview;
	}

	private String title;

	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * 监听最终选择的数据
	 */
	public interface ResultListener {
		public void onResult(String minValue, String maxValue);
	}

	private ResultListener mResultListener;

	public void setResultListener(ResultListener l) {
		this.mResultListener = l;
	}

	private PopupWindow initPopuptWindow() {
		View popupWindow_view = LayoutInflater.from(mContext).inflate(
				R.layout.two_wheelview, null);
		popupWindow_view.findViewById(R.id.confirm_text).setOnClickListener(
				this);
		title_text = (TextView) popupWindow_view.findViewById(R.id.title_text);
		wheelView1 = (WheelView) popupWindow_view.findViewById(R.id.wheelView1);
		wheelView2 = (WheelView) popupWindow_view.findViewById(R.id.wheelView2);

		wheelView1.setWheelBackground(R.drawable.wheel_bg_holo);
		wheelView1.setWheelForeground(R.drawable.wheel_val_holo);
		wheelView1.setShadowColor(0x88FFFFFF, 0x88FFFFFF, 0x88FFFFFF);
		wheelView2.setWheelBackground(R.drawable.wheel_bg_holo);
		wheelView2.setWheelForeground(R.drawable.wheel_val_holo);
		wheelView2.setShadowColor(0x88FFFFFF, 0x88FFFFFF, 0x88FFFFFF);
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
		poup = initPopuptWindow();
		// textviewMaps1 = new HashMap<Integer, TextView>();
		// textviewMaps2 = new HashMap<Integer, TextView>();
		poup.setWidth(LayoutParams.MATCH_PARENT);
		poup.setHeight(LayoutParams.MATCH_PARENT);
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

	private MyWheelViewAdapter mArrayWheelAdapter1, mArrayWheelAdapter2;

	private void bindViewData() {
		if (StringUtil.checkStr(title)) {
			title_text.setText(title);
		}
		if(null==array1 || array1.length()<=0){
			return;
		}
		JSONArray newArray1 = new JSONArray();
		for(int i=0;i<array1.length();i++){
			if(i!=array1.length()-1){
				newArray1.put(array1.optJSONObject(i));
			}
		}
		array1 = newArray1;
		mArrayWheelAdapter1 = new MyWheelViewAdapter(mContext, array1);
		
		//mArrayWheelAdapter2 = new MyWheelViewAdapter(mContext, array2);
		// mArrayWheelAdapter1.setTextViewMap1(textviewMaps1);
		// mArrayWheelAdapter1.setTextViewMap2(textviewMaps2);
		// mArrayWheelAdapter2.setItemResource(R.layout.wheel_textview_item);
		// mArrayWheelAdapter2.setItemTextResource(R.id.wheel_name);
		wheelView1.setViewAdapter(mArrayWheelAdapter1);
		//wheelView2.setViewAdapter(mArrayWheelAdapter2);
		double value1=array1.optJSONObject(0).optDouble("value");
		updateWheel2(value1);
		wheelView1.addChangingListener(this);
		wheelView2.addChangingListener(this);
		wheelView1.setVisibleItems(3);
		wheelView2.setVisibleItems(3);
		wheelView1.setCurrentItem(0);
		wheelView2.setCurrentItem(0);
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

	private String minValue = "", maxValue = "";
	private String min_label = "", max_label = "";

	private void confirm() {
		if (!StringUtil.checkStr(minValue)) {
			minValue = getValue(array1, 0);
			min_label = getLabel(array1, 0);
		}
		if (!StringUtil.checkStr(maxValue)) {
			maxValue = getValue(newArray2, 0);
			max_label = getLabel(newArray2, 0);
		}
		if(StringUtil.checkStr(max_label)){
			textview.setText(min_label + "到" + max_label);
		}else{
			textview.setText(min_label+"");
		}
		
		if (null != mResultListener)
			mResultListener.onResult(minValue, maxValue);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if(wheel.getId() == R.id.wheelView1){
			minValue = getValue(array1, newValue);
			min_label = getLabel(array1, newValue);
			//textviewMaps1.get(newValue).setTextColor(ColorUtil.getColor(mContext, R.color.title_text_color));
			mArrayWheelAdapter1.setSelection(newValue);
			//mArrayWheelAdapter1.notifyAll();
			wheelView1.invalidate();
			updateWheel2(Double.parseDouble(minValue));
		}
		if(wheel.getId() == R.id.wheelView2){
			maxValue = getValue(newArray2, newValue);
			max_label = getLabel(newArray2, newValue);
			//textviewMaps2.get(newValue).setTextColor(ColorUtil.getColor(mContext, R.color.title_text_color));
			mArrayWheelAdapter2.setSelection(newValue);
			//mArrayWheelAdapter2.notifyAll();
			wheelView1.invalidate();
		}
	}

	private JSONArray newArray2;
	private void updateWheel2(double value1) {
		newArray2 = new JSONArray();
		for(int i=0;i<array2.length();i++){
			JSONObject obj = array2.optJSONObject(i);
			double value2 = obj.optDouble("value");
			if(value2>value1){
				newArray2.put(obj);
			}
		}
		//array2 = newArray2;
		mArrayWheelAdapter2 = new MyWheelViewAdapter(mContext, newArray2);
		wheelView2.setViewAdapter(mArrayWheelAdapter2);
	}

	private String getValue(JSONArray array, int index) {
		if (null == array || array.length() <= 0)
			return "";
		JSONObject obj = array.optJSONObject(index);
		return obj.optString("value");
	}

	private String getLabel(JSONArray array, int index) {
		if (null == array || array.length() <= 0)
			return "";
		JSONObject obj = array.optJSONObject(index);
		return obj.optString("label");
	}
}
