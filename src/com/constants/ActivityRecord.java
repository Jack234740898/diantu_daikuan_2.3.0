package com.constants;

import android.app.Activity;

import java.util.ArrayList;

public class ActivityRecord {

	public static ArrayList<Activity> loginList = new ArrayList<Activity>();
	
	public static ArrayList<Activity> province_school_list = new ArrayList<Activity>();
	
	public static void addActivity(Activity a){
		if(!loginList.contains(a)){
			loginList.add(a);
		}
	}
	public static void activityFinish(ArrayList<Activity> list){
		if(null==list || list.size()==0){
			for(int i=0;i<loginList.size();i++){
				loginList.get(i).finish();
			}
			loginList.clear();
			return;
		}
			
		for(int i=0;i<list.size();i++){
			list.get(i).finish();
		}
		list.clear();
	}
}
