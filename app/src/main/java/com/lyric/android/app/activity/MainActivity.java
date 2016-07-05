package com.lyric.android.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lyric.android.app.R;
import com.lyric.android.app.adapter.FragmentAdapter;
import com.lyric.android.app.base.BaseApplication;
import com.lyric.android.app.mvvm.view.LoginActivity;
import com.lyric.android.app.utils.AddPictureUtils;
import com.lyric.android.app.view.AddPicturePopup;
import com.lyric.android.library.utils.ActivityUtils;
import com.lyric.android.library.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ganyu
 * @description
 * @time 2016/1/19 17:47
 */
public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ImageView iv_user_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Snackbar comes out", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "Toast comes out", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();

        initialize();
    }

    private void initialize() {
        AddPictureUtils.getInstance().initialize(this);
        AddPictureUtils.getInstance().setOnMenuClickListener(new AddPicturePopup.OnMenuClickListener() {
            @Override
            public void takePhoto(PopupWindow window) {
                AddPictureUtils.getInstance().takePhotoForAvatar(MainActivity.this);
            }

            @Override
            public void openPhotoAlbum(PopupWindow window) {
                AddPictureUtils.getInstance().openPhotoAlbum(MainActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> titles = new ArrayList<>();
        titles.add("第一页");
        titles.add("第二页");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ListFragment());
        fragments.add(new ListFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        iv_user_avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_user_avatar);
        iv_user_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPictureUtils.getInstance().showPopup(v);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_login: {
                        ActivityUtils.jumpActivity(MainActivity.this, LoginActivity.class);
                    }
                        break;
                    case R.id.nav_loading: {// LoadingTest
                        ActivityUtils.jumpActivity(MainActivity.this, LoadingActivity.class);
                    }
                        break;
                    case R.id.nav_progress: {// CircleProgressBar
                        ActivityUtils.jumpActivity(MainActivity.this, CircleProgressBarActivity.class);
                    }
                        break;
                    case R.id.nav_web: {// WebActivity
                        ActivityUtils.jumpActivity(MainActivity.this, WebActivity.class);
                    }
                        break;
                    case R.id.menu_item_about: {// 关于
                        ActivityUtils.jumpActivity(MainActivity.this, ViewTestActivity.class);
                    }
                        break;
                    case R.id.menu_item_exit: {// 退出
                        finish();
                    }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final int size = DisplayUtils.dip2px(BaseApplication.getContext(), 72);
        switch (requestCode) {
            case AddPictureUtils.REQUEST_CODE_TAKE_PHOTO: {// 拍照
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bitmap = AddPictureUtils.getInstance().getBitmapForAvatar(size, size);
                    if (bitmap == null) {
                        return;
                    }
                    iv_user_avatar.setImageBitmap(bitmap);
                }
            }
                break;
            case AddPictureUtils.REQUEST_CODE_PHOTO_ALBUM: {// 相册
                if (data != null && resultCode == Activity.RESULT_OK) {
                    Bitmap bitmap = AddPictureUtils.getInstance().getBitmapForAvatar(data, size, size);
                    if (bitmap == null) {
                        return;
                    }
                    iv_user_avatar.setImageBitmap(bitmap);
                }
            }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AddPictureUtils.getInstance().destroy();
    }
}
