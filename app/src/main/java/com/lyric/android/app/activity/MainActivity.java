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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.fragment.ListFragment;
import com.lyric.android.app.fragment.ViewFragment;
import com.lyric.android.app.fragment.WebFragment;
import com.lyric.android.app.utils.AddPictureUtils;
import com.lyric.android.app.widget.AddPicturePopup;
import com.lyric.android.app.common.BaseActivity;
import com.lyric.android.app.common.BaseFragmentStatePagerAdapter;
import com.lyric.android.app.utils.DisplayUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 主页面
 * @author lyricgan
 */
public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private ImageView ivUserAvatar;

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

        initAddPictureUtils();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AddPictureUtils.REQUEST_CODE_TAKE_PHOTO: {// 拍照
                if (resultCode == Activity.RESULT_OK) {
                    int size = DisplayUtils.dip2px(AndroidApplication.getContext(), 72);
                    Bitmap bitmap = AddPictureUtils.getInstance().getBitmapForAvatar(size, size);
                    updateAvatarView(bitmap);
                }
            }
                break;
            case AddPictureUtils.REQUEST_CODE_PHOTO_ALBUM: {// 相册
                if (data != null && resultCode == Activity.RESULT_OK) {
                    int size = DisplayUtils.dip2px(AndroidApplication.getContext(), 72);
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

    private void initAddPictureUtils() {
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

    private void updateAvatarView(Bitmap bitmap) {
        if (bitmap != null) {
            ivUserAvatar.setImageBitmap(bitmap);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        ivUserAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_user_avatar);
        ivUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPictureUtils.getInstance().showPopup(v);
            }
        });
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
