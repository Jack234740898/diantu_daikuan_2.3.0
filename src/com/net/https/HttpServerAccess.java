/**
 * Copyright (c) 2003-2009 Wonders Information Co.,Ltd. All Rights Reserved.
 * 5-6/F, 20 Bldg, 481 Guiping RD. Shanghai 200233,PRC
 *
 * This software is the confidential and proprietary information of Wonders Group.
 * (Research & Development Center). You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with Wonders Group.
 *
 * Distributable under GNU LGPL license by gun.org
 */

package com.net.https;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * 
 * @author zhiwen.nan
 * @version $Revision$ 2011-3-11
 * @author (lastest modification by $Author$)
 * @since 2.0
 */
public class HttpServerAccess {

	private static int timeoutConnection = 90 * 1000;
	public static final String TAG = "HttpServerAccess";

	public static String request(String url, List<NameValuePair> params) throws Exception {

		String result = "";
		HttpPost httpRequest = new HttpPost(url);
		httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

		BasicHttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);

		HttpResponse httpResponse = httpclient.execute(httpRequest);
		Log.d(TAG, "http status code:" + httpResponse.getStatusLine().getStatusCode());
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);

		} else {
			throw new Exception();
		}
		return result;
	}

	public static String request(String url, List<NameValuePair> params, int timeoutConnection) throws Exception {
		String result = "";
		HttpPost httpRequest = new HttpPost(url);
		httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

		BasicHttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);

		HttpResponse httpResponse = httpclient.execute(httpRequest);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);

		} else {
			throw new Exception();
		}
		return result;
	}

	public static String request(String url) throws Exception {
		HttpGet httpRequest = new HttpGet(url);

		BasicHttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);

		HttpResponse httpResponse = httpclient.execute(httpRequest);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String strResult = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
			return strResult;
		} else {
			throw new Exception();
		}
	}

	public static String requestWithHttps(String url) {

		String result = "";
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
			HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			result = sb.toString();
			Log.d("tag", "result str:" + sb.toString());

		} catch (Exception e) {
			Log.d("tag", " result str exception:" + e.toString());
		}

		return result;
	}

	private static class MyHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}

	}

	private static class MyTrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)

		throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	}

	public static boolean isConnect(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isNetWorkAvailable(Context aContext) {
		ConnectivityManager connectManager = (ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = null;
		if (connectManager != null) {
			netinfo = connectManager.getActiveNetworkInfo();
		}

		return (netinfo != null && netinfo.isConnected());
	}

}
