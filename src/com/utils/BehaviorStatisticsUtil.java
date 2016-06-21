package com.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

/*
 * 用户操作行为统计
 */
public class BehaviorStatisticsUtil {

	/*
	 * 输入项行为统计
	 */
	private static HashSet<String> oriDataSet;
	public static void setViewListener(EditText editText){
		oriDataSet = new HashSet<String>();
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				oriDataSet.add(s.toString());
			}
		});
	}
	
	public static HashSet<String> getOriBehaviorData(){
		return oriDataSet;
	}
}
