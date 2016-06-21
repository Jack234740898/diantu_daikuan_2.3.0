package com.utils;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/*
 * 贷款 额度，期限预估
 */
public class EvaluateUtil {

	private Activity mActivity;
	/*private String[] salaryName={"1千","5千","1万","2万","5万","10万","20万","50万","100万","1000万"};
	private String[] salaryValue={"1000","5000","10000","20000","50000","100000","200000","500000","1000000","10000000"};
	private String[] termName={"1个月","3个月","6个月","1年","2年","3年","5年","10年"};
	private String[] termValue={"1","3","6","12","24","36","60","120"};*/
	private String[] salary1_name = new String[50];
	private int[] salary1_value = new int[50];
	
	private String[] salary2_name = new String[10];
	private int[] salary2_value = new int[10];
	
	private String[] salary3_name = new String[10];
	private int[] salary3_value = new int[10];
	
	private String[] salary4_name = new String[16];
	private int[] salary4_value = new int[16];
	
	private String[] salary5_name = new String[10];
	private int[] salary5_value = new int[10];
	
	private String[] term1_name = new String[36];
	private int[] term1_value = new int[36];
	
	private String[] term2_name = new String[8];
	private int[] term2_value = new int[8];
	
	private String[] term3_name = new String[5];
	private int[] term3_value = new int[5];
	public EvaluateUtil(Activity activity){
		this.mActivity = activity;
		init_salary1();
		init_salary2();
		init_salary3();
		init_salary4();
		init_salary5();
		init_term1();
		init_term2();
		init_term3();
		initData();
	}
	
	
	private void init_term3() {
		int sum=60;
		for(int i=0;i<5;i++){
			sum+=12;
			String name = (sum/12)+"年";
			term3_name[i] = name;
			term3_value[i] = sum;
		}
	}


	private void init_term2() {
		int sum=36;
		for(int i=0;i<8;i++){
			sum+=3;
			String name = sum+"个月";
			term2_name[i] = name;
			term2_value[i] = sum;
		}
	}


	private void init_term1() {
		for(int i=0;i<36;i++){
			int value=i+1;
			String name = value+"个月";
			term1_name[i] = name;
			term1_value[i] = value;
		}
	}


	private void init_salary5() {
		int sum=1000000;
		for(int i=0;i<9;i++){
			sum+=1000000;
			String name = sum/10000+"万";
			salary5_name[i] = name;
			salary5_value[i] = sum;
		}
	}


	private void init_salary4() {
		int sum=200000;
		for(int i=0;i<16;i++){
			sum+=50000;
			String name = sum/10000+"万";
			salary4_name[i] = name;
			salary4_value[i] = sum;
		}
	}


	private void init_salary3() {
		int sum=100000;
		for(int i=0;i<10;i++){
			sum+=10000;
			String name = sum/10000+"万";
			salary3_name[i] = name;
			salary3_value[i] = sum;
		}
	}


	private void init_salary2() {
		int sum=50000;
		for(int i=0;i<10;i++){
			sum+=5000;
			String name = sum/10000+"万";
			salary2_name[i] = name;
			salary2_value[i] = sum;
		}
	}


	private void init_salary1() {
		for(int i=0;i<50;i++){
			int value = 1000*(i+1);
			String name = (double)value/10000.0d+"万";
			salary1_name[i] = name;
			salary1_value[i] = value;
		}
	}


	private HashMap<Integer,JSONObject> salaryMap;
	private HashMap<Integer,JSONObject> termMap;
	private void  initData(){
		salaryMap = new HashMap<Integer,JSONObject>();
		termMap = new HashMap<Integer,JSONObject>();
		for(int i=0;i<95;i++){
			JSONObject obj = new JSONObject();
			try {
				if(i<50){
					obj.put("name", salary1_name[i]);
					obj.put("value", String.valueOf(salary1_value[i]));
				}else if(i>=50&&i<60){
					int index = i-50;
					obj.put("name", salary2_name[index]);
					obj.put("value", String.valueOf(salary2_value[index]));
				}else if(i>=60&&i<70){
					int index = i-60;
					obj.put("name", salary3_name[index]);
					obj.put("value", String.valueOf(salary3_value[index]));
				}else if(i>=70&&i<86){
					int index = i-70;
					obj.put("name", salary4_name[index]);
					obj.put("value", String.valueOf(salary4_value[index]));
				}else if(i>=86&&i<95){
					int index = i-86;
					obj.put("name", salary5_name[index]);
					obj.put("value", String.valueOf(salary5_value[index]));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			salaryMap.put(i, obj);
		}
		
		//36,8,5
		for(int i=0;i<49;i++){
			JSONObject obj = new JSONObject();
			try {
				if(i<36){
					obj.put("name", term1_name[i]);
					obj.put("value", term1_value[i]);
				}else if(i>=36&&i<44){
					int index = i-36;
					obj.put("name", term2_name[index]);
					obj.put("value", term2_value[index]);
				}else if(i>=44&&i<49){
					int index = i-44;
					obj.put("name", term3_name[index]);
					obj.put("value", term3_value[index]);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			termMap.put(i, obj);
		}
	}
	
	/*
	 * 得到贷款金额值
	 */
	public String getSalaryValue(int key){
		if(salaryMap.containsKey(key)){
			JSONObject obj = salaryMap.get(key);
			if(null!=obj){
				return obj.optString("value");
			}
		}
		return "";	
	}
	/*
	 * 得到贷款金额值
	 */
	public String getSalaryName(int key){
		if(salaryMap.containsKey(key)){
			JSONObject obj = salaryMap.get(key);
			if(null!=obj){
				return obj.optString("name");
			}
		}
		return "";	
	}
	
	/*
	 * 得到贷款金额值
	 */
	public String getTermValue(int key){
		if(termMap.containsKey(key)){
			JSONObject obj = termMap.get(key);
			if(null!=obj){
				return obj.optString("value");
			}
		}
		return "";	
	}
	
	/*
	 * 得到贷款金额值
	 */
	public String getTermName(int key){
		if(termMap.containsKey(key)){
			JSONObject obj = termMap.get(key);
			if(null!=obj){
				return obj.optString("name");
			}
		}
		return "";	
	}
}
