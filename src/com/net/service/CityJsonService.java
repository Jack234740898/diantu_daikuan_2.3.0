package com.net.service;

import android.content.Context;

import com.constants.InterfaceParams;
import com.utils.JSONUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CityJsonService extends BaseJSONService{

	private static final String TAG = "CityJsonService";
	public CityJsonService(Context con) {
		super(con);
	}
	
	/*
	 * 城市列表
	 * http://test.daigj.com/api/adr/option/city
	 */
	public JSONObject option_city(){
		String params = InterfaceParams.option_city;
		mNeedCach = false;
		JSONObject jsonObj = getJSON(null, params, null, true);
		if(null!=jsonObj)
			return jsonObj.optJSONObject("data");
		return null;
	}
	
	/*
	 * province列表
	 * http://test2.daigj.com/api/adr/option/province
	 */
	public ArrayList<JSONObject> option_province(){
		String params = InterfaceParams.option_province;
		JSONObject jsonObj = getJSON(null, params, null, true);
		if(null!=jsonObj)
			jsonObj = jsonObj.optJSONObject("data");
		if(null!=jsonObj)
			return JSONUtil.arrayToList(jsonObj.optJSONArray("provinceList"));
		return null;
	}
	
	/*
	 * university列表
	 * http://test2.daigj.com/api/adr/option/province/university
	 */
	public ArrayList<JSONObject> option_province_university(int provinceId){
		String params = InterfaceParams.option_province_university;
		Map<String,String> mapParams = new HashMap<String, String>();
		mapParams.put("provinceId", String.valueOf(provinceId));
		mNeedCach = false;
		JSONObject jsonObj = getJSON(null, params, mapParams, true);
		if(null!=jsonObj)
			jsonObj = jsonObj.optJSONObject("data");
		if(null!=jsonObj)
			return JSONUtil.arrayToList(jsonObj.optJSONArray("universityList"));
		return null;
	}
} 
