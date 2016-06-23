package com.lyric.android.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.lyric.android.app.R;
import com.lyric.android.app.widget.photoview.HackyViewPager;

public class ImagePagerActivity extends FragmentActivity implements OnPageChangeListener {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";
	private HackyViewPager mHackyViewPager;
	/** 页面索引号 */
	private int mPagerPosition;
	private TextView tv_indicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_detail_pager);
		
		mPagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		String[] urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
		
		mHackyViewPager = (HackyViewPager) findViewById(R.id.pager);
		tv_indicator = (TextView) findViewById(R.id.tv_indicator);
		
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
		mHackyViewPager.setAdapter(mAdapter);
		CharSequence text = getString(R.string.viewpager_indicator, 1, mHackyViewPager.getAdapter().getCount());
		tv_indicator.setText(text);
		
		// 注册监听事件更新下标
		mHackyViewPager.setOnPageChangeListener(this);
		if (savedInstanceState != null) {
			mPagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}
		mHackyViewPager.setCurrentItem(mPagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mHackyViewPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {
		public String[] mImageUrlArray;

		public ImagePagerAdapter(FragmentManager fm, String[] imageUrlArray) {
			super(fm);
			this.mImageUrlArray = imageUrlArray;
		}

		@Override
		public int getCount() {
			return mImageUrlArray == null ? 0 : mImageUrlArray.length;
		}

		@Override
		public Fragment getItem(int position) {
			String url = mImageUrlArray[position];
			return ImageDetailFragment.newInstance(url);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int position) {
		CharSequence text = getString(R.string.viewpager_indicator, position + 1, mHackyViewPager.getAdapter().getCount());
		tv_indicator.setText(text);
	}
}