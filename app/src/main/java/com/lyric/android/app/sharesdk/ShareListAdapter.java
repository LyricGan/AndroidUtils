package com.lyric.android.app.sharesdk;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyric.android.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.ViewHolder> implements View.OnClickListener {
    private OnRecyclerViewItemClickListener onItemClickListener;

    private ShareSdkHelper.PlatformType[] platforms = ShareSdkHelper.PlatformType.values();
    private List<ShareSdkHelper.PlatformType> platList = new ArrayList<>(Arrays.asList(platforms));

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharesdk_platform_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        viewHolder.platformIcon = (ImageView) convertView.findViewById(R.id.platform_logo);
        viewHolder.platformName = (TextView) convertView.findViewById(R.id.platform_name);
        convertView.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.platformName.setText(platList.get(position).getName());
        holder.platformIcon.setImageResource(platList.get(position).getPlatformLogo());
        holder.itemView.setTag(platList.get(position));
    }

    @Override
    public int getItemCount() {
        return platList.size();
    }


    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, (ShareSdkHelper.PlatformType) v.getTag());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView platformIcon;
        public TextView platformName;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    //ItemClick的回调接口
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ShareSdkHelper.PlatformType platformType);
    }
}