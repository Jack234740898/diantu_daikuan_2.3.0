package com.net.https;



public interface RequestListener {

	public void onComplete(String response);

	public void onError(Exception e);

}
