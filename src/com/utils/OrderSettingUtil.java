package com.utils;

import com.constants.XindaiType;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderSettingUtil {

	/*
	 * 根据id得到名称
	 */
	public static String getTypeValue1(String repaymentType,JSONArray array){
		if(!StringUtil.checkStr(repaymentType))
			return "";
		if(null==array || array.length()<=0)
			return "";
		String[] ids=null;
		if(repaymentType.contains(",")){
			ids = repaymentType.split(",");
		}else{
			ids = new String[1];
			ids[0]=repaymentType;
		}
		StringBuilder sb = new StringBuilder();
		for(int j=0;j<ids.length;j++){
			String value1 = ids[j];
			for(int i=0;i<array.length();i++){
				JSONObject obj = array.optJSONObject(i);
				String label = obj.optString("label");
				String value = obj.optString("value");
				if(value1.equals(value)){
					sb.append(label+",");
					
				}
			}
		}
		if(sb.length()>0){
			sb = sb.deleteCharAt(sb.lastIndexOf(","));
			return sb.toString();
		}
		return "";
	}
	
	public static String getTypeValue2(String type,JSONArray array){
		if(!StringUtil.checkStr(type))
			return "";
		if(null==array || array.length()<=0)
			return "";
		//特殊处理
		double valueD = Double.parseDouble(type);
		//int valueInt = (int) valueD;
		for(int i=0;i<array.length();i++){
			JSONObject obj = array.optJSONObject(i);
			String label = obj.optString("label");
			double value = obj.optDouble("value");
			//特殊处理
			if(valueD==value){
				return label;
			}
			/*if(String.valueOf(valueInt).equals(value)){
				return label;
			}else if(type.equals(value)){
				return label;
			}*/
		}
		return "";
	}
	
	public static String getXindaiTypeStr(int type){
		String type_value = "";
		if(XindaiType.Xindai_Credit==type){
			type_value = "信用贷";
		}else if(XindaiType.Xindai_House==type){
			type_value = "房产抵押";
		}else if(XindaiType.Xindai_Car==type){
			type_value = "车贷";
		}
		return type_value;
	}
}
