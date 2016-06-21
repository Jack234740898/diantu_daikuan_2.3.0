package com.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*
 * ScrowView嵌套ViewPager
 */
public class ScrowViewPager extends ViewPager {
        public ScrowViewPager(Context context) {
                super(context);
        }

        public ScrowViewPager(Context context, AttributeSet attrs) {
                super(context, attrs);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent arg0) {
                // 返回false
                return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent arg0) {
                // 返回false
                return false;
        }

        @Override
        public void setCurrentItem(int item, boolean smoothScroll) {
                super.setCurrentItem(item, smoothScroll);
        }

        @Override
        public void setCurrentItem(int item) {
                super.setCurrentItem(item);
        }


// 防止viewpager跟scrollview冲突
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev)
        {
          boolean ret = super.dispatchTouchEvent(ev);
          if(ret) 
          {
            requestDisallowInterceptTouchEvent(true);
          }
          return ret;
        }       
        
        
}