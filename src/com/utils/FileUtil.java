package com.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
	
	public static String BASE_DIR = Environment.getExternalStorageDirectory() + "/DOCTOR/";
	public static String BASE_DOCTOR_DIR = "DOCTOR/";
	public static String BASE_RECORD_DIR = "records/";
	
	private static String TEMP_FILE_NAME = "temp.jpg";
	
	public static String IMAGE_FILE_NAME = "faceImage.jpg";
	
	public static File createFile(byte[] bitmapByte) {
		return createFile(bitmapByte, IMAGE_FILE_NAME);
	}
	
	public static File createFile(byte[] bitmapByte, String fileName) {
		BufferedOutputStream stream = null;
        File file = null;
        try {
        	file = new File(BASE_DIR);
        	if (!file.exists())
        		file.mkdir();
        	file = new File(BASE_DIR + fileName);
        	file.createNewFile();
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(bitmapByte);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
	}
	
	public static void checkRecordPath() {
		File file = null;
        try {
        	file = new File(BASE_DIR + BASE_RECORD_DIR);
        	if (!file.exists())
        		file.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/*
	 * 得到应用数据的存储路径
	 */
	public static String getDiskCacheDir(Context context) {  
	    String cachePath;  
	    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {  
	        cachePath = context.getExternalCacheDir().getPath();  
	    } else {  
	        cachePath = context.getCacheDir().getPath();  
	    }  
	    return cachePath;  
	}  
	
	/*
	 * 文件是否存在
	 */
	public static boolean isExist(String file_path){
		if(null==file_path)
			return false;
		return new File(file_path).exists();
	}
}
