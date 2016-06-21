package com.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.sxjs.diantu_daikuan.R;

public class ProgressBar extends ImageView {
	private Animation staggered;
    public ProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAnimation(attrs);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAnimation(attrs);
    }

    public ProgressBar(Context context) {
        super(context);
    }

    private void setAnimation(AttributeSet attrs) {
//        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBar);
//        int frameCount = a.getInt(R.styleable.ProgressView_frameCount, 16);  
//        int duration = a.getInt(R.styleable.ProgressView_duration, 1200);
//        a.recycle();

        setAnimation(16, 1200);
    }

    public void setAnimation(final int frameCount, final int duration) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.progress_anim);
        a.setDuration(duration);
        a.setInterpolator(new Interpolator() {

            @Override
            public float getInterpolation(float input) {
                return (float)Math.floor(input*frameCount)/frameCount;
            }
        });
        startAnimation(a);
        staggered = a;
        
    }
    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if( visibility == View.VISIBLE )
            startAnimation(staggered);
        else
            clearAnimation();

    }
}
