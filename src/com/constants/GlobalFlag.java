package com.constants;

import android.widget.TextView;

import java.util.HashMap;

/*
 * 全局静态变量
 */
public class GlobalFlag {
	
	public static String global_token="";
	//public static int xindai_type;
	
	public static TextView tips_text;
	
	//public static String cur_chat_username1;
	
	public static HashMap<String, Integer> unReadCountMaps = new HashMap<String, Integer>();
	
	public static boolean need_choose = true;
	
	public static int userType = 0;
	public static int pd_id = 0;
	
	public static HashMap<Integer, Integer> scoreFlagMaps = new HashMap<Integer, Integer>();
	
	public static boolean have_no_apply_loan = false;
	
	//public static boolean apply_loan_by_pd = false;
	
	public static boolean dk_sucess_back_home = false;
	
	public static String cur_choose_city = "";
	
	public static int loanCategory = 0;
	
	public static String huji_city;
	public static String work_city;
	
	public static String loanCategoryName;
	
	public static int specialStatus =-1;
}
