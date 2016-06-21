package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.img.download.ImageLoad;

import org.json.JSONObject;

import java.util.ArrayList;

public class MyBaseAdapter extends BaseAdapter {

	protected Context mContext;
	protected LayoutInflater mInflater;
	public MyBaseAdapter(Context con){
		mContext = con;
		mInflater = LayoutInflater.from(mContext);
	}
	protected ArrayList<JSONObject> dataList;
	public void setData(ArrayList<JSONObject> dataList){
		this.dataList = dataList;
	}
	
	
	/*
	 * 图片加载类
	 */
	protected ImageLoad mImgLoad;
	public void setImgLoad(ImageLoad imgLoad){
		this.mImgLoad = imgLoad;
	}
	@Override
	public int getCount() {
		return dataList==null?0:dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return convertView;
	}

}
