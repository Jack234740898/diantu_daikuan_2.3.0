package com.apreparey.main;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.apreparey.ImmorVpAdapter;
import com.immortalviewpager.AutoScrollViewPager;
import com.immortalviewpager.FillingViewsListener;
import com.sxjs.diantu_daikuan.R;

public class MainFgTopHelper {

	private View mConvertView;

	public MainFgTopHelper(Context context) {
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
		mVp = (AutoScrollViewPager) mConvertView.findViewById(R.id.vp);
		mVp.setAdapter(mAdapter);
		mVp.setInterval(2500);
	}

	public void setData() {
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
