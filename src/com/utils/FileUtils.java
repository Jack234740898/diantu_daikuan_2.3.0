
package com.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    private String sdpath;

    public String getSdpath() {
        return sdpath;
    }

    public FileUtils() {
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
    /*
     * 取列表文件
     */
    /*
     * public List<Mp3Info> getFilesList(String path) { List<Mp3Info> list = new
     * ArrayList<Mp3Info>(); File file = new File(sdpath+File.separator+path);
     * File[] files = file.listFiles(); for(int i=0;i<files.length;i++) {
     * if(files[i].getName().endsWith("mp3")) { Mp3Info mp3 = new Mp3Info();
     * mp3.setMp3Name(files[i].getName()); mp3.setMp3Size(files[i].length()+"");
     * mp3.setLrcName(files[i].getName().replace(".mp3", ".lrc"));
     * list.add(mp3); } } return list; }
     */
}
