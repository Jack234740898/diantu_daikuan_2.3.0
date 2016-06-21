package com.utils;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * 订单设置value的值
 */
public class FiledValueUtil {

	public static String getValue(int typeKey,JSONArray array){
		if(null==array)
			return null;
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<array.length();i++){
			JSONObject obj = array.optJSONObject(i);
			int type = obj.optInt("type");
			String label = obj.optString("label");
			if(typeKey==type)
				sb.append(label);
		}
		return sb.toString();
	}
}
