package com.net;

import com.sxjs.diantu_daikuan.AppConfig;



/*
 * 请求数据的url
 */
public class NetUrl {
	/*
	 * 准生产环境 测试环境 + 正式数据
	 */
    private static final String TEST_HOST = "test2.daigj.com/api/adr/";//"192.168.1.100/api/adr/";//
    
    
	/*
	 * 正式服务器IP(域名)
	 */
    private static final String ONLINE_HOST = "www.daigj.com/api/adr/";
	
	
	public static String getHost(){
		if(AppConfig.IS_TEST){
			return "http://"+TEST_HOST;
		}else{
			return "http://"+ONLINE_HOST;
		}
	}
	
	public static final String TOOLS_HOST = "http://test.tools.daigj.com/ios/";//工具查询相关接口的host
	
	public static String getWebSocketUrl(String ticket){
		if(AppConfig.IS_TEST){
			return "ws://"+TEST_HOST+"?ticket="+ticket;
		}else{
			return "ws://"+ONLINE_HOST+"?ticket="+ticket; 
		}
	}
	
}
