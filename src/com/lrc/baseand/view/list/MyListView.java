package com.lrc.baseand.view.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.lrc.baseand.R;

/**
 * 自定义列表
 * 
 * @author lyric
 * @created 2014-1-16
 * 
 */
public class MyListView extends ListView implements OnScrollListener {
	public static final String TAG = MyListView.class.getSimpleName();
	private float mLastY = -1;
	private Scroller mScroller;
	private OnScrollListener mScrollListener;
	private IOnListLoadListener mListViewListener;
	/** 顶部视图 */
	private MyListViewHeader mListHeaderView;
	private RelativeLayout layout_header_content;
	private TextView tv_header_time;
	/** 顶部视图高度 */
	private int mHeaderViewHeight;
	/** 允许下拉刷新标记 */
	private boolean mEnablePullRefresh = true;
	/** 正在下拉刷新标记 */
	private boolean mPullRefreshing = false;
	/** 底部视图 */
	private MyListViewFooter mFooterView;
	/** 允许加载更多标记 */
	private boolean mEnablePullLoad;
	/** 正在加载更多标记 */
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;
	/** 列表项总数 */
	private int mTotalItemCount;
	private int mScrollBack;
	private final static int SCROLL_BACK_HEADER = 0;
	private final static int SCROLL_BACK_FOOTER = 1;
	/** 滚动持续时间 */
	private final static int SCROLL_DURATION = 400;
	/** 上拉超过50像素加载更多 */
	private final static int PULL_LOAD_MORE_DELTA = 50;
	private final static float OFFSET_RADIO = 1.8f;

	public MyListView(Context context) {
		super(context);
		initView(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);
		// 添加头部视图
		mListHeaderView = new MyListViewHeader(context);
		layout_header_content = (RelativeLayout) mListHeaderView.findViewById(R.id.layout_header_content);
		tv_header_time = (TextView) mListHeaderView.findViewById(R.id.tv_header_time);
		addHeaderView(mListHeaderView);
		
		// 初始化头部视图高度
		mListHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				mHeaderViewHeight = layout_header_content.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
		// 初始化底部视图
		mFooterView = new MyListViewFooter(context);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// 确保底部视图为列表最后一项，并且只添加一次
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * 设置是否允许下拉刷新
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		// 判断下拉刷新是否可用
		if (!mEnablePullRefresh) {
			layout_header_content.setVisibility(View.INVISIBLE);
		} else {
			layout_header_content.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置是否允许上拉加载更多
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(MyListViewFooter.STATE_NORMAL);
			// 上拉或者点击启动加载更多
			mFooterView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * 停止刷新，重置顶部视图
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			// 重置顶部视图
			resetHeaderHeight();
		}
	}

	/**
	 * 停止加载，重置底部视图
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			// 重置底部视图
			mFooterView.setState(MyListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * 设置刷新时间
	 * @param time
	 */
	public void setRefreshTime(String time) {
		tv_header_time.setText(time);
	}
	
	/**
	 * 调用滚动事件
	 */
	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnListScrollListener) {
			OnListScrollListener scrollListener = (OnListScrollListener) mScrollListener;
			scrollListener.onListScrolling(this);
		}
	}
	
	/**
	 * 修改顶部视图高度
	 * @param delta
	 */
	private void updateHeaderHeight(float delta) {
		mListHeaderView.setVisiableHeight((int) delta + mListHeaderView.getVisiableHeight());
		// 未处于刷新状态，更新箭头
		if (mEnablePullRefresh && !mPullRefreshing) {
			if (mListHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mListHeaderView.setState(MyListViewHeader.STATE_READY);
			} else {
				mListHeaderView.setState(MyListViewHeader.STATE_NORMAL);
			}
		}
		// 滚动到第一项
		setSelection(0);
	}

	/**
	 * 重置顶部视图高度
	 */
	private void resetHeaderHeight() {
		int height = mListHeaderView.getVisiableHeight();
		if (height == 0) {
			return;
		}
		// 判断下拉是否完全
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		// 默认回滚高度
		int finalHeight = 0;
		// 正在刷新，显示完全下拉刷新视图
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLL_BACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		// 触发计算滚动
		invalidate();
	}
	
	/**
	 * 修改底部视图高度
	 * @param delta
	 */
	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			// 判断高度是否大于上拉加载高度
			if (height > PULL_LOAD_MORE_DELTA) {
				mFooterView.setState(MyListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(MyListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);
	}
	
	/**
	 * 重置底部视图高度
	 */
	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLL_BACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
			invalidate();
		}
	}
	
	/**
	 * 加载更多
	 */
	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(MyListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			mLastY = ev.getRawY();
		}
			break;
		case MotionEvent.ACTION_MOVE: {
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (getFirstVisiblePosition() == 0 && (mListHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				// 修改顶部视图高度
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				// 调用滚动事件
				invokeOnScrolling();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				// 修改底部视图高度
				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
		}
			break;
		default: {
			mLastY = -1;
			if (getFirstVisiblePosition() == 0) {
				// 调用刷新
				if (mEnablePullRefresh && mListHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					mPullRefreshing = true;
					mListHeaderView.setState(MyListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						mListViewListener.onRefresh();
					}
				}
				// 重置顶部视图高度
				resetHeaderHeight();
			}
			if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// 调用加载更多
				if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
					// 加载更多
					startLoadMore();
				}
				// 重置底部视图高度
				resetFooterHeight();
			}
		}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLL_BACK_HEADER) {
				mListHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			// 调用滚动
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener listener) {
		mScrollListener = listener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}
	
	/**
	 * 设置列表加载监听接口
	 * @param listener
	 */
	public void setOnListLoadListener(IOnListLoadListener listener) {
		mListViewListener = listener;
	}

	/**
	 * 监听列表滑动接口
	 */
	public interface OnListScrollListener extends OnScrollListener {
		public void onListScrolling(View view);
	}

	/**
	 * 下拉刷新、加载更多接口
	 */
	public interface IOnListLoadListener {
		/** 下拉刷新 */
		public void onRefresh();

		/** 加载更多 */
		public void onLoadMore();
	}
}
