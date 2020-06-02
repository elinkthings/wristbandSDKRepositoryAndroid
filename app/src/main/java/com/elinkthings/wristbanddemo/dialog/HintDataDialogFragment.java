package com.elinkthings.wristbanddemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.elinkthings.wristbanddemo.R;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;


/**
 * 显示信息的弹框
 */
public class HintDataDialogFragment extends DialogFragment {
    private static String TAG = HintDataDialogFragment.class.getName();
    private Context mContext;
    private DialogListener mDialogListener;
    private TextView mTvTitle;
    private TextView mTvCancel, mTvSucceed, mTvContent;
    private View view_cancel_line;
    private CharSequence mTitle;
    private CharSequence mContent;
    /**
     * 是否居中
     */
    private boolean isCenter = false;
    private CharSequence mCancel;
    @ColorInt
    private int mCancelColor = 0;
    private CharSequence mOkStr;
    @ColorInt
    private int mOkColor = 0;
    @ColorInt
    private int mTitleColor = 0;
    private boolean mBottom;
    /**
     * 是否显示灰色背景
     */
    private boolean mBackground=true;

    /**
     * 点击空白区域是否关闭
     */
    private boolean mCancelBlank;

    /**
     * 是否显示
     */
    private boolean mShow;


    public static HintDataDialogFragment newInstance() {
        return new HintDataDialogFragment();
    }

    private HintDataDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialogView = new Dialog(requireContext(), R.style.MyDialog);// 创建自定义样式dialog
        dialogView.setCancelable(false);//设置是否可以关闭
        dialogView.setCanceledOnTouchOutside(mCancelBlank);//设置点击空白处是否可以取消

