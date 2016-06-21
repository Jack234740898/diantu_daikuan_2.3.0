package com.utils;

import android.widget.EditText;

public class EdittextUtil {

	/**
	 * 2 * 设置EditText是否可编辑 3 * @author com.tiantian 4 * @param editText
	 * 要设置的EditText 5 * @param value 可编辑:true 不可编辑:false 6
	 */
	public static void setEditTextEditable(EditText editText, boolean isEdit) {
		if (isEdit) {
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
		} else {
			editText.setFocusableInTouchMode(false);
			editText.clearFocus();
		}
	}
}
