package com.elinkthings.wristbanddemo.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * xing<br>
 * 2020/5/9<br>
 * SeekBar允许设置最小值
 * 兼容旧版本
 */
public class SeekBarMin extends SeekBar implements SeekBar.OnSeekBarChangeListener {


    private int mMin;
    private boolean mApi26 = false;
    private OnSeekBarChangeListener mOnSeekBarChangeListener;

    public SeekBarMin(Context context) {
        this(context, null);
    }

    public SeekBarMin(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBarMin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mApi26 = true;
        }
        super.setOnSeekBarChangeListener(this);
    }


    @Override
    public synchronized void setMin(int min) {
        if (mApi26) {
            super.setMin(min);
        } else {
            mMin = min;
        }
    }


    @Override
    public synchronized void setProgress(int progress) {
        if (!mApi26) {
            progress -= mMin;
        }
        super.setProgress(progress);
    }

    @Override
    public synchronized void setMax(int max) {
        if (!mApi26) {
            max -= mMin;
        }
        super.setMax(max);
    }

    @Override
    public int getMin() {
        return mMin;
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        mOnSeekBarChangeListener = l;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!mApi26) {
            progress += mMin;
        }
        if (mOnSeekBarChangeListener != null)
            mOnSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mOnSeekBarChangeListener != null)
            mOnSeekBarChangeListener.onStartTrackingTouch(seekBar);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mOnSeekBarChangeListener != null)
            mOnSeekBarChangeListener.onStopTrackingTouch(seekBar);
    }
}
