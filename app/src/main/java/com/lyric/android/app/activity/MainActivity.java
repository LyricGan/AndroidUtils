package com.lyric.android.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseActivity;
import com.lyric.android.app.common.BaseFragmentStatePagerAdapter;
import com.lyric.android.app.fragment.ListFragment;
import com.lyric.android.app.fragment.ViewFragment;
import com.lyric.android.app.fragment.WebFragment;

import java.util.Arrays;
import java.util.List;

/**
 * main activity
 * @author lyricgan
 */
public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onContentViewInitialize(View view, Bundle savedInstanceState) {
        Toolbar toolbar = findViewByIdRes(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mDrawerLayout = findViewByIdRes(R.id.dl_main_drawer);
        NavigationView navigationView = findViewByIdRes(R.id.nv_main_navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        FloatingActionButton floatingActionButton = findViewByIdRes(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home: {
                if (mDrawerLayout != null) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    private void setupViewPager() {
        TabLayout tabLayout = findViewByIdRes(R.id.tabs);
        // 设置为可滚动模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        Fragment[] fragments = {ListFragment.newInstance(), ViewFragment.newInstance(), WebFragment.newInstance("https://www.baidu.com/")};
        String[] titles = {ListFragment.class.getSimpleName(), ViewFragment.class.getSimpleName(), WebFragment.class.getSimpleName()};
        List<Fragment> fragmentList = Arrays.asList(fragments);
        List<String> titleList = Arrays.asList(titles);
        int size = titleList.size();
        for (int i = 0; i < size; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
        }
        PagerAdapter adapter = new BaseFragmentStatePagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        mViewPager.setAdapter(adapter);
        // 设置缓存页数
        mViewPager.setOffscreenPageLimit(titles.length);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_options_01:
                        break;
                    case R.id.nav_options_02:
                        break;
                    case R.id.nav_options_03:
                        break;
                    case R.id.nav_options_04:
                        break;
                    case R.id.menu_item_01:
                        break;
                    case R.id.menu_item_02:
                        break;
                    case R.id.menu_item_03:
                        break;
                }
                menuItem.setChecked(true);
                if (mDrawerLayout != null) {
                    mDrawerLayout.closeDrawers();
                }
                return true;
            }
        });
    }
}
