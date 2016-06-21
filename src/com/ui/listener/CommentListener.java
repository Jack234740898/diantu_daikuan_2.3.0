package com.ui.listener;

import org.json.JSONObject;

public interface CommentListener {

	public void onError();
	public void onSuccess(JSONObject jsoObject);
}
