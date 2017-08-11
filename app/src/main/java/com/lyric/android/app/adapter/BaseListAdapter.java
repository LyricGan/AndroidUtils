package com.lyric.android.app.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lyricgan
 * @description base adapter for list, {@link BaseAdapter}
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
		return mDataList.get(position);
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
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        return mDataList;
    }

    public boolean isEmpty() {
        return (mDataList == null || mDataList.isEmpty());
    }

    public void add(T object) {
        this.add(this.mDataList.size(), object);
    }

    public void add(int location, T object) {
        if (location < 0 || location > this.mDataList.size()) {
            return;
        }
        if (object != null) {
            this.mDataList.add(location, object);
            this.notifyDataSetChanged();
        }
    }

    public void add(List<T> dataList) {
        if (dataList != null && dataList.size() > 0) {
            this.mDataList.addAll(dataList);
            this.notifyDataSetChanged();
        }
    }

    public void remove(T object) {
        if (object != null) {
            this.mDataList.remove(object);
            this.notifyDataSetChanged();
        }
    }

    public void remove(int location) {
        if (location < 0 || location >= this.mDataList.size()) {
            return;
        }
        this.mDataList.remove(location);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.mDataList.clear();
        this.notifyDataSetChanged();
    }
}
