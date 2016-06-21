package com.utils;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtil {

	public static void hideKeyboard(Activity activity) {
		((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity
		.getCurrentFocus()
		.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public static void showKeyboard(Activity activity,EditText editText) {
		InputMethodManager imm = ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE));
		//显示键盘
		imm.showSoftInput(editText, 0);
	}
	
	public static void hideBoard(Activity activity,ViewGroup contentView){
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(contentView.getWindowToken(), 0);
	}
}
