package com.utils;

import android.content.Context;
import android.widget.TextView;

public class FormShow {

	private Context mContext;
	public FormShow(Context con){
		this.mContext = con;
	}
	
	private TextView textview;
	public void setTextView(TextView textview){
		this.textview = textview;
	}
	public void setHuxing_text(int layout_type2, int room, int hall, int bath) {
		String content=null;
		switch (layout_type2) {
		case 1:
			content= "单层住宅"+room+"室"+hall+"厅"+bath+"卫";
			break;
		case 2:
			content= "复式住宅"+room+"室"+hall+"厅"+bath+"卫";
			break;
		case 3:
			content= "别墅";
			break;
		case 4:
			content= "商业";
			break;
		default:
			break;
		}
		if(StringUtil.checkStr(content)){
			if(null!=textview)
				textview.setText(content);
		}
	}
}
