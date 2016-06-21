package com.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	public MyViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	} 

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	float mDownX , mDownY;
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getX();
			mDownY = ev.getY();
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			if(Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY))
			{
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			else 
			{
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			getParent().requestDisallowInterceptTouchEvent(false);
			break; 
		}
		
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void setCurrentItem(int item) {
		item = getOffsetAmount() + (item % getAdapter().getCount());
		super.setCurrentItem(item);
	}
	
	/**
	 * 从0开始向左可以滑动的次数
	 * @return
	 */
	private int getOffsetAmount() {
		if (getAdapter() instanceof InfiniteLoopViewPagerAdapter) {
			InfiniteLoopViewPagerAdapter infiniteAdapter = (InfiniteLoopViewPagerAdapter) getAdapter();
			// 允许向前滚动100000次
			return infiniteAdapter.getRealCount() * 100000;
		} else {
			return 0;
		}
	}
}
