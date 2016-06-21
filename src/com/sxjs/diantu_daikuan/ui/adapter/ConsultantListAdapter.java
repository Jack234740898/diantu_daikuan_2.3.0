package com.sxjs.diantu_daikuan.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.constants.ParamsKey;
import com.db.service.UserInfoService;
import com.sxjs.diantu_daikuan.MyApplication;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.activity.ConsultantDetailActiviy;
import com.sxjs.diantu_daikuan.ui.activity.DaikuanProductDetailActivity;
import com.sxjs.diantu_daikuan.ui.activity.WebviewActivity;
import com.ui.view.CircularImage;
import com.ui.view.ImgScrollPointView;
import com.utils.IntentUtil;
import com.utils.LogUtil;
import com.utils.ScreenUtil;
import com.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class ConsultantListAdapter extends MyBaseAdapter {

	private static final String TAG = "ConsultantListAdapter";
	private int screenW;
	public ConsultantListAdapter(Context con) {
		super(con);
		screenW = ScreenUtil.getWidth((Activity)con);
	}

	private ArrayList<JSONObject> dataList;
	private JSONArray bannerList;

	public void setData(JSONArray bannerList, ArrayList<JSONObject> dataList) {
		this.bannerList = bannerList;
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null != bannerList&&bannerList.length()>0)
			count++;
		return null==dataList?count:count + dataList.size();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (0 == position && null != bannerList && bannerList.length() > 0) {
			view = mInflater.inflate(R.layout.consultant_banner_view, null);
			bindBannerData(view);
		} else {
			ViewHolder vh = null;
			if (null == view || !(view.getTag() instanceof ViewHolder)) {
				view = mInflater.inflate(R.layout.consulant_list_item, null);
				vh = new ViewHolder();
				vh.head_img = (CircularImage) view.findViewById(R.id.head_img);
				vh.name_text = (TextView) view.findViewById(R.id.name_text);
				vh.rating_num_text = (TextView) view
						.findViewById(R.id.rating_num_text);
				vh.desc_text = (TextView) view.findViewById(R.id.desc_text);
				vh.status_text = (TextView) view.findViewById(R.id.status_text);
				vh.rating_img_group = (LinearLayout) view
						.findViewById(R.id.rating_img_group);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			int index = position;
			if (null != bannerList && bannerList.length() > 0) {
				index--;
			}
			bindviewData(index, vh);
		}

		return view;
	}

	private static final int Interval_Time = 3 * 1000;
	private LinearLayout img_point_viewgroup;
	private AutoScrollViewPager view_pager;
	private ImgScrollPointView mImgScrollPointView;
	private ViewPagerCycleAdapter mViewPagerCycleAdapter;

	private void bindBannerData(View view) {
		img_point_viewgroup = (LinearLayout) view
				.findViewById(R.id.img_point_viewgroup);
		view_pager = (AutoScrollViewPager) view.findViewById(R.id.view_pager);
		RelativeLayout banner_rl = (RelativeLayout) view.findViewById(R.id.banner_rl);// screenW*164/750
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenW,screenW*200/750);
		banner_rl.setLayoutParams(params);
		view_pager.setInterval(Interval_Time);
		view_pager.setCycle(true);
		view_pager.startAutoScroll();
		mImgScrollPointView = new ImgScrollPointView((Activity) mContext,
				img_point_viewgroup);
		addBannerData(bannerList);
	}

	private void addBannerData(JSONArray cmsList) {
		if (null == cmsList || cmsList.length() <= 0)
			return;
		List<View> viewLists = new ArrayList<View>();
		for (int i = 0; i < cmsList.length(); i++) {
			final JSONObject cmsExtend = cmsList.optJSONObject(i);
			// final JSONObject cmsExtend = obj.optJSONObject("cmsExtend");
			String image = cmsExtend.optString("image");
			View view = mInflater.inflate(R.layout.banner_item, null);//LayoutParams.WRAP_CONTENT
			ImageView imageView = (ImageView) view.findViewById(R.id.imgview);//750*164   50：11 screenW*164/50
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenW,screenW*200/750);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			mImgLoad.loadImg(imageView, image, 0);
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					banner_forward(cmsExtend);
				}
			});
			viewLists.add(imageView);
		}
		mImgScrollPointView.addImgScrollPoint(viewLists.size());
		view_pager
				.setAdapter(mViewPagerCycleAdapter = new ViewPagerCycleAdapter(
						viewLists));
		view_pager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageSelected(int arg0) {
						mImgScrollPointView.movePoint(arg0);
					}

				});
	}

	/*
	 * type; //0 外链 1银行贷款 2现金分期 3银行服务 4信用卡服务
	 */
	protected void banner_forward(JSONObject cmsExtend) {
		int type = cmsExtend.optInt("type");
		int id = cmsExtend.optInt("id");
		Bundle bundle = new Bundle();
		bundle.putInt(ParamsKey.id, id);
		switch (type) {
		case 0:
			String title = cmsExtend.optString("title");
			String link = cmsExtend.optString("link");
			bundle.putString(ParamsKey.web_url, link);
			if(StringUtil.checkStr(link)&&link.startsWith("http")){
				IntentUtil.activityForward(mContext, WebviewActivity.class,
						bundle, false);
			}
			break;
		case 1:
			IntentUtil.activityForward((Activity) mContext,
					DaikuanProductDetailActivity.class, bundle, false);
			break;
		case 2:
			bundle.putString(ParamsKey.head_name, "现金分期详情");
			IntentUtil.activityForward((Activity) mContext,
					DaikuanProductDetailActivity.class, bundle, false);
			break;
		default:
			break;
		}
	}

	private void bindviewData(int position, ViewHolder vh) {
		JSONObject obj = dataList.get(position);
		final int id = obj.optInt("id");
		String userName = obj.optString("userName");
		String companyName = obj.optString("companyName");
		String avatar = obj.optString("avatar");
		String advantage = obj.optString("advantage");
		String description = obj.optString("description");
		double rating = obj.optDouble("rating");
		int loanDays = obj.optInt("loanDays");
		int investSuccCount = obj.optInt("investSuccCount");
		int investTotalAmount = obj.optInt("investTotalAmount");
		int authFlag = obj.optInt("onlineFlag");
		//IMUserdata.userInfoMaps.put(ParamsKey.openim_userid_pre+id,new UserInfo(userName, avatar));
		UserInfoService userInfoService = new UserInfoService((MyApplication)mContext.getApplicationContext(), id+"");
		Map<String, String> map = new HashMap<String, String>();
		map.put(ParamsKey.user_head, avatar);
		map.put(ParamsKey.user_realname, "电兔顾问-"+userName);
		userInfoService.saveData(map);
		vh.status_text.setBackgroundResource(R.drawable.online_small_ico);
		if (authFlag > 0) {//onlineFlag; //0在线 1忙碌
			vh.status_text.setBackgroundResource(R.drawable.busy_small_ico);
		}
		showPro_ratings_stars(rating, vh);
		vh.head_img.setScaleType(ScaleType.CENTER_CROP);
		mImgLoad.loadImg(vh.head_img, avatar, R.drawable.default_head);
		vh.head_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt(ParamsKey.id, id);
				IntentUtil.activityForward(mContext,
						ConsultantDetailActiviy.class, bundle, false);
			}
		});
		vh.name_text.setText("");
		if (StringUtil.checkStr(userName)) {
			vh.name_text.setText(userName);
		}
		String content1 = StringUtil.getHtmlStr(investSuccCount, "单", "累计成交；");
		String content2 = StringUtil.getHtmlStr(investTotalAmount, "万元",
				"放款总额；");
		String content3 = StringUtil.getHtmlStr(loanDays, "天", "平均放款时间");
		vh.desc_text.setText(Html.fromHtml(content1 + content2 + "" + content3));
	}

	private void showPro_ratings_stars(double pro_ratings_stars, ViewHolder vh) {
		LogUtil.d(TAG, "showPro_ratings_stars()==pro_ratings_stars is "
				+ pro_ratings_stars);
		vh.rating_img_group.removeAllViews();
		if (pro_ratings_stars <= 0.5) {
			vh.rating_num_text.setVisibility(View.GONE);
		} else {
			vh.rating_num_text.setVisibility(View.VISIBLE);
			vh.rating_num_text.setText(pro_ratings_stars + "颗星");
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
							rating_img2
									.setBackgroundResource(R.drawable.grade_star_half);
						}
					}
				}
			}
			vh.rating_img_group.addView(view);
		}
	}

	static final class ViewHolder {
		CircularImage head_img;
		TextView name_text, rating_num_text, desc_text, status_text;
		LinearLayout rating_img_group;
	}
}
