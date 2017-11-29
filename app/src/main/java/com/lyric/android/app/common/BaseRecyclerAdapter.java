package com.lyric.android.app.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * RecyclerView适配器基类
 * @author lyricgan
 * @time 2016/8/12 11:51
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<T> mDataList;
    private int mLayoutId;
    private OnItemClickListener<T> mOnItemClickListener;

    public BaseRecyclerAdapter(Context context, int layoutId) {
        this(context, new ArrayList<T>(), layoutId);
    }

    public BaseRecyclerAdapter(Context context, T[] arrays, int layoutId) {
        this(context, Arrays.asList(arrays), layoutId);
    }

    public BaseRecyclerAdapter(Context context, List<T> dataList, int layoutId) {
        this.mContext = context;
        this.mDataList = dataList;
        this.mLayoutId = layoutId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        final RecyclerView.ViewHolder holder = new InnerViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                T object = null;
                if (mDataList != null && !mDataList.isEmpty()) {
                    object = mDataList.get(position);
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, object, v);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        T object = null;
        if (mDataList != null && !mDataList.isEmpty()) {
            object = mDataList.get(position);
        }
        convert(holder.itemView, position, object);
    }

    public abstract void convert(View itemView, int position, T item);

    @Override
    public int getItemCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Context getContext() {
        return mContext;
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mOnItemClickListener = listener;
    }

    public void setDataList(List<T> dataList) {
        this.mDataList = dataList;
    }

    public List<T> getDataList() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        return mDataList;
    }

    public boolean isEmpty() {
        return (mDataList == null || mDataList.isEmpty());
    }

    public void add(T object) {
        add(mDataList.size(), object);
    }

    public void add(int location, T object) {
        if (location < 0 || location > mDataList.size()) {
            return;
        }
        if (object != null) {
            mDataList.add(location, object);
            notifyDataSetChanged();
        }
    }

    public void add(List<T> dataList) {
        if (dataList != null && dataList.size() > 0) {
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public void remove(T object) {
        if (object != null) {
            mDataList.remove(object);
            notifyDataSetChanged();
        }
    }

    public void remove(int location) {
        if (location < 0 || location >= mDataList.size()) {
            return;
        }
        mDataList.remove(location);
        notifyDataSetChanged();
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    private static class InnerViewHolder extends RecyclerView.ViewHolder {

        InnerViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener<T> {

        void onItemClick(int position, T object, View itemView);
    }
}
