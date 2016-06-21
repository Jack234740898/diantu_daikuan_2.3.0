package com.immortalviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class ImmortalvpAdapter extends PagerAdapter {

    private Context mContext;
    List<View> mList =new ArrayList<View>();
    private ArrayList<ImageView> imgList = new ArrayList<>();

    public ImmortalvpAdapter(Context context){
        mContext = context ;
    }

    @Deprecated
    public void setViewList( List views ){
        mList = views ;
    }

    public void initViewList(int size,int res,FillingViewsListener listener){
        for ( int i = 0 ; i<size ; i++){
            if ( size>1 && i == 0 ){
                View viewFirst = View.inflate(mContext,res,null);
                listener.fillingView(viewFirst,size-1);
                mList.add(viewFirst);
            }
            View viewNormal = View.inflate(mContext,res,null);
            listener.fillingView(viewNormal,i);
            mList.add(viewNormal);
            if ( size>1 && i == (size-1) ){
                View viewfinal = View.inflate(mContext,res,null);
                listener.fillingView(viewfinal,0);
                mList.add(viewfinal);
            }
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1 ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(mList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(mList.get(position));
        return mList.get(position);
    }

    private int mSelectRes ;
    private int mNormalRes ;
    public void setBtmCount(ViewGroup llgroup, int img, int count, int selectRes , int normalRes) {
    	if ( count <=1 )
    		return;
        mSelectRes = selectRes ;
        mNormalRes = normalRes ;
        for ( int i = 0 ; i < count ; i++ ){
            ImageView imgv = (ImageView) View.inflate(mContext,img,null);
            LinearLayout.LayoutParams params = new LayoutParams(30, 30);
            imgList.add(imgv);
            llgroup.addView(imgv,params);
        }
    }

    public void setCorrectPosition(int correctPosition,int lastPosition) {
    	if( imgList.size() <= 0){
    		return ;
    	}
        imgList.get(correctPosition).setBackgroundResource(mSelectRes);
        imgList.get(lastPosition).setBackgroundResource(mNormalRes);
    }

}
