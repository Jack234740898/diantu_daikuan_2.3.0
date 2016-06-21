package com.sxjs.diantu_daikuan.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.utils.DatetimeUtil;
import com.utils.LogUtil;
import com.utils.ScreenUtil;
import com.utils.StringUtil;

import org.json.JSONObject;

public class CustomerReviewsAdapter extends MyBaseAdapter {

	private static final String TAG = "CustomerReviewsAdapter";
	public CustomerReviewsAdapter(Context con) {
		super(con);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(null==convertView){
			vh = new ViewHolder();
			convertView = mInflater.inflate(R.layout.customer_reviews_item, null);
			vh.content_text = (TextView) convertView.findViewById(R.id.content_text);
			vh.rating_num_text = (TextView) convertView.findViewById(R.id.rating_num_text);
			vh.time_text = (TextView) convertView.findViewById(R.id.time_text);
			vh.rating_img_group = (LinearLayout) convertView.findViewById(R.id.rating_img_group);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		bindViewData(vh,position);
		return convertView;
	}
	
	private void showPro_ratings_stars(double pro_ratings_stars, ViewHolder vh) {
		LogUtil.d(TAG, "showPro_ratings_stars()==pro_ratings_stars is "
				+ pro_ratings_stars);
		vh.rating_img_group.removeAllViews();
		if (pro_ratings_stars <= 0.5) {
			vh.rating_num_text.setVisibility(View.GONE);
		} else {
			vh.rating_num_text.setVisibility(View.VISIBLE);
			vh.rating_num_text.setText(pro_ratings_stars + "分");
		}
		// "pro_ratings_stars": 4.5,
		// "pro_ratings": "4.8"
		int pro_ratings_stars_int = (int) pro_ratings_stars;
		double double_differ = pro_ratings_stars - pro_ratings_stars_int;// 计算浮点精度差值
		for (int i = 0; i < 5; i++) {
			View view = mInflater.inflate(R.layout.rating_img_item, null);
			ImageView rating_img1 = (ImageView) view
					.findViewById(R.id.rating_img1);
			ImageView rating_img2 = (ImageView) view
					.findViewById(R.id.rating_img2);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.rightMargin = ScreenUtil.dip2px(mContext, 4.0f);
			//rating_img1.setLayoutParams(params);
			//rating_img2.setLayoutParams(params);
			if (pro_ratings_stars < 0.5) {
				rating_img2.setVisibility(View.GONE);
			} else {
				if (i <= (pro_ratings_stars_int - 1)) {
					rating_img1.setVisibility(View.GONE);
					rating_img2.setVisibility(View.VISIBLE);
					rating_img2.setBackgroundResource(R.drawable.grade_star);
				} else {
					rating_img1.setVisibility(View.VISIBLE);
					rating_img2.setVisibility(View.GONE);
					if (double_differ >= 0.5d) {
						if (i == (pro_ratings_stars_int)) {
							rating_img1.setVisibility(View.GONE);
							rating_img2.setVisibility(View.VISIBLE);
							rating_img2.setBackgroundResource(R.drawable.grade_star_half);
						}
					}
				}
			}
			vh.rating_img_group.addView(view);
		}
	}
	
	private void bindViewData(ViewHolder vh,int position) {
		JSONObject obj = dataList.get(position);
		String sourceUserName = obj.optString("sourceUserName");
		double score = obj.optDouble("score");
		String content = obj.optString("content");
		long createTime = obj.optLong("createTime");
		vh.content_text.setText("");
		if(StringUtil.checkStr(content)){
			vh.content_text.setText(content);
		}
		String time = DatetimeUtil.getTimeLocal(createTime);
		vh.time_text.setText(time+"");
		if(StringUtil.checkStr(sourceUserName)){
			vh.time_text.setText(time+" "+sourceUserName);
		}
		showPro_ratings_stars(score, vh);
	}

	static final class ViewHolder{
		TextView content_text,rating_num_text,time_text;
		LinearLayout rating_img_group;
	}
}
