package com.openim;

import com.sxjs.diantu_daikuan.AppConfig;


public class IMConfig {

	public static final String dgj_APP_KEY ="23250064";
	public static final String dtdk_APP_KEY ="23227021";
	
	public static String getUseridPre(){
		if(AppConfig.IS_TEST){
			return "t_dk_";
		}
		return "dk_";
	}
}
