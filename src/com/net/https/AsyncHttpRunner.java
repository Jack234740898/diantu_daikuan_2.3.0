package com.net.https;


import android.util.Log;

import org.apache.http.NameValuePair;

import java.util.List;


/**
 *
 *@version 1.0.0
 * @author zhiwen.nan
 */
public class AsyncHttpRunner {
    private  static final String TAG = "AsyncHttpRunner";


	public static void request(final String url, final List<NameValuePair> params,
			 final RequestListener requestListener) {
		new Thread() {
			@Override
			public void run() {
				try {
					String result = HttpServerAccess.request(url, params) ;
                    Log.d(TAG, "request result:" + result);
                    if(!result.equals("")) {
                        requestListener.onComplete(result);
                    }

				} catch (Exception e) {
					requestListener.onError(e);
				}
			}
		}.start();

	}



    public static void request(final String url, final List<NameValuePair> params,
                               final RequestListener requestListener,final int timeOutConnction) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String result = HttpServerAccess.request(url, params, timeOutConnction) ;
                    Log.d(TAG, "request result:" + result);
                    if(!result.equals("")) {
                        requestListener.onComplete(result);
                    }

                } catch (Exception e) {
                    requestListener.onError(e);
                }
            }
        }.start();

    }




    public static void request(final String url,final RequestListener requestListener){
        new Thread() {
            @Override
            public void run() {
                try {
                    String result = HttpServerAccess.request(url) ;
                    Log.d(TAG, "request result:" + result);
                    requestListener.onComplete(result);
                } catch (Exception e) {
                    requestListener.onError(e);
                }
            }
        }.start();
    }


   public static void requestWithHttps(final String url,final RequestListener requestListener) {
       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   String result = HttpServerAccess.requestWithHttps(url) ;
                   requestListener.onComplete(result);
               }catch (Exception e) {
                   requestListener.onError(e);
               }

           }
       }).start();
   }
}
