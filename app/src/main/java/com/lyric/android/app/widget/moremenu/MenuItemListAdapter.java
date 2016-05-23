package com.lyric.android.app.widget.moremenu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyric.android.app.R;

import java.util.ArrayList;
import java.util.List;

public class MenuItemListAdapter extends RecyclerView.Adapter<MenuItemListAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<MenuItemEntity> mMenuItemEntityList;
    private OnMenuItemClickListener mOnMenuItemClickListener;

    public interface OnMenuItemClickListener {

        void onItemClick(TextView itemView, MenuItemEntity itemEntity);
    }

    public MenuItemListAdapter(Context context) {
        this(context, new ArrayList<MenuItemEntity>());
    }

    public MenuItemListAdapter(Context context, List<MenuItemEntity> menuItemEntityList) {
        this.mContext = context;
        this.mMenuItemEntityList = menuItemEntityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.view_menu_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        viewHolder.tv_menu_item = (TextView) convertView.findViewById(R.id.tv_menu_item);
        convertView.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MenuItemEntity itemEntity = mMenuItemEntityList.get(position);
        if (TextUtils.isEmpty(itemEntity.getText())) {
            holder.tv_menu_item.setText(itemEntity.getItemType().getValueId());
        } else {
            holder.tv_menu_item.setText(itemEntity.getText());
        }
        if (itemEntity.getColorId() > 0) {
            holder.tv_menu_item.setTextColor(itemEntity.getColorId());
        } else {
            holder.tv_menu_item.setTextColor(0x777777);
        }
        holder.itemView.setTag(itemEntity);
    }

    @Override
    public int getItemCount() {
        return mMenuItemEntityList != null ? mMenuItemEntityList.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if (mOnMenuItemClickListener != null) {
            TextView itemView = (TextView) v.findViewById(R.id.tv_menu_item);
            MenuItemEntity itemEntity = (MenuItemEntity) v.getTag();
            mOnMenuItemClickListener.onItemClick(itemView, itemEntity);
        }
    }

    public void setDataList(List<MenuItemEntity> menuItemEntityList) {
        this.mMenuItemEntityList = menuItemEntityList;
    }

    public List<MenuItemEntity> getDataList() {
       return this.mMenuItemEntityList;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_menu_item;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
