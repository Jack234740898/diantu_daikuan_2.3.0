package com.utils;

import com.constants.XindaiType;

public class DaikuanUtil {

	/*
	 * 贷款类型
	 */
	public static String getDaikuanTypeStr(int type){
		if(XindaiType.Xindai_Credit == type){
			return "信用贷";
		}else if(XindaiType.Xindai_House == type){
			return "房产抵押";
		}else if(XindaiType.Xindai_Car == type){
			return "车贷";
		}else if(XindaiType.Xindai_Voice == type){
			return "语音贷";
		}
		return null;
	}
}
