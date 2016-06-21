package com.utils;

import android.os.Build;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnectionUtil {
	private String url = "http://192.168.1.202/location/list-country";
	private int connectTimeout = 5000;
	private int readTimeout = 5000;

	public HttpURLConnectionUtil(String url) {
		this.url = url;
	}
	
	public HttpURLConnectionUtil(String url, int connectTimeout, int readTimeout) {
		this.url = url;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	private String openConnection(String method, String params) {
		String line = "";
		StringBuffer sb = new StringBuffer();
		HttpURLConnection urlConnection = null;
		DataInputStream dataInputStream = null;
		DataOutputStream dataOutputStream = null;
		
		try {
			URL url = new URL(this.url);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(connectTimeout);
			urlConnection.setReadTimeout(readTimeout);
			if ("POST".equals(method)) {
				urlConnection.setDoOutput(true);
				
				dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
				dataOutputStream.writeUTF(params);
				
				OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
				out.write(params.getBytes());
			}
			
			if(HttpURLConnection.HTTP_OK == urlConnection.getResponseCode())
            {
				dataInputStream = new DataInputStream(urlConnection.getInputStream());
	            return dataInputStream.readUTF();
	            
//            	BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                while ((line = buffer.readLine()) != null) {
//                    sb.append(line);
//                }
//                
//                return sb.toString();
            }
			return "";
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			urlConnection.disconnect();
		}
	}
	
	public String get() {
		return openConnection("GET", null);
	}
	
	public String get(String params) {
		return openConnection("GET", params);
	}

	public String post(String params) {
		return openConnection("POST", params);
	}
	
	public static void main(String[]  args) {
		String url = "http://192.168.1.202/location/list-country";
		HttpURLConnectionUtil httpURLConnectionUtil = new HttpURLConnectionUtil(url);
		System.out.println(httpURLConnectionUtil.get());
		
//		HttpClient client = new DefaultHttpClient(); 
//		HttpGet get = new HttpGet("http://192.168.1.202/location/list-country");
//		try {
//			HttpResponse response = client.execute(get);
//			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
//				System.out.println(new InputStreamReader(response.getEntity().getContent(), HTTP.UTF_8));
//			}
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private void disableConnectionReuseIfNecessary() {
	    // HTTP connection reuse which was buggy pre-froyo
	    if (Integer.parseInt(Build.VERSION.SDK) <= Build.VERSION_CODES.FROYO) {
	        System.setProperty("http.keepAlive", "false");
	    }
	}
}
