package com.lyric.android.app.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.adapter.BaseRecyclerAdapter;
import com.lyric.android.app.adapter.SettingsListAdapter;
import com.lyric.android.app.entity.SettingsEntity;
import com.lyric.android.app.widget.TitleBar;
import com.lyric.android.app.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置页面
 * @author ganyu
 * @time 17/7/30
 */
public class SettingsActivity extends BaseCompatActivity {

    @Override
    protected void onTitleCreated(TitleBar titleBar) {
    }

    @Override
    public void onViewCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settings);
        RecyclerView recyclerView = findViewWithId(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SettingsListAdapter settingsListAdapter = new SettingsListAdapter(this);
        settingsListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<SettingsEntity>() {
            @Override
            public void onItemClick(int position, SettingsEntity object, View itemView) {
                ActivityUtils.startActivity(SettingsActivity.this, object.getCls());
            }
        });
        recyclerView.setAdapter(settingsListAdapter);

        settingsListAdapter.add(getSettingsEntityList());
    }

    private List<SettingsEntity> getSettingsEntityList() {
        List<SettingsEntity> settingsEntities = new ArrayList<>();
        settingsEntities.add(new SettingsEntity("Splash", SplashActivity.class));
        settingsEntities.add(new SettingsEntity("SwipeMenu", SwipeMenuActivity.class));
        settingsEntities.add(new SettingsEntity("SwipeMenuSimple", SwipeMenuSimpleActivity.class));
        return settingsEntities;
    }
}
