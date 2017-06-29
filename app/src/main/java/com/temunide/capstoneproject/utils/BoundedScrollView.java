package com.temunide.capstoneproject.utils;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;

import com.temunide.capstoneproject.R;

/**
 * Created by varma on 28-06-2017.
 */

public class BoundedScrollView extends NestedScrollView {

    private final int MaxWidth;

    public BoundedScrollView(Context context) {
        super(context);
        MaxWidth = 0;
    }

    public BoundedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MaxWidth = context.obtainStyledAttributes(attrs, R.styleable.BoundedScrollView).getDimensionPixelSize(R.styleable.BoundedScrollView_max_width, 0);

    }

    public BoundedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        MaxWidth = context.obtainStyledAttributes(attrs, R.styleable.BoundedScrollView).getDimensionPixelSize(R.styleable.BoundedScrollView_max_width, 0);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (MaxWidth != 0 && MeasureSpec.getSize(widthMeasureSpec) > MaxWidth) {
            Log.d("spec", MeasureSpec.getSize(widthMeasureSpec) + ">" + MaxWidth);
            int measureSpec = MeasureSpec.getMode(widthMeasureSpec);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(MaxWidth, measureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