        dialogView.setOnKeyListener((dialog, keyCode, event) -> {
            if (mCancelBlank) {
                return false;
            } else {
                //返回不关闭
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });


        return dialogView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_hint_data, container);// 得到加载view


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = view.getContext();
        initView(view);
        initData();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            getDialog().setOnShowListener(null);
            getDialog().setOnCancelListener(null);
            getDialog().setOnDismissListener(null);

            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams params = window.getAttributes();
                if (mBackground) {
                    params.dimAmount = 0.8f;
                } else {
                    params.dimAmount = 0f;
                }
                if (mBottom) {
                    params.y = 10;//设置Dialog距离底部的距离
                    params.gravity = Gravity.BOTTOM;
                }
                window.setAttributes(params);
            }
        }
    }


    /**
     * 初始化控件,数据
     */
    private void initView(View mDialogView) {
        mTvTitle = mDialogView.findViewById(R.id.tv_hint_data_title);
        mTvCancel = mDialogView.findViewById(R.id.tv_hint_data_cancel);
        view_cancel_line = mDialogView.findViewById(R.id.view_cancel_line);
        mTvSucceed = mDialogView.findViewById(R.id.tv_hint_data_ok);
        mTvContent = mDialogView.findViewById(R.id.tv_hint_data_context);
        mTvCancel.setOnClickListener(v -> {
            if (mDialogListener != null) {
                mDialogListener.tvCancelListener(v);
            }
            this.dismiss();

        });
        mTvSucceed.setOnClickListener(v -> {
            if (mDialogListener != null) {
                mDialogListener.tvSucceedListener(v);
            }
            this.dismiss();
        });
    }

    private void initData() {
        this.initData(mTitle, mContent, isCenter, mCancel, mOkStr);
        this.initColor(mCancelColor, mOkColor);
    }


    /**
     * 设置取消,确定按钮颜色
     *
     * @param cancelColor 取消的颜色
     * @param okColor     确定的颜色
     */
    public HintDataDialogFragment initColor(@ColorInt int cancelColor, @ColorInt int okColor) {
        this.mCancelColor = cancelColor;
        this.mOkColor = okColor;
        if (mTvCancel != null && mCancelColor != 0)
            mTvCancel.setTextColor(mCancelColor);
        if (mTvSucceed != null && mOkColor != 0)
            mTvSucceed.setTextColor(mOkColor);
        return this;
    }


    public HintDataDialogFragment setContent(CharSequence content, boolean center) {
        this.mContent = content;
        this.isCenter = center;
        if (mTvContent != null) {
            if (mContent != null && !mContent.equals("")) {
                mTvContent.setVisibility(View.VISIBLE);
                mTvContent.setText(mContent);
            } else if (mContent == null) {
                mTvContent.setVisibility(View.GONE);
            }
            if (isCenter)
                mTvContent.setGravity(Gravity.CENTER);
            else
                mTvContent.setGravity(Gravity.CENTER_VERTICAL);
        }
        return this;
    }


    public HintDataDialogFragment setTitle(CharSequence title, @ColorInt int titleColor) {
        this.mTitle = title;
        this.mTitleColor = titleColor;
        if (mTvTitle != null) {
            if (mTitle != null && !mTitle.equals("")) {
                mTvTitle.setVisibility(View.VISIBLE);
                mTvTitle.setText(mTitle);
            } else if (mTitle == null) {
                mTvTitle.setVisibility(View.GONE);
            }
            if (mTitleColor != 0) {
                mTvTitle.setTextColor(mTitleColor);
            }

        }
        return this;
    }

    public HintDataDialogFragment setCancel(CharSequence cancel, @ColorInt int cancelColor) {
        this.mCancel = cancel;
        this.mCancelColor = cancelColor;
        if (mTvCancel != null) {
            if (mCancel != null && !mCancel.equals("")) {
                mTvCancel.setVisibility(View.VISIBLE);
                view_cancel_line.setVisibility(View.VISIBLE);
                mTvCancel.setText(mCancel);
            } else if (mCancel == null) {
                mTvCancel.setVisibility(View.GONE);
                view_cancel_line.setVisibility(View.GONE);
            }
            if (mCancelColor != 0) {
                mTvCancel.setTextColor(mCancelColor);
            }


        }
        return this;
    }


    public HintDataDialogFragment setOk(CharSequence ok, @ColorInt int okColor) {
        this.mOkStr = ok;
        this.mOkColor = okColor;
        if (mTvSucceed != null) {
            if (mOkStr != null && !mOkStr.equals("")) {
                mTvSucceed.setVisibility(View.VISIBLE);
                mTvSucceed.setText(mOkStr);
            } else if (mOkStr == null) {
                mTvSucceed.setVisibility(View.GONE);
            }
            if (mOkColor != 0)
                mTvSucceed.setTextColor(mOkColor);
        }
        return this;
    }


    /**
     * 设置dialog是否在底部
     */
    public HintDataDialogFragment setBottom(boolean bottom) {
        mBottom = bottom;
        return this;
    }


    /**
     * 设置dialog是否有灰色背景
     */
    public HintDataDialogFragment setBackground(boolean background) {
        mBackground = background;
        return this;
    }

    /**
     * 设置点击空白区域是否可以关闭
     */
    public HintDataDialogFragment setCancelBlank(boolean cancelable) {
        mCancelBlank = cancelable;
        return this;
    }

    /**
     * 当前是否显示
     */
    public boolean isShow() {
        return mShow;
    }

    /**
     * 初始化数据
     *
     * @param isCenter 显示的内容是否居中
     */
    public HintDataDialogFragment initData(CharSequence title, CharSequence content,
                                           boolean isCenter, CharSequence cancel, CharSequence ok) {
        setTitle(title, mTitleColor);
        setContent(content, isCenter);
        setCancel(cancel, mCancelColor);
        setOk(ok, mOkColor);
        return this;
    }

    /**
     * 初始化数据
     */
    public HintDataDialogFragment initData(CharSequence title, CharSequence content) {
        return this.initData(title, content, false, "", "");
    }

    /**
     * 初始化数据
     */
    public HintDataDialogFragment initData(CharSequence title, CharSequence content,
                                           CharSequence cancel, CharSequence ok) {
        return this.initData(title, content, false, cancel, ok);
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setOnDismissListener(this);
            DisplayMetrics dm = new DisplayMetrics();
            if (getActivity() != null) {
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                if (dialog.getWindow() != null)
                    //设置宽度为80%
                    dialog.getWindow()
                            .setLayout((int) (dm.widthPixels * 0.9),
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }


    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            if (!mShow) {
                super.show(manager, tag);
                mShow = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void show(@NonNull FragmentManager manager) {
        this.show(manager, "HintDataDialogFragment");
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        mShow = false;
    }


    @Override
    public void dismiss() {
        try {
            mShow = false;
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface DialogListener {

        /**
         * 取消的点击事件
         *
         * @param v
         */
        default void tvCancelListener(View v) {
        }

        /**
         * 成功的点击事件
         *
         * @param v
         */
        default void tvSucceedListener(View v) {
        }

    }

    public HintDataDialogFragment setDialogListener(DialogListener dialogListener) {
        mDialogListener = dialogListener;
        return this;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mShow = false;
    }
}
