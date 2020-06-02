package com.elinkthings.wristbanddemo.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elinkthings.wristbanddemo.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * xing<br>
 * 2019/8/24<br>
 * dialog列表<br>
 */
public class DialogStringImageAdapter extends RecyclerView.Adapter<DialogStringImageAdapter.KeyViewHolder> {

    private List<DialogStringImageBean> mList;
    private OnItemClickListener listener;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public DialogStringImageAdapter(Context context, List<DialogStringImageBean> list, OnItemClickListener listener) {
        mList = list;
        this.listener = listener;
        mContext = context;
    }


    @NonNull
    @Override
    public KeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_string_dialog_list, parent, false);

        return new KeyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyViewHolder holder, int position) {
        DialogStringImageBean bean = mList.get(position);
        holder.mTvTitle.setText(bean.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    static class KeyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvTitle;

        KeyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_dialog_list_data);

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(getLayoutPosition());
            });
        }
    }


    public static class DialogStringImageBean{

        String mName;
        long mType;

        public DialogStringImageBean(String name, long type) {
            mName = name;
            mType = type;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public long getType() {
            return mType;
        }

        public void setType(long type) {
            mType = type;
        }
    }

}
