package com.apreparey.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apreparey.ImmorVpAdapter;
import com.immortalviewpager.AutoScrollViewPager;
import com.immortalviewpager.FillingViewsListener;
import com.sxjs.diantu_daikuan.R;

public class MainFgBannerVpHelper {

	private View mConvertView;

	public MainFgBannerVpHelper(Context context) {
		mContext = context;
	}

	public void setConvertView(View body) {
		mConvertView = body;
		setVp();
	}

	private Context mContext;
	private AutoScrollViewPager mVp;
	private ImmorVpAdapter mAdapter;

	public void setVp() {
		mAdapter = new ImmorVpAdapter(mContext);
		mVp = (AutoScrollViewPager) mConvertView.findViewById(R.id.vp_banner);
		mVp.setAdapter(mAdapter);
		mVp.setInterval(2500);
	}

	public void setData() {
		mAdapter.setBtmCount((ViewGroup) mConvertView.findViewById(R.id.btm),
				R.layout.aatest_btmimg, 2, R.drawable.xl_indicator_blue,
				R.drawable.xl_indicator_gray);
		mAdapter.initViewList(2, R.layout.aatest_immortal_vp_item,
				new FillingViewsListener() {
					@Override
					public void fillingView(View view, int position) {
						TextView tvfinal = (TextView) view
								.findViewById(R.id.tv);
						tvfinal.setText("" + position);
					}
				});
		mAdapter.notifyDataSetChanged();
		mVp.setCurrentItem(1);
		mVp.startAutoScroll();
	}

}
