package com.db.service;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.constants.ParamsValue;
import com.utils.JSONUtil;
import com.utils.LogUtil;
import com.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class PicDataService {

	private static final String TAG = "PicDataService";
	private Activity mActivity;

	private LocalCachService mLocalCachService;
	public PicDataService(Activity activity) {
		this.mActivity = activity;
		mLocalCachService = new LocalCachService(mActivity, "");
	}

	public ArrayList<JSONObject> getAlbumsPic() {
		String picsJson = mLocalCachService.getCachData("localpic");
		if(StringUtil.checkStr(picsJson)){
			try {
				JSONArray picJSONArray = new JSONArray(picsJson);
				ArrayList<JSONObject> list = JSONUtil.arrayToList(picJSONArray);
				if(null!=list && list.size()>0){
					return list;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver mContentResolver = mActivity.getContentResolver();

		// 只查询jpeg和png的图片
		Cursor mCursor = mContentResolver.query(mImageUri, null,
				MediaStore.Images.Media.MIME_TYPE + "=? or "
						+ MediaStore.Images.Media.MIME_TYPE + "=?",
				new String[] { "image/jpeg", "image/png" },
				MediaStore.Images.Media.DEFAULT_SORT_ORDER);
		LogUtil.d(TAG, "getAlbumsPic()==mCursor is "+mCursor);
		if(null==mCursor)
			return null;
		//ArrayList<JSONObject> albumsPicList = new ArrayList<JSONObject>();
		JSONArray jsonArray = new JSONArray();
		int i=-1;
		while (mCursor.moveToNext()) {
			// 获取图片的路径
			String path = mCursor.getString(mCursor
					.getColumnIndex(MediaStore.Images.Media.DATA));

			// 获取该图片的父路径名
			String parentName = new File(path).getParentFile().getName();
			LogUtil.d(TAG, "getAlbumsPic()==path is "+path+",parentName is "+parentName);
			i++;
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("id", i);
				jsonObject.put(ParamsValue.pic, path);
				jsonObject.put(ParamsValue.pic_l, path);
				jsonArray.put(jsonObject);
				//albumsPicList.add(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if(jsonArray.length()>0)
			mLocalCachService.cachData("localpic", jsonArray.toString());
		mCursor.close();
		return JSONUtil.arrayToList(jsonArray);
	}
	
	public void delete(){
		mLocalCachService.clearCachData();
	}
	
	/*
	 * 保存图片上传状态
	 */
	public void savePicUploadState(String key,String result){
		mLocalCachService.cachData(key, result);
	}
	/*
	 * 获取图片上传状态
	 */
	public ArrayList<JSONObject> getPicUploadState(){
		
		return null;
	}
}
