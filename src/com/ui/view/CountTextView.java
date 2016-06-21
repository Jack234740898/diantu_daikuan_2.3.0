package com.ui.view;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CountTextView extends TextView {

	// 动画时长 ms
	int duration = 400;
	int number;

	public CountTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void showNumberWithAnimation(int number) {
		// 修改number属性，会调用setNumber方法
		//ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "number",0, number);
		ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "number",0, number);
		objectAnimator.setDuration(duration);
		// 加速器，从慢到快到再到慢
		objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		//去掉动画//objectAnimator.start();
	}

	/*public float getNumber() {
		return number;
	}*/

	public void setNumber(int number) {
		this.number = number;
		setText(String.valueOf(number));
	}
}