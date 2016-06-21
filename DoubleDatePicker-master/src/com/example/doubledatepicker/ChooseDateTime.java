package com.example.doubledatepicker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;

/*
 * 选择日期时间
 */
public class ChooseDateTime {

	private Context mContext;
	public ChooseDateTime(Context context){
		this.mContext = context;
	}
	public interface DateTimeListener{
		public void onDateTime(int year,int month,int day);
	}
	private DateTimeListener mDateTimeListener;
	public void setDateTimeListener(DateTimeListener l){
		this.mDateTimeListener = l;
	}
	/*
	 * 只选择年月
	 */
	public void chooseYearMonth(){
		final Calendar c = Calendar.getInstance();
		new DoubleDatePickerDialog(mContext, DatePickerDialog.THEME_HOLO_LIGHT, new DoubleDatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
					int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
					int endDayOfMonth) {
				if(null!=mDateTimeListener){
					mDateTimeListener.onDateTime(startYear, startMonthOfYear, startDayOfMonth);
				}
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
	}
}
