package com.db;

import java.util.HashMap;

public class RandomData {

	/*
	 * 信贷经理在线人数与时间对应关系
	 */
	public static HashMap<Integer,Float> getOnlineMap(){
		HashMap<Integer,Float> maps = new HashMap<Integer,Float>();
		maps.put(0, 0.8f);
		maps.put(1, 0.6f);
		maps.put(2, 0.4f);
		maps.put(3, 0.2f);
		maps.put(4, 0.1f);
		maps.put(5, 0.2f);
		maps.put(6, 0.4f);
		maps.put(7, 0.6f);
		maps.put(8, 0.8f);
		maps.put(9, 1.0f);
		maps.put(10, 1.0f);
		maps.put(11, 1.0f);
		maps.put(12, 0.9f);
		maps.put(13, 0.9f);
		maps.put(14, 1.0f);
		maps.put(15, 1.0f);
		maps.put(16, 1.0f);
		maps.put(17, 1.0f);
		maps.put(18, 0.9f);
		maps.put(19, 0.9f);
		maps.put(20, 0.9f);
		maps.put(21, 1.0f);
		maps.put(22, 1.0f);
		maps.put(23, 1.0f);
		return maps;
	}
}
