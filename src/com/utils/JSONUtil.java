package com.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONUtil {

	/**
	 * 将json对象转换成Map
	 * 
	 * @param jsonObject
	 *            json对象
	 * @return Map对象
	 */
	public static Map<String, Object> jsonObjtoMap(JSONObject jsonObject) {
		if (null == jsonObject)
			return null;
		Map<String, Object> result = new HashMap<String, Object>();
		Iterator<String> iterator = jsonObject.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = jsonObject.opt(key);
			result.put(key, value);
		}
		return result;
	}

	public static ArrayList<JSONObject> jsonObjtoArrayList(JSONObject jsonObject) {
		if (null == jsonObject)
			return null;
		Iterator<String> iterator = jsonObject.keys();
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = jsonObject.optString(key);
			JSONObject jsoObject = new JSONObject();
			try {
				jsoObject.put("id", key);
				jsoObject.put("name", value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			list.add(jsoObject);
		}
		return list;
	}

	/*
	 * 把map转为json
	 */
	public static JSONObject mapToJson(Map<String, String> maps) {
		if (null == maps || maps.isEmpty())
			return null;
		JSONObject jsonObject = new JSONObject();
		for (Map.Entry<String, String> map : maps.entrySet()) {
			try {
				jsonObject.put(map.getKey(), map.getValue());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonObject;
	}

	/*
	 * List转为jsonArray
	 */
	public static JSONArray listToJsonArray(List<String> list) {
		JSONArray array = new JSONArray();
		if (null == list || list.size() <= 0)
			return array;
		for (int i = 0; i < list.size(); i++) {
			array.put(list.get(i));
		}
		return array;
	}

	/*
	 * 将jsonArray转换为List
	 */
	public static ArrayList<JSONObject> arrayToList(JSONArray array) {
		if (null == array || array.length() <= 0)
			return null;
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		for (int i = 0; i < array.length(); i++) {
			list.add(array.optJSONObject(i));
		}
		return list;
	}

	public static String arryToString(JSONArray array) {
		if (null == array)
			return null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length(); i++) {
			String str = array.optString(i);
			sb.append(str + " ");
		}
		return sb.toString();
	}

	public static String arryToStringWithSplit(JSONArray array) {
		if (null == array||array.length()<=0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length(); i++) {
			String str = array.optString(i);
			if(StringUtil.checkStr(str)){
				sb.append(str + ",");
			}
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		return sb.toString();
	}
	
	/*
	 * 城市
	 */
	public static String[] JsonarryToStringArray(JSONArray array) {
		if (null == array)
			return null;
		String[] strArray = new String[array.length()];
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.optJSONObject(i);
			strArray[i] = obj.optString("name");
		}
		return strArray;
	}

	/*
	 * String[] 转为jsonarray
	 */
	public static JSONArray stringArrayToJsonarry(String[] str) {
		if (null == str || str.length <= 0)
			return null;
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < str.length; i++) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("name", str[i]);
				obj.put("value", i + "");
				jsonArray.put(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	/*
	 * JSONObject转为list
	 */
	public static ArrayList<JSONObject> jsonObjtoList(JSONObject jsonObject) {
		if (null == jsonObject)
			return null;
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
		Iterator<String> iterator = jsonObject.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = jsonObject.opt(key);
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put(key, value);
				result.add(jsonObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/*
	 * 抢单设置查询
	 * 将JSONArray转为map
	 */
	public static HashMap<Integer, JSONObject> JSONArrayToMap(JSONArray array){
		if(null==array || array.length()<=0)
			return null;
		HashMap<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
		for(int i=0;i<array.length();i++){
			JSONObject obj = array.optJSONObject(i);
			int type = obj.optInt("type");
			map.put(type, obj);
		}
		return map;
	}
	
	public static String getriskRawValue(JSONArray cityArray,JSONArray userNameArray,JSONArray amountArray,JSONArray incomeArray,
			JSONArray social_security_ageArray,JSONArray creditRecordArray,JSONArray debtTypeArray,JSONArray guaranteeTypeArray) {
		String riskValue = "";
		JSONObject obj = new JSONObject();
		try {
			obj.put("city", cityArray);
			obj.put("userName", userNameArray);
			obj.put("amount", amountArray);
			obj.put("income", incomeArray);
			obj.put("social_security_age", social_security_ageArray);
			obj.put("creditRecord", creditRecordArray);
			obj.put("debtType", debtTypeArray);
			obj.put("guaranteeType", guaranteeTypeArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return obj.toString();
	}

	/*
	 * 针对风险提示的json转换
	 */
	public static JSONArray getStringJSONArray(ArrayList<String> stringSet){
		JSONArray array = new JSONArray();
		if(null!=stringSet&&stringSet.size()>2){
			/*Iterator<String> iterator=stringSet.iterator();
			while(iterator.hasNext()){
				String value = iterator.next();
				if(null!=value){
					array.put(value);
				}
			}*/
			for(int i=0;i<stringSet.size();i++){
				String value = stringSet.get(i);
				if(null!=value){
					array.put(value);
				}
			}
		}
		return array;
	}
	
	/*
	 * 特殊业务数据  转换
	 */
	public static JSONArray getStringArrayToJSONArray(String[] name,String[] value){
		if(null==name||name.length<=0)
			return null;
		if(null==value||value.length<=0)
			return null;
		if(name.length!=value.length)
			return null;
		JSONArray array = new JSONArray();
		for(int i=0;i<name.length;i++){
			String label = name[i];
			String val = value[i];
			JSONObject obj = new JSONObject();
			try {
				obj.put("label", label);
				obj.put("value", val);
				array.put(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return array;
	}
}
