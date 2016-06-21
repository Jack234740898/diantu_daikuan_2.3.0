package com.ui.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DateDialog implements OnDateSetListener{

	private Activity mActivity;
	public DateDialog(Activity activity){
		this.mActivity = activity;
	}
	
	//private TextView textview;
	private String record_id,field;
	public void setTextView(String field,String record_id){
		this.field = field;
		this.record_id = record_id;
	}
	
	private boolean isUploadDate;
	
	public interface DateListener{
		public void dateResult(Bundle date);
	}
	private DateListener mDateListener;
	public void setDateListener(DateListener l){
		this.mDateListener = l;
	}
	
	public void setUploadDate(boolean isUploadDate){
		this.isUploadDate = isUploadDate;
	}
	private DatePickerDialog dialog;
	public void showDatePickerDialog(){
		Calendar calendar = Calendar.getInstance();
		if(null==dialog)
			dialog = new DatePickerDialog(mActivity, this, 
                calendar.get(Calendar.YEAR), 
                calendar.get(Calendar.MONTH), 
                calendar.get(Calendar.DAY_OF_MONTH)); 
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		String content = "你选择了" + year + "年" +  
                (monthOfYear+1) + "月" + dayOfMonth + "日";
		Bundle bundle = new Bundle();
		bundle.putInt("year", year);
		bundle.putInt("monthOfYear", (monthOfYear+1));
		bundle.putInt("dayOfMonth", dayOfMonth);
		String date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
		if(null!=mDateListener)
			mDateListener.dateResult(bundle);
	}
	
	

}
