package com.ui.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sxjs.diantu_daikuan.R;

public class ImgScrollPointView {

	private Activity mActivity;
	private LinearLayout img_point_viewgroup;
	private LayoutInflater mInflater;
	private int currIndex;

	public ImgScrollPointView(Activity activity,
			LinearLayout img_point_viewgroup) {
		this.mActivity = activity;
		this.img_point_viewgroup = img_point_viewgroup;
		mInflater = LayoutInflater.from(mActivity);
	}

	/*
	 * 添加左右滑动是下面的小圆圈
	 */
	public void addImgScrollPoint(int size) {
		if (size <= 1)
			return;
		// currIndex = index;
		img_point_viewgroup.removeAllViews();
		for (int i = 0; i < size; i++) {
			View view = mInflater.inflate(R.layout.img_scroll_point_item, null);
			ImageView pointimageview = (ImageView) view
					.findViewById(R.id.pointimageview);
			view.setTag(i);
			if (i == currIndex) {
				pointimageview.setImageDrawable(mActivity.getResources()
						.getDrawable(R.drawable.circle_black_shape));
			} else {
				// pointView.setImageResource(R.drawable.mag_gallary_point);
				pointimageview.setImageDrawable(mActivity.getResources()
						.getDrawable(R.drawable.light_gray_circle_shape));
			}
			img_point_viewgroup.addView(view);
		}
	}

	/*
	 * 滑动时小圆圈的移动
	 */
	public void movePoint(int index) {
		currIndex = index;
		for (int i = 0; i < img_point_viewgroup.getChildCount(); i++) {
			View view = img_point_viewgroup.findViewWithTag(i);
			ImageView im = (ImageView) view.findViewById(R.id.pointimageview);
			if (i == currIndex) {
				im.setImageDrawable(mActivity.getResources().getDrawable(
						R.drawable.circle_black_shape));
			} else {
				im.setImageDrawable(mActivity.getResources().getDrawable(
						R.drawable.light_gray_circle_shape));
			}
		}
	}
}
