package com.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RepaymentTypeUtil {

	/*
	 * 1、等额本息还款 2、月还息，到期还本 3、其他”
	 * 1分期还款、2到期还款、3随借随还、4其他
	 * 还款类型: 1 先息后本 2 等额本息 3 等额本金
	 */
	public static JSONArray getRepaymentTypeData() {
		JSONArray array = new JSONArray();
		for (int i = 0; i < 3; i++) {
			try {
				JSONObject obj = new JSONObject();
				if (0 == i) {
					obj.put("name", "等额本息还款");
					obj.put("value", "1");
				}else if(1==i){
					obj.put("name", "月还息，到期还本");
					obj.put("value", "2");
				}else if(2==i){
					obj.put("name", "其他");
					obj.put("value", "3");
				}
				array.put(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return array;
	}
	
	public static String getNameStr(JSONArray array,int repaymentType){
		if(null==array||array.length()<=0)
			return "";
		String nameStr="";
		for(int i=0;i<array.length();i++){
			JSONObject obj = array.optJSONObject(i);
			String name= obj.optString("name");
			int value = obj.optInt("value");
			if(value==repaymentType){
				nameStr = name;
			}				
		}
		return nameStr;
	}
}
