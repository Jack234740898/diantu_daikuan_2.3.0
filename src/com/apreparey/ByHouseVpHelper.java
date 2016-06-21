package com.apreparey;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.immortalviewpager.AutoScrollViewPager;
import com.immortalviewpager.FillingViewsListener;
import com.sxjs.diantu_daikuan.R;

public class ByHouseVpHelper {

	private Context mContext;
	private View mContentView;
	private AutoScrollViewPager mVp;
	private ImmorVpAdapter mAdapter;

	public ByHouseVpHelper(Context context) {
		mContext = context;
	}

	public View getVp() {
		mContentView = View.inflate(mContext, R.layout.aatest_immortalvp, null);
		mAdapter = new ImmorVpAdapter(mContext);
		mVp = (AutoScrollViewPager) mContentView.findViewById(R.id.vp);
		mVp.setAdapter(mAdapter);
		mVp.setInterval(2500);
		return mContentView;
	}

	public void setData() {
		mAdapter.setBtmCount((ViewGroup) mContentView.findViewById(R.id.btm),
				R.layout.aatest_btmimg, 6, R.drawable.xl_indicator_blue,
				R.drawable.xl_indicator_gray);
		mAdapter.initViewList(6, R.layout.aatest_immortal_vp_item,
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
