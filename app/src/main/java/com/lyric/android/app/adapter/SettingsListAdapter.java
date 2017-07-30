package com.lyric.android.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lyric.android.app.R;
import com.lyric.android.app.entity.SettingsEntity;

/**
 * 设置列表适配器
 * @author ganyu
 * @time 17/7/30
 */
public class SettingsListAdapter extends BaseRecyclerAdapter<SettingsEntity> {

    public SettingsListAdapter(Context context) {
        super(context, R.layout.item_settings_list);
    }

    @Override
    public void convert(View itemView, int position, SettingsEntity item) {
        TextView tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvName.setText(item.getName());
    }
}
