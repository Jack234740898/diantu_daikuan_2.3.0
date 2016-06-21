package com.img.download;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static boolean isNotEmpty(String str) {
		return str != null && str.trim().length() > 0;
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static boolean isTelNumber(String str) {
		String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";

		Pattern p = Pattern.compile(regExp);

		Matcher m = p.matcher(str);

		return m.find();
	}

	public static boolean isNickName(String str) {
		String regExp = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{1,12}$";

		Pattern p = Pattern.compile(regExp);

		Matcher m = p.matcher(str);

		return m.find();
	}

	public static boolean isNumber(String str) {
		if (isEmpty(str))
			return false;

		String regExp = "^[0-9]{1}$";

		Pattern p = Pattern.compile(regExp);

		Matcher m = p.matcher(str.substring(0, 1));

		return m.find();
	}

	// 国标码和区位码转换常�??
	static final int GB_SP_DIFF = 160;
	// 存放国标�??��汉字不同读音的起始区位码
	static final int[] secPosValueList = { 1601, 1637, 1833, 2078, 2274, 2302,
			2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
			4086, 4390, 4558, 4684, 4925, 5249, 5600 };
	// 存放国标�??��汉字不同读音的起始区位码对应读音
	static final char[] firstLetter = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x',
			'y', 'z' };

	// 获取�??��字符串的拼音�??
	public static String getFirstLetter(String oriStr) {
		String str = oriStr.toLowerCase();
		StringBuffer buffer = new StringBuffer();
		char ch;
		char[] temp;
		for (int i = 0; i < str.length(); i++) { // 依次处理str中每个字�??
			ch = str.charAt(i);
			temp = new char[] { ch };
			byte[] uniCode = new String(temp).getBytes();
			if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉�??
				buffer.append(temp);
			} else {
				buffer.append(convert(uniCode));
			}
		}
		return buffer.toString();
	}

	/**
	 * 获取�??��汉字的拼音首字母�??GB码两个字节分别减�??60，转换成10进制码组合就可以得到区位�??
	 * 例如汉字“你”的GB码是0xC4/0xE3，分别减�??xA0�??60）就�??x24/0x43
	 * 0x24转成10进制就是36�??x43�??7，那么它的区位码就是3667，在对照表中读音为�?n�??
	 */
	static char convert(byte[] bytes) {
		char result = '-';
		int secPosValue = 0;
		int i;
		for (i = 0; i < bytes.length; i++) {
			bytes[i] -= GB_SP_DIFF;
		}
		secPosValue = bytes[0] * 100 + bytes[1];
		for (i = 0; i < 23; i++) {
			if (secPosValue >= secPosValueList[i]
					&& secPosValue < secPosValueList[i + 1]) {
				result = firstLetter[i];
				break;
			}
		}
		return result;
	}

	// 判断字符串的合法�?
	public static boolean checkStr(String str) {
		if (null == str) {
			return false;
		}
		if ("".equals(str)) {
			return false;
		}
		if ("".equals(str.trim())) {
			return false;
		}
		if ("null".equals(str) || "nul".equals(str)) {
			return false;
		}
		return true;
	}

	// 判断电话号码格式是否正确
	public static boolean isMobileNO(String mobiles) {

		/*
		 * Pattern p = Pattern
		 * 
		 * .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		 */

		if (!checkStr(mobiles))
			return false;
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(mobiles);

		return m.matches() & mobiles.trim().length() == 11;

	}

	// 判断密码格式是否正确
	public static boolean isPassword(String password) {
		if (password.length() >= 6) {
			return true;
		}
		if (password.length() <= 13) {
			return true;
		}
		return false;
	}

	/*
	 * 判断字符串是否含有数�?
	 */
	public static boolean isContainsNum(String content) {
		if (!checkStr(content))
			return false;
		boolean isDigit = false;
		for (int i = 0; i < content.length(); i++) { // 循环遍历字符�?
			if (Character.isDigit(content.charAt(i))) { // 用char包装类中的判断数字的方法判断每一个字�?
				isDigit = true;
			}
			if (Character.isLetter(content.charAt(i))) { // 用char包装类中的判断字母的方法判断每一个字�?
				// isLetter = true;
			}
		}
		return isDigit;
	}

	/*
	 * 是否为汉�?
	 */
	public static boolean isChineseChar(String str) {
		if (!checkStr(str))
			return false;
		boolean temp = false;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			temp = true;
		}
		return temp;
	}

	public static String formatMoney(String s) {// , int len
		if (!checkStr(s))
			return "";
		int len = s.length();
		NumberFormat formater = null;
		double num = Double.parseDouble(s);
		if (len == 0) {
			formater = new DecimalFormat("###,###");

		} else {
			StringBuffer buff = new StringBuffer();
			buff.append("###,###.");
			for (int i = 0; i < len; i++) {
				buff.append("#");
			}
			formater = new DecimalFormat(buff.toString());
		}
		String result = formater.format(num);
		/*
		 * if (result.indexOf(".") == -1) { result = "�?" + result + ".00"; }
		 * else { result = "�?" + result; }
		 */
		return result;
	}

	/*
	 * �?查是否为小数
	 */
	public static boolean isPointNum(String value) {
		if (!checkStr(value))
			return false;
		try {
			Float.parseFloat(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/*
	 * �?查是否为小数
	 */
	public static boolean isIntNum(String value) {
		if (!checkStr(value))
			return false;
		if(value.length()>9)
			return false;
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 分割字符�?
	 *
	 * @param line
	 *            原始字符�?
	 * @param seperator
	 *            分隔�?
	 * @return 分割结果
	 */
	public static String[] split(String line, String seperator) {
		ArrayList<String> list = splitToList(line, seperator);
		if (list == null) {
			return null;
		}
		return list.toArray(new String[0]);
	}

	public static ArrayList<String> splitToList(String line, String seperator) {
		if (line == null || seperator == null || seperator.length() == 0) {
			return null;
		}
		ArrayList<String> list = new ArrayList<String>();
		int pos1 = 0;
		int pos2;
		for (;;) {
			pos2 = line.indexOf(seperator, pos1);
			if (pos2 < 0) {
				list.add(line.substring(pos1));
				break;
			}
			list.add(line.substring(pos1, pos2));
			pos1 = pos2 + seperator.length();
		}
		// 去掉末尾的空串，和String.split行为保持�?�?
		for (int i = list.size() - 1; i >= 0 && list.get(i).length() == 0; --i) {
			list.remove(i);
		}
		return list;
	}

	/*
	 * 得到指定范围的随机数
	 */
	public static int getFieldRandom(int min,int max){
		if(min>=max)
			return 0;
		 Random random = new Random();
	     int s = random.nextInt(max)%(max-min+1) + min;
	     return s;
	}
	
	/*
	 * 始终得到�?个四位数
	 */
	public static String getFourDigit(int num){
		String numStr = "0000";
		if(num>=0&&num<10){
			numStr = "000"+num;
		}else if(num>=10&&num<100){
			numStr = "00"+num;
		}else if(num>=100&&num<1000){
			numStr = "0"+num;
		}else if(num>=1000&&num<10000){
			numStr = String.valueOf(num);
		}else if(num>=10000){
			String ss = String.valueOf(num);
			numStr = ss.substring(0, 4);
		}
		return numStr;
	}
	
	/*
	 * 根据时间�?  得到随机的四位数
	 */
	public static String getRandomFourDigit(boolean isPersonLoan){
		Date date = new Date(System.currentTimeMillis());
		//Random r = new Random();
		//int rNum = r.nextInt(2500);
		int num = 2100;
		int h = date.getHours();
		String numStr="0000";
		if(h>=6&&h<=24){
			if(isPersonLoan){
				//2100�?2500
				num = getFieldRandom(2100, 2500);
			}else{
				//1500�?2000
				num = getFieldRandom(1500, 2000);
			}
		}else{
			if(isPersonLoan){
				//200�?500
				num = getFieldRandom(200, 500);
			}else{
				//100�?300
				num = getFieldRandom(100, 300);
			}
		}
		numStr = getFourDigit(num);
		return numStr;
	}
	public static String getHtmlStr(int num,String unit,String content){
		String htmlStr = "<font size='3' color='#FF6600'>"+num+"</font>"
				+"<font size='1' color='#FF6600'>"+unit+"</font>"
				+"<font size='1' color='#858585'>"+content+"</font>";
		return htmlStr;
	}
	
	public static String getHtmlStr1(String content1, int num,String content2){
		String htmlStr = "<font size='1' color='#333333'>"+content1+"</font>"+"<font size='3' color='#FF6600'>"+num+"</font>"
				+"<font size='1' color='#333333'>"+content2+"</font>";
		return htmlStr;
	}
	public static String getHtmlStr2(int num,String content){
		String htmlStr = "<font size='2' color='#FF6600'>"+num+"</font>"
				+"<font size='2' color='#333333'>"+content+"</font>";
		return htmlStr;
	}
	
	/*
	 * 林兴友信贷经理已接单
	 */
	public static String getHtmlStr3(String investName,String content){
		if(checkStr(investName)){
			String htmlStr = "<font  color='#5ca6e2'>"+investName+"</font>"
					+"<font color='#333333'>"+content+"</font>";
			return htmlStr;
		}
		return "信贷"+content;
	}
	
	public static String getRateString(String ss){
		if(!checkStr(ss))
			return null;
		if(!ss.contains("["))
			return null;
		if(!ss.contains("]"))
			return null;
		ss = ss.replace("[", "");
		ss = ss.replace("]", "");
		System.out.println("ss is "+ss);
		String[] array = ss.split(",");
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<array.length;i++){
			String a = array[i];
			String[] arr = a.split(":");
			if(arr.length==2){
				String bb = arr[0]+"个月"+arr[1]+"%";
				sb.append(bb+"�?");
			}
		}
		if(sb.length()>0){
			sb = sb.deleteCharAt(sb.lastIndexOf("�?"));
		}
		return sb.toString();
	}
	
	/*
	 * "[{\"1\":12.7},{\"3\":13.8},{\"60\":14.8}]",
	 */
	public static String getJSONObjectStr(JSONObject obj){
		if(null==obj)
			return "";
		Iterator<String> iterator = obj.keys();
		StringBuilder sb = new StringBuilder();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = obj.opt(key);
			String bb = key+"个月"+value+"%";
			sb.append(bb+"�?");
		}
		if(sb.length()>0){
			sb = sb.deleteCharAt(sb.lastIndexOf("�?"));
		}
		return sb.toString();
	}
}
