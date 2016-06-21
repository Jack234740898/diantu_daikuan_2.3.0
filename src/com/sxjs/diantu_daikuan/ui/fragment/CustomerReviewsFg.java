package com.sxjs.diantu_daikuan.ui.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.CustomerReviewsAdapter;
import com.utils.DatetimeUtil;
import com.utils.LogUtil;
import com.utils.ScreenUtil;
import com.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class CustomerReviewsFg extends BaseFragment {

	private static final String TAG = "CustomerReviewsFg";
	private JSONArray score;

	public CustomerReviewsFg(JSONArray score) {
		this.score = score;
	}

	@Override
	protected int setContentView() {
		return R.layout.customer_review_item;
	}

	@Override
	protected void initView() {
		initV();
	}

	private TextView textview;
	private View empty_view;
	//private ListView listview;
	private CustomerReviewsAdapter mAdapter;
	private LinearLayout view_group;
	private View bottom_view;
	private void initV() {
		bottom_view = findViewById(R.id.bottom_view);
		bottom_view.setVisibility(View.VISIBLE);
		view_group = (LinearLayout) findViewById(R.id.view_group);
		empty_view = findViewById(R.id.empty_view);
		empty_view.setVisibility(View.GONE);
		textview = (TextView) findViewById(R.id.textview);
		textview.setText("最近还无评论~");
		//listview = (ListView) findViewById(R.id.listview);
		//listview.setVisibility(View.VISIBLE);
		view_group.setVisibility(View.VISIBLE);
		if (null == score || score.length() <= 0) {
			//listview.setVisibility(View.GONE);
			bottom_view.setVisibility(View.GONE);
			view_group.setVisibility(View.GONE);
			empty_view.setVisibility(View.VISIBLE);
			empty_view.setBackgroundColor(Color.WHITE);
		} else {
			/*listview.setAdapter(mAdapter = new CustomerReviewsAdapter(mActivity));
			mAdapter.setData(JSONUtil.arrayToList(score));
			mAdapter.notifyDataSetChanged();*/
			addView();
		}
		
	}

	private void addView(){
		view_group.removeAllViews();
		for(int i=0;i<score.length();i++){
			View view = mInflater.inflate(R.layout.customer_reviews_item, null);
			TextView content_text = (TextView) view.findViewById(R.id.content_text);
			TextView rating_num_text = (TextView) view.findViewById(R.id.rating_num_text);
			TextView time_text = (TextView) view.findViewById(R.id.time_text);
			LinearLayout rating_img_group = (LinearLayout) view.findViewById(R.id.rating_img_group);
			JSONObject obj = score.optJSONObject(i);
			String sourceUserName = obj.optString("sourceUserName");
			double score = obj.optDouble("score");
			String content = obj.optString("content");
			long createTime = obj.optLong("createTime");
			content_text.setText("");
			if(StringUtil.checkStr(content)){
				content_text.setText(content);
			}
			String time = DatetimeUtil.getTimeLocal(createTime);
			time_text.setText(time+"");
			if(StringUtil.checkStr(sourceUserName)){
				time_text.setText(time+" "+sourceUserName);
			}
			showPro_ratings_stars(score, rating_img_group,rating_num_text);
			view_group.addView(view);
		}
	}
	
	private void showPro_ratings_stars(double pro_ratings_stars, LinearLayout rating_img_group,TextView rating_num_text) {
		LogUtil.d(TAG, "showPro_ratings_stars()==pro_ratings_stars is "
				+ pro_ratings_stars);
		rating_img_group.removeAllViews();
		if (pro_ratings_stars <= 0.5) {
			rating_num_text.setVisibility(View.GONE);
		} else {
			rating_num_text.setVisibility(View.VISIBLE);
			rating_num_text.setText(pro_ratings_stars + "分");
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
			params.rightMargin = ScreenUtil.dip2px(mActivity,4.0f);
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
			rating_img_group.addView(view);
		}
	}
}
