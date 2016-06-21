
package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownLoader {
    private URL url;

    public String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(conn!=null)
             conn.disconnect();
        }
        return sb.toString();
    }

    public int downfile(String urlStr, String path, String filename) {
        InputStream input = null;
        int ret = 0;
        HttpURLConnection conn = null;
        try {
            FileUtils fileUtils = new FileUtils();
            if (fileUtils.isFileExists(filename, path))
                return 1;
            else {
                url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                input = conn.getInputStream();
                File resultFile = fileUtils.write2SDFromInput(path, filename, input);
                if (resultFile == null)
                    return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = -1;
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(conn!=null)
                conn.disconnect();
        }
        return ret;
    }
}
