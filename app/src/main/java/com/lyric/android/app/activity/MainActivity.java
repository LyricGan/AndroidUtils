package com.lyric.android.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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

import com.lyric.android.app.BaseApp;
import com.lyric.android.app.R;
import com.lyric.android.app.adapter.FragmentAdapter;
import com.lyric.android.app.fragment.ListFragment;
import com.lyric.android.app.fragment.LoadingFragment;
import com.lyric.android.app.fragment.LoginFragment;
import com.lyric.android.app.fragment.ProgressBarFragment;
import com.lyric.android.app.fragment.ViewTestFragment;
import com.lyric.android.app.fragment.WebFragment;
import com.lyric.android.app.utils.AddPictureUtils;
import com.lyric.android.app.widget.AddPicturePopup;
import com.lyric.android.app.utils.ActivityUtils;
import com.lyric.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyricgan
 * @time 2016/1/19 17:47
 */
public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private ImageView ivUserAvatar;

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
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                ActivityUtils.startActivity(MainActivity.this, SettingsActivity.class);
                return true;
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> titleList = new ArrayList<>();
        titleList.add("选项卡一");
        titleList.add("选项卡二");
        titleList.add("选项卡三");
        titleList.add("选项卡四");
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(3)));

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(ListFragment.newInstance());
        fragmentList.add(ListFragment.newInstance());
        fragmentList.add(ListFragment.newInstance());
        fragmentList.add(ListFragment.newInstance());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        ivUserAvatar = navigationView.getHeaderView(0).findViewById(R.id.iv_user_avatar);
        ivUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPictureUtils.getInstance().showPopup(v);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.nav_login: {
                        ActivityUtils.startActivity(MainActivity.this, BaseFragmentActivity.newIntent(MainActivity.this, LoginFragment.class));
                    }
                        break;
                    case R.id.nav_loading: {
                        ActivityUtils.startActivity(MainActivity.this, BaseFragmentActivity.newIntent(MainActivity.this, LoadingFragment.class));
                    }
                        break;
                    case R.id.nav_progress: {
                        ActivityUtils.startActivity(MainActivity.this, BaseFragmentActivity.newIntent(MainActivity.this, ProgressBarFragment.class));
                    }
                        break;
                    case R.id.nav_web: {
                        ActivityUtils.startActivity(MainActivity.this, BaseFragmentActivity.newIntent(MainActivity.this, WebFragment.class));
                    }
                        break;
                    case R.id.menu_item_view: {
                        ActivityUtils.startActivity(MainActivity.this, BaseFragmentActivity.newIntent(MainActivity.this, ViewTestFragment.class));
                    }
                        break;
                    case R.id.menu_item_video: {
                        ActivityUtils.startActivity(MainActivity.this, VideoListActivity.class);
                    }
                        break;
                    case R.id.menu_item_exit: {
                        finish();
                    }
                    break;
                }
                return true;
            }
        });
    }

    private void updateAvatarView(Bitmap bitmap) {
        if (bitmap != null) {
            ivUserAvatar.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final int size = DisplayUtils.dip2px(BaseApp.getContext(), 72);
        switch (requestCode) {
            case AddPictureUtils.REQUEST_CODE_TAKE_PHOTO: {// 拍照
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bitmap = AddPictureUtils.getInstance().getBitmapForAvatar(size, size);
                    updateAvatarView(bitmap);
                }
            }
                break;
            case AddPictureUtils.REQUEST_CODE_PHOTO_ALBUM: {// 相册
                if (data != null && resultCode == Activity.RESULT_OK) {
                    Bitmap bitmap = AddPictureUtils.getInstance().getBitmapForAvatar(data, size, size);
                    updateAvatarView(bitmap);
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
