package com.sxjs.diantu_daikuan;

public class AppConfig {

	//登陆测试用户：13520286898，密码111111  token: AdMnHfkNM10hXsFEGqE1Qb6GPGU3mEnHi7k=
	public static final int MIN_HEAP_SIZE = 8 * 1024 * 1024;// 最小堆内存
	public static final float TARGET_HEAP_UTILIZATION = 0.75f;// 加强法度堆内存的处理惩罚效力
	public static final int SDCARD_SPACE_UNVALIABLE = 100;// sdcard_space_unvailable
	public static final int SD_AVAIABLE_SIZE = 100;// SD卡大小100Mb

	public static final boolean IS_DEBUG = false;// log日志的开关,true为输出，false为不输出
	public static final boolean  IS_TEST = false;// 网络连接地址的开关，true为测试环境，false为正式环境
	public static String APP_VERSION="";//应用版本号
	public static final String appId="dtdk";// dtyd|dtdk|dtqd
	public static final String app="adr_dtdk";// adr_dtyd|adr_dtdk|adr_dtqd
	public static final int CONNECT_TIME_OUT = 15 * 1000;// 连接超时时间设置
	
	public static final String default_groupId = "dtgw08";
	//public static final String openim_userid_pre = "t_dk_";
	
	public static String channel_name="";//渠道名称
	public static String net_way="";//网络连接方式
	public static String device_model="";//手机品牌型号
	public static String system_version="";//系统版本号
	public static String IMSI="";//运营商

}
