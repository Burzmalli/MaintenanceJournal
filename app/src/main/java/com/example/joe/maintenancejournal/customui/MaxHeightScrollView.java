package com.example.joe.maintenancejournal.customui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

/**
 * Created by jowillia on 5/5/2017.
 */

public class MaxHeightScrollView extends ScrollView {
    public static int WITHOUT_MAX_HEIGHT_VALUE = -1;

    private int maxHeight = WITHOUT_MAX_HEIGHT_VALUE;

    public MaxHeightScrollView(Context context) {
        super(context);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (maxHeight != WITHOUT_MAX_HEIGHT_VALUE
                    && heightSize > maxHeight) {
                heightSize = maxHeight;
            }
            if(heightSize < 0 || heightSize > 1000)
                heightSize = 0;

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
            getLayoutParams().height = heightSize;
        } catch (Exception e) {
            Log.d("onMesure", "Error forcing height", e);
        } finally {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
