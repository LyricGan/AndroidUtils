package com.lyric.android.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lyric.android.app.R;
import com.lyric.android.app.widget.photoview.PhotoViewAttacher;
import com.lyric.android.library.image.NetworkImageView;

public class ImageDetailFragment extends Fragment {
	private static final String TAG = ImageDetailFragment.class.getSimpleName();
	/** 图片地址 */
	private String mImageUrl;
	private NetworkImageView mImageView;
	private ProgressBar mProgressBar;
	private PhotoViewAttacher mAttacher;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment fragment = new ImageDetailFragment();
		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		fragment.setArguments(args);
		
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_image_detail, container, false);
		mImageView = (NetworkImageView) view.findViewById(R.id.iv_picture);
		mAttacher = new PhotoViewAttacher(mImageView);
		mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View view, float x, float y) {
				getActivity().finish();
			}
		});
		mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        mProgressBar.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.GONE);
        mImageView.setImageUrl(mImageUrl);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                mAttacher.update();
            }
        }, 3000);
	}
}
