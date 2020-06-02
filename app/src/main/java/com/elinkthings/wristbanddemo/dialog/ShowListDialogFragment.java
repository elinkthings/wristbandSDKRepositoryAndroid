package com.elinkthings.wristbanddemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.elinkthings.wristbanddemo.R;
import com.elinkthings.wristbanddemo.utils.L;
import com.elinkthings.wristbanddemo.view.MyItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 列表显示的dialog
 */
public class ShowListDialogFragment extends DialogFragment implements View.OnClickListener {
    private String TAG = ShowListDialogFragment.class.getName();

    private Context mContext;
    private onDialogListener mOnDialogListener;
    private TextView mTvCancel, mTvTitle;
    private RecyclerView rv_dialog_list;
    private DialogStringImageAdapter mAdapter;
    private int mCancelColor;
    private CharSequence mCancel;
    private CharSequence mTitle;
    private ArrayList<DialogStringImageAdapter.DialogStringImageBean> mList;
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

    public static ShowListDialogFragment newInstance() {
        return new ShowListDialogFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_list_data_fillet, container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = view.getContext();
        initView(view);
        initData(mList, mTitle, mCancel, mCancelColor);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.i(TAG,"onActivityCreated");
        Dialog dialog = getDialog();
        if (dialog != null) {
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



    private void initView(View view) {
        mTvTitle = view.findViewById(R.id.tv_dialog_list_data_title);
        mTvCancel = view.findViewById(R.id.tv_dialog_photo_cancel);
        rv_dialog_list = view.findViewById(R.id.rv_dialog_list);
        rv_dialog_list.setLayoutManager(new LinearLayoutManager(mContext));
        rv_dialog_list.addItemDecoration(new MyItemDecoration(mContext,
                LinearLayoutManager.VERTICAL, 1,
                mContext.getResources().getColor(R.color.public_white)));

    }


    private void initData(ArrayList<DialogStringImageAdapter.DialogStringImageBean> list, CharSequence title, CharSequence cancel, int cancelColor) {

        if (mList == null)
            mList = new ArrayList<>();
        mAdapter = new DialogStringImageAdapter(mContext, mList, position -> {
            if (mOnDialogListener != null) {
                //item点击事件
                mOnDialogListener.onItemListener(position);
                dismiss();
            }
        });
        rv_dialog_list.setAdapter(mAdapter);

        setList(list);
        setTitle(title);
        setCancel(cancel, cancelColor);
        mTvCancel.setOnClickListener(this);
    }

    public ShowListDialogFragment setTitle(CharSequence title) {
        this.mTitle = title;
        if (mTvTitle != null) {
            if (mTitle != null && !mTitle.equals("")) {
                mTvTitle.setVisibility(View.VISIBLE);
                mTvTitle.setText(mTitle);
            } else if (mTitle == null) {
                mTvTitle.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public ShowListDialogFragment setCancel(CharSequence cancel, @ColorInt int cancelColor) {
        this.mCancel = cancel;
        this.mCancelColor = cancelColor;
        if (mTvCancel != null) {
            if (mCancel != null && !mCancel.equals("")) {
                mTvCancel.setVisibility(View.VISIBLE);
                mTvCancel.setText(mCancel);
            } else if (mCancel == null) {
                mTvCancel.setVisibility(View.GONE);
            }
            if (mCancelColor != 0)
                mTvCancel.setTextColor(mCancelColor);


        }
        return this;
    }

    public ShowListDialogFragment setList(List<DialogStringImageAdapter.DialogStringImageBean> list) {
        if (mList == null) {
            mList = new ArrayList<>();
            mList.addAll(list);
        }
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
        return this;
    }

    public ShowListDialogFragment setOnDialogListener(onDialogListener onDialogListener) {
        mOnDialogListener = onDialogListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_dialog_photo_cancel) {
            if (mOnDialogListener != null)
                mOnDialogListener.onCancelListener(v);
            dismiss();
        }
    }


    public ShowListDialogFragment setBottom(boolean bottom) {
        mBottom = bottom;
        return this;
    }


    public ShowListDialogFragment setBackground(boolean background) {
        mBackground = background;
        return this;
    }


    public ShowListDialogFragment setCancelBlank(boolean cancelable) {
        mCancelBlank = cancelable;
        return this;
    }


    public boolean isShow() {
        return mShow;
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


    public interface onDialogListener {

        default void onCancelListener(View v) {
        }

        void onItemListener(int position);


    }

}
