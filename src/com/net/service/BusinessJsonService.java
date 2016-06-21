package com.net.service;

import android.content.Context;

import com.constants.InterfaceParams;
import com.utils.JSONUtil;
import com.utils.LogUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessJsonService extends BaseJSONService{

	private static final String TAG = "BusinessJsonService";
	public BusinessJsonService(Context con) {
		super(con);
	}
	
	/*
	 * 贷款首页
	 * http://test.daigj.com/api/adr/dk/index
	 */
	public JSONObject dk_index(String city){
		String params = InterfaceParams.dk_index;
		HashMap<String, String> paramMaps = new HashMap<String, String>();
		paramMaps.put("city", city);
		//String str = mNetRequService.requestData("POST", params, paramMaps, mNeedCach);
		//String str = mOkHttpClientManager.requestData(null, params, paramMaps, true, false);
		JSONObject jsonObject = getJSON(null, params, paramMaps, true);
		LogUtil.d(TAG, "贷款首页==str is "+jsonObject);
		if(null==jsonObject)
			return null;
		return jsonObject.optJSONObject("data");
	}
	
	/*
	 * 热门贷款|现金分期 详情
	 * productHot/detail
	 */
	public JSONObject productHot_detail(int id){
		String params = InterfaceParams.productHot_detail;
		HashMap<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id+"");
		JSONObject jsonObject = getJSON(null, params, paramMap, true);
		if(null==jsonObject)
			return null;
		JSONObject data =  jsonObject.optJSONObject("data");
		if(null!=data){
			return data.optJSONObject("productHot");
		}
		return null;
	}
	
	public ArrayList<JSONObject> productHot_list(int companyType,int weight,int pageNo){
		String params = InterfaceParams.productHot_list;
		HashMap<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("companyType", companyType+"");
		paramMap.put("weight", weight+"");
		paramMap.put("pageNo", pageNo+"");
		paramMap.put("pageSize", String.valueOf(20));
		JSONObject jsonObject = getJSON(null, params, paramMap, true);
		if(null==jsonObject)
			return null;
		jsonObject = jsonObject.optJSONObject("data");
		if(null!=jsonObject){
			return JSONUtil.arrayToList(jsonObject.optJSONArray("list"));
		}
		return null;
		/*String str = mNetRequService.requestData("POST", params, paramMap, mNeedCach);
		if (!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			JSONObject data = jsonObject.optJSONObject("data");
			if(null!=data){
				return JSONUtil.arrayToList(data.optJSONArray("list"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 顾问列表
	 * http://test.daigj.com/api/adr/dk/expert
	 */
	public JSONObject dk_expert(int pageNo){
		String params = InterfaceParams.dk_expert;
		HashMap<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("pageNo", pageNo+"");
		paramMap.put("pageSize", String.valueOf(20));
		JSONObject jsonObject = getJSON(null, params, paramMap, true);
		if(null==jsonObject)
			return null;
		return jsonObject.optJSONObject("data");
	}
	
	/*
	 * 信贷经理详细信息
	 * http://test.daigj.com/api/adr/dk/user/getInvestUserV2
	 */
	public JSONObject dk_user_getInvestUserV2(int id){
		String params = InterfaceParams.dk_user_getInvestUserV2;
		HashMap<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(id));
		JSONObject jsonObject = getJSON(null, params, paramMap, true);
		if(null==jsonObject)
			return null;
		return jsonObject.optJSONObject("data");
	}
	
	/*
	 * 抢单的信贷经理
	 * http://test.daigj.com/api/adr/dk/loan/listInvestor
	 */
	public ArrayList<JSONObject> dk_loan_listInvestor(int loanId){
		String params = InterfaceParams.dk_loan_listInvestor;
		HashMap<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("loanId", String.valueOf(loanId));
		JSONObject jsonObject = getJSON(null, params, paramMap, true);
		if(null==jsonObject)
			return null;
		JSONObject data =  jsonObject.optJSONObject("data");
		if(null!=data){
			data =  data.optJSONObject("data");
			if(null!=data){
				return JSONUtil.arrayToList(data.optJSONArray("list"));
			}
		}
		return null;
		/*String str = mNetRequService.requestData("POST", params, paramMap, mNeedCach);
		if (!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			JSONObject data =  jsonObject.optJSONObject("data");
			if(null!=data){
				data =  data.optJSONObject("data");
				if(null!=data){
					return JSONUtil.arrayToList(data.optJSONArray("list"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 获取信贷经理
	 * http://test.daigj.com/api/adr/dk/user/getInvestUser
	 */
	public JSONObject dk_user_getInvestUser(int id){
		String params = InterfaceParams.dk_user_getInvestUser;
		HashMap<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(id));
		JSONObject jsonObject = getJSON(null, params, paramMap, true);
		if(null==jsonObject)
			return null;
		JSONObject data =  jsonObject.optJSONObject("data");
		if(null!=data){
			return data.optJSONObject("user");
		}
		return null;
		/*String str = mNetRequService.requestData("POST", params, paramMap, mNeedCach);
		if (!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			JSONObject data =  jsonObject.optJSONObject("data");
			if(null!=data){
				return data.optJSONObject("user");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * 我的贷款详情
	 * http://test.daigj.com/api/adr/dk/loan/detail
	 */
	public JSONObject dk_loan_detail(int loanId){
		String params = InterfaceParams.dk_loan_detail;
		HashMap<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("loanId", String.valueOf(loanId));
		JSONObject jsonObject = getJSON(null, params, paramMap, true);
		if(null==jsonObject)
			return null;
		JSONObject data =  jsonObject.optJSONObject("data");
		if(null!=data){
			return data.optJSONObject("loan");
		}
		return null;
		/*String str = mNetRequService.requestData("POST", params, paramMap, mNeedCach);
		if (!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			JSONObject data =  jsonObject.optJSONObject("data");
			if(null!=data){
				return data.optJSONObject("loan");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
	
	/*
	 * http://test.daigj.com/api/adr/qd/pd/detail
	 */
	public JSONObject qd_pd_detail(int id){
		String params = InterfaceParams.qd_pd_detail;
		HashMap<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(id));
		JSONObject jsonObject = getJSON(null, params, paramMap, true);
		if(null==jsonObject)
			return null;
		return jsonObject.optJSONObject("data");
		/*String str = mNetRequService.requestData("POST", params, paramMap, mNeedCach);
		if (!StringUtil.checkStr(str))
			return null;
		try {
			JSONObject jsonObject = new JSONObject(str);
			JSONObject data =  jsonObject.optJSONObject("data");
			return data;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;*/
	}
} 
