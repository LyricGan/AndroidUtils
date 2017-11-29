package com.lyric.android.app.common;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 适配器基类{@link BaseAdapter}
 * @author lyricgan
 * @time 16/3/10
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    private List<T> mDataList;
    private int mLayoutId;

    public BaseListAdapter(int layoutId) {
        this(new ArrayList<T>(), layoutId);
    }
	
    public BaseListAdapter(T[] arrays, int layoutId) {
        this(Arrays.asList(arrays), layoutId);
    }

    public BaseListAdapter(List<T> dataList, int layoutId) {
        this.mDataList = dataList;
        this.mLayoutId = layoutId;
    }

	@Override
	public int getCount() {
		return mDataList != null ? mDataList.size() : 0;
	}

	@Override
	public T getItem(int position) {
        if (mDataList != null) {
            if (position >= 0 && position < mDataList.size()) {
                return mDataList.get(position);
            }
        }
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder = BaseViewHolder.get(convertView, parent, mLayoutId);
        T object = getItem(position);
        holder.setAssociatedObject(object);
        convert(holder, position, object);
        return holder.getView();
    }

    public abstract void convert(BaseViewHolder holder, int position, T item);

    public void setDataList(List<T> dataList) {
        this.mDataList = dataList;
    }

    public List<T> getDataList() {
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
}
