package com.utils;

import android.view.View;
import android.widget.TextView;

import com.img.download.ImageLoad;
import com.sxjs.diantu_daikuan.R;
import com.ui.view.CircularImage;

public class UserHeadShowUtil {

	public static void showHead(ImageLoad imgLoad, CircularImage head_img,
			TextView head_text, String userAvata, String userName) {
		if (StringUtil.checkStr(userAvata) && userAvata.startsWith("http")) {
			head_img.setVisibility(View.VISIBLE);
			head_text.setVisibility(View.GONE);
			imgLoad.loadImg(head_img, userAvata, R.drawable.default_head);
		} else {
			head_img.setVisibility(View.GONE);
			head_text.setVisibility(View.VISIBLE);
			if (StringUtil.checkStr(userName)) {
				head_text.setBackgroundResource(R.drawable.circle_white_shape);
				head_text.setText(userName.substring(0, 1) + "");
			} else {
				head_text.setBackgroundResource(R.drawable.default_head);
				head_text.setText("");
			}
		}
	}
}
