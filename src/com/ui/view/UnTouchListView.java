package com.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("NewApi")
public class UnTouchListView extends ListView {
	
	private UnTouchListView instance;
	
	public UnTouchListView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public UnTouchListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public UnTouchListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public UnTouchListView(Context context) {
		super(context);
		init();
	}

	private void init(){
		instance = this ;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}
	
	/**
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
	
	Handler handler = new Handler(){
		private int index;
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			index += 1;
			if (index > 5) {
				index = 0;
				instance.setSelection(0);
			}
			// lv.smoothScrollToPosition(index);
			// lv.setSelection(index);
			instance.smoothScrollBy(310, 3000);
		}
		
	};
	
	public void startSroll(){
		Timer autoUpdate = new Timer();
		autoUpdate.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 0, 3000);
	}
	
}
