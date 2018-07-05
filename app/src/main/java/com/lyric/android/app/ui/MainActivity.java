package com.lyric.android.app.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseActivity;
import com.lyric.android.app.common.BaseFragment;
import com.lyric.android.app.common.BaseFragmentStatePagerAdapter;
import com.lyric.android.app.test.Test;

import java.util.Arrays;
import java.util.List;

/**
 * main activity
 *
 * @author lyricgan
 */
public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private BaseFragment mFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreateContentView(View view, Bundle savedInstanceState) {
        Toolbar toolbar = findViewWithId(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mDrawerLayout = findViewWithId(R.id.dl_main_drawer);
        NavigationView navigationView = findViewWithId(R.id.nv_main_navigation);
        setupDrawerContent(navigationView);

        findViewWithId(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.app_name, Snackbar.LENGTH_SHORT).show();
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();

        Test.showLogs(AndroidApplication.getContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout != null) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            case R.id.action_settings:
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
        if (mFragment != null && mFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (mDrawerLayout != null) {
                    mDrawerLayout.closeDrawers();
                }
                return true;
            }
        });
    }

    private void setupViewPager() {
        TabLayout tabLayout = findViewWithId(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        String url = "https://www.baidu.com/";
        Fragment[] fragments = {ListFragment.newInstance(), ViewFragment.newInstance(), WebFragment.newInstance(url)};
        String[] titles = {ListFragment.class.getSimpleName(), ViewFragment.class.getSimpleName(), WebFragment.class.getSimpleName()};
        final List<Fragment> fragmentList = Arrays.asList(fragments);
        List<String> titleList = Arrays.asList(titles);
        int size = titleList.size();
        TabLayout.Tab tab;
        for (int i = 0; i < size; i++) {
            tab = tabLayout.newTab();
            tab.setText(titleList.get(i));

            tabLayout.addTab(tab);
        }
        PagerAdapter adapter = new BaseFragmentStatePagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(size);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position >= 0 && position < fragmentList.size()) {
                    Fragment fragment = fragmentList.get(position);
                    if (fragment instanceof BaseFragment) {
                        mFragment = (BaseFragment) fragment;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabLayout.setupWithViewPager(mViewPager);
    }

}
