package com.lrc.baseand;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 列表适配器基类，泛型，继承 {@link BaseAdapter}
 * 
 * @author ganyu
 * @created 2015-4-20
 * 
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
	protected Context mContext;
	/** 数据列表 */
	protected List<T> mDataList;
	
	public BaseListAdapter(Context context, List<T> dataList) {
		this.mContext = context;
		this.mDataList = dataList;
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
	public abstract View getView(int position, View convertView, ViewGroup parent);
	
	/**
	 * 移除指定索引的列表项
	 * @param position 列表索引
	 */
	public void remove(int position) {
		if (mDataList == null || position >= mDataList.size()) {
			return;
		}
		mDataList.remove(position);
		notifyDataSetChanged();
	}
	
	/**
	 * 添加列表项
	 * @param object
	 */
	public void add(T object) {
		if (mDataList == null || object == null) {
			return;
		}
		int position = 0;
		if (mDataList.size() > 0) {
			position = mDataList.size() - 1;
		}
		mDataList.add(position, object);
		notifyDataSetChanged();
	}
	
	/**
	 * 在指定索引添加列表项
	 * @param position 索引
	 * @param object
	 */
	public void add(int position, T object) {
		if (mDataList == null || position < 0 || object == null) {
			return;
		}
		mDataList.add(position, object);
		notifyDataSetChanged();
	}

}
