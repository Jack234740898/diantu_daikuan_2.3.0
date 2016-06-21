package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.adapter.ViewPagerAdapter;
import com.utils.IntentUtil;

import java.io.FileOutputStream;
import java.util.ArrayList;

/*
 * 第一次启动引导页面
 */
public class OpenGuideActivity extends BaseActivity implements OnPageChangeListener,OnClickListener{

	// 引导图片资源
	private static final int[] pics = { R.drawable.guide1, R.drawable.guide2,
			R.drawable.guide3, R.drawable.guide4 };

	@Override
	protected void onInit(Bundle bundle) {
		mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mActivity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onInit(bundle);
		initView();
		//AnimationUtil.fadeAnimat(mActivity, root, true);
	}

	private ArrayList<View> views;
	private ViewPager viewpager;
	private LinearLayout img_point_group;
	private View root,quick_go;
	private void initView() {
		root = findViewById(R.id.root);
		views = new ArrayList<View>();
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		img_point_group = (LinearLayout) findViewById(R.id.img_point_group);
		quick_go = findViewById(R.id.quick_go);
		quick_go.setVisibility(View.GONE);
		quick_go.setOnClickListener(this);
		initPics();
		addViewPoint();
		viewpager.setAdapter(new ViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(this);
	}

	private void initPics() {
		// 定义一个布局并设置参数
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		// 初始化引导图片列表
		for (int i = 0; i < pics.length; i++) {
			ImageView iv = new ImageView(mActivity);
			iv.setScaleType(ScaleType.CENTER_CROP);
			iv.setLayoutParams(mParams);
			iv.setImageResource(pics[i]);
			if(i==(pics.length-1)){
				iv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
					}
				});
			}
			views.add(iv);
		}

	}
	protected void write() {
		try 
		{  
		       FileOutputStream fos = 
		openFileOutput("test.txt",Context.MODE_PRIVATE);  
		       String content = 
		"key=123";  //要写入的内容
		       fos.write(content.getBytes());  
		       
		fos.close();  
		   } catch (Exception e) {
		       e.printStackTrace();  

		   }
	}

	private int currIndex;
	private void addViewPoint() {
		img_point_group.removeAllViews();
		for(int i=0;i<pics.length;i++){
			View view = mInflater.inflate(R.layout.img_scroll_point_item, null);
			ImageView pointimageview = (ImageView) view
					.findViewById(R.id.pointimageview);
			view.setTag(i);
			pointimageview.setImageDrawable(mActivity.getResources()
					.getDrawable(R.drawable.circle_light_gray_shape));
			if (i == currIndex) {
				pointimageview.setImageDrawable(mActivity.getResources()
						.getDrawable(R.drawable.circle_orange_shape));
			} 
			img_point_group.addView(view);
		}
	}

	/*
	 * 滑动时小圆圈的移动
	 */
	private void movePoint(int index) {
		currIndex = index;
		for (int i = 0; i < img_point_group.getChildCount(); i++) {
			View view = img_point_group.findViewWithTag(i);
			ImageView im = (ImageView) view.findViewById(R.id.pointimageview);
			im.setImageDrawable(mActivity.getResources().getDrawable(
					R.drawable.circle_light_gray_shape));
			if (i == currIndex) {
				im.setImageDrawable(mActivity.getResources().getDrawable(
						R.drawable.circle_orange_shape));
			}
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {		
	}

	@Override
	public void onPageSelected(int arg0) {	
		movePoint(arg0);
		if(arg0==(pics.length-1)){
			quick_go.setVisibility(View.VISIBLE);
		}else{
			quick_go.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.quick_go:
			write();
			IntentUtil.activityForward(mActivity,
					MainHomeActivity.class, null, true);
			break;

		default:
			break;
		}
	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.open_guide;
	}
}
