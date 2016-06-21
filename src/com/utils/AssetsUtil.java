package com.utils;

import android.content.Context;

import com.constants.FileDir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetsUtil {

	public static String copyApkFromAssets(Context context, String fileName) {  
        try {  
        	File file = new File(FileDir.APP_LOCAL_PATH_DIR+fileName); 
        	if(file.exists())
        		return FileDir.APP_LOCAL_PATH_DIR+fileName; 
            InputStream is = context.getAssets().open(fileName);  
             
            file.createNewFile();  
            FileOutputStream fos = new FileOutputStream(file);  
            byte[] temp = new byte[1024];  
            int i = 0;  
            while ((i = is.read(temp)) > 0) {  
                fos.write(temp, 0, i);  
            }  
            fos.close();  
            is.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return FileDir.APP_LOCAL_PATH_DIR+fileName;  
    }  
}
