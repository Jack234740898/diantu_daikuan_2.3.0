package com.img.download;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

	private static final String TAG = "FileUtil";
	
	/**
	 * 将源文件拷贝到目标文件
	 */
	public static boolean copyFile(File src, File dst) {
		try {
			InputStream in = new FileInputStream(src);
			if (!dst.exists()) {
				dst.createNewFile();
			}
			OutputStream out = new FileOutputStream(dst);
			StreamUtil.copyStream(in, out);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/*
	 * 删掉本地文件数据
	 */
	public static void deleteFiles(String filePath){
		if(!StringUtil.checkStr(filePath)) return ;
		deleteImgDatas(new File(filePath));
	}
	/*
	 * 删掉本地文件数据
	 */
	public static void deleteImgDatas(File file) {
		if(file.isDirectory()){
			File[] files = file.listFiles();
			if(null == files || files.length == 0)return;
			for(int i=0;i<files.length;i++){
				if(files[i].isDirectory()){
					deleteImgDatas(files[i]);
				}else{
					files[i].delete();
				}
			}
		}else{
			file.delete();
		}
	}
	
	public static boolean checkExist(String path){
		if(!StringUtil.checkStr(path)) return false;
		return new File(path).exists();
	}
	
	private String sdpath;

    public String getSdpath() {
        return sdpath;
    }

    public FileUtil() {
        sdpath = Environment.getExternalStorageDirectory() + File.separator;
    }

    public File createSDFile(String filename, String dir) throws IOException {
        File file = new File(sdpath + dir + File.separator + filename);
        file.createNewFile();
        return file;
    }

    public File createSDDir(String dirname) {
        File dir = new File(sdpath + dirname + File.separator);
        dir.mkdir();
        return dir;
    }

    public boolean isFileExists(String filename, String path) {
        File file = new File(sdpath + path + File.separator + filename);
        return file.exists();
    }

    public File write2SDFromInput(String path, String filename, InputStream input) {
        File file = null;
        OutputStream out = null;
        try {
            this.createSDDir(path);
            File f = this.createSDFile(filename, path);
            out = new FileOutputStream(f);
            byte[] buffer = new byte[4 * 1024];
            int tmp = 0;
            while ((tmp = input.read(buffer)) != -1) {
                out.write(buffer, 0, tmp);
            }
            out.flush();
            // System.out.println(path+filename);
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    
    public FileInputStream getFileInputStream(String path, String filename) {
    	File file  = new File (sdpath + path + File.separator + filename);
    	if (file.exists()) {
    		FileInputStream in;
			try {
				in = new FileInputStream(file);
				return in;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return null;
    }
    
    public static boolean isExist(String filePath){
    	if(null!=filePath){
    		return new File(filePath).exists();
    	}
    	return false;
    }
}