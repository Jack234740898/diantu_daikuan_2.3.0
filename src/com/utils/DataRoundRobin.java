package com.utils;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sxjs.diantu_daikuan.R;

/*
 * 轮转效果 viewpager
 */
public class DataRoundRobin {

	private static final String TAG = "DataRoundRobin";
	private static int DIFFER_TIME = 3500;
	private Activity mActivity;
	private ViewPager viewpager;
	private LinearLayout img_point_viewgroup;
	private LayoutInflater mInflater;
	private int currIndex;

	private Handler mHandler;
	private int mDataSize;

	public DataRoundRobin(Activity activity, int size, ViewPager viewpager,
			LinearLayout img_point_viewgroup) {
		this.mActivity = activity;
		this.mDataSize = size;
		this.viewpager = viewpager;
		this.img_point_viewgroup = img_point_viewgroup;
		mInflater = LayoutInflater.from(mActivity);
		mHandler = new Handler();
	}

	private boolean isAutoScroll=true;
	public void setParams(boolean isAutoScroll){
		this.isAutoScroll = isAutoScroll;
	}
	public void showImgData(){
		addImgScrollPoint(mDataSize);
		setPageScrowListener();
		if(isAutoScroll)
			mHandler.postDelayed(loopPlay, DIFFER_TIME);
	}
	/*
	 * 添加左右滑动是下面的小圆圈
	 */
	private void addImgScrollPoint(int size) {
		if (size<=1 )
			return;
		// currIndex = index;
		for (int i = 0; i < size; i++) {
			View view = mInflater.inflate(R.layout.img_scroll_point_item, null);
			ImageView pointimageview = (ImageView) view
					.findViewById(R.id.pointimageview);
			view.setTag(i);
			if (i == currIndex) {
				pointimageview.setImageDrawable(mActivity.getResources()
						.getDrawable(R.drawable.circle_orange_shape));
			} else {
				// pointView.setImageResource(R.drawable.mag_gallary_point);
				pointimageview.setImageDrawable(mActivity.getResources()
						.getDrawable(R.drawable.circle_light_gray_shape));
			}
			img_point_viewgroup.addView(view);
		}
	}

	/*
	 * 滑动时小圆圈的移动
	 */
	private void movePoint(int index) {
		currIndex = index;
		for (int i = 0; i < img_point_viewgroup.getChildCount(); i++) {
			View view = img_point_viewgroup.findViewWithTag(i);
			ImageView im = (ImageView) view.findViewById(R.id.pointimageview);
			if (i == currIndex) {
				im.setImageDrawable(mActivity.getResources().getDrawable(
						R.drawable.circle_orange_shape));
			} else {

				im.setImageDrawable(mActivity.getResources().getDrawable(
						R.drawable.circle_light_gray_shape));
			}
		}
	}

	private boolean isAutoScrow = true;
	private boolean isTouch=false;
	private void setPageScrowListener() {
		viewpager.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isAutoScrow = false;
				isTouch = true;
				//mHandler.removeCallbacks(loopPlay);
				return false;
			}
		});
		viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				//movePoint(arg0);
				int getChildCount = viewpager.getChildCount();
				//LogUtil.d(TAG, "getChildCount is "+getChildCount);
				
				
				//viewpager.removeAllViews();
				//viewpager.removeAllViewsInLayout();
				
				isAutoScrow = true;
				isTouch = false;
				//mHandler.postDelayed(loopPlay, DIFFER_TIME);
//				int position = viewpager.getCurrentItem();
//				if(position>0){
//					if (position == (mDataSize - 1)) {
//						position = 0;
//						viewpager.setCurrentItem(position,true);
//					} else {
//						viewpager.setCurrentItem(++position,true);
//					}
//				}
				
				movePoint(arg0);
			}
		});
	}

	Runnable loopPlay = new Runnable() {

		@Override
		public void run() {
			
			int position = viewpager.getCurrentItem();
			if (position == (mDataSize - 1)) {
				position = 0;
				viewpager.setCurrentItem(position,true);
			} else {
				viewpager.setCurrentItem(++position,true);
			}
			movePoint(position);
			//LogUtil.d(TAG, "position thread" + position + "mDataSize is "+ mDataSize);
			if(isTouch){
				DIFFER_TIME = 5500;
			}else{
				DIFFER_TIME = 3500;
			}
			mHandler.postDelayed(loopPlay, DIFFER_TIME);
		}
	};

}
