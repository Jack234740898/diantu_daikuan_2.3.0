package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;

import org.json.JSONObject;

public class PopuApplyLoanAdapter extends MyBaseAdapter {

	public PopuApplyLoanAdapter(Context con) {
		super(con);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder vh = null;
		if (null == view) {
			vh = new ViewHolder();
			view = mInflater.inflate(R.layout.popu_apply_loan_item, null);
			vh.textview = (TextView) view.findViewById(R.id.textview);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		bindViewData(position, vh);
		return view;
	}

	private void bindViewData(int position, ViewHolder vh) {
		JSONObject obj = dataList.get(position);
		String name = obj.optString("label");
		vh.textview.setText(name + "");
	}

	static final class ViewHolder {
		TextView textview;
	}
}
