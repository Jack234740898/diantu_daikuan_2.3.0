package com.utils;

import android.content.Context;
import android.content.Intent;

/*
 * 调用系统分享
 */
public class ShareUtil {

	public static void shareBySystem(Context con,String content){
		Intent shareInt=new Intent(Intent.ACTION_SEND);
        shareInt.setType("text/plain");   
        shareInt.putExtra(Intent.EXTRA_SUBJECT, "选择分享方式");   
        shareInt.putExtra(Intent.EXTRA_TEXT, content);    
        shareInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 

        con.startActivity(shareInt);
	}
}
