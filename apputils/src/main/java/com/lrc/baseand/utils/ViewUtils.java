package com.lrc.baseand.utils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Selection;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
/**
 * 视图工具类
 * 
 * @author ganyu
 * @created 2015-5-28
 *
 */
public class ViewUtils {
	private static final String CLASS_NAME_GRID_VIEW        = "android.widget.GridView";
    private static final String FIELD_NAME_VERTICAL_SPACING = "mVerticalSpacing";

    /**
     * 获取GridView垂直间距
     * @param gridView
     * @return
     */
    public static int getGridViewVerticalSpacing(GridView gridView) {
		Class<?> cls = null;
		int verticalSpacing = 0;
		try {
			cls = Class.forName(CLASS_NAME_GRID_VIEW);
			Field field = cls.getDeclaredField(FIELD_NAME_VERTICAL_SPACING);
			field.setAccessible(true);
			verticalSpacing = (Integer) field.get(gridView);
			return verticalSpacing;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verticalSpacing;
    }

	/**
	 * 设置视图及其子视图可用
	 * @param view
	 */
	public static void setViewEnabled(View view) {
		setViewEnabled(view, true);
	}
	
	/**
	 * 设置视图及其子视图不可用
	 * @param view
	 */
	public static void setViewUnabled(View view) {
		setViewEnabled(view, false);
	}
	
	/**
	 * 设置视图及其子视图是否可用
	 * @param view 视图对象
	 * @param enabled
	 */
	public static void setViewEnabled(View view, boolean enabled) {
		if (view instanceof ViewGroup) {
			ViewGroup viewGroup = ((ViewGroup) view);
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				View child = viewGroup.getChildAt(i);
				if (child instanceof ViewGroup) {
					setViewEnabled(child, enabled);
				}
				child.setEnabled(enabled);
			}
		} else {
			if (view != null && (view.isEnabled() != enabled)) {
				view.setEnabled(enabled);
			}
		}
	}
	
	/**
	 * 设置视图及其子视图点击时间
	 * @param view
	 * @param listener
	 */
	public static void setSearchViewOnClickListener(View view, OnClickListener listener) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = group.getChildAt(i);
                if (child instanceof LinearLayout || child instanceof RelativeLayout) {
                    setSearchViewOnClickListener(child, listener);
                }
                if (child instanceof TextView) {
                    TextView text = (TextView)child;
                    text.setFocusable(false);
                }
                child.setOnClickListener(listener);
            }
        }
    }
	
	/**
	 * 设置视图高度
	 * @param view
	 * @param height
	 * @see {@link View}
	 */
	public static void setViewHeight(View view, int height) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
    }
	
	/**
     * 设置ListView高度，{@link # getListViewHeightBasedOnChildren(ListView)}
     * @param view
     * @return
     */
    public static void setListViewHeightBasedOnChildren(ListView view) {
        setViewHeight(view, getListViewHeightBasedOnChildren(view));
    }

	/**
	 * 获取ListView的高度，通过计算 {@link ListView}
	 * @param view
	 * @return
	 */
	public static int getListViewHeightBasedOnChildren(ListView view) {
        int height = getAbsListViewHeightBasedOnChildren(view);
        ListAdapter adapter;
        int adapterCount;
        if (view != null && (adapter = view.getAdapter()) != null && (adapterCount = adapter.getCount()) > 0) {
            height += view.getDividerHeight() * (adapterCount - 1);
        }
        return height;
    }
	
	/**
     * 设置AbsListView的高度，通过计算 {@link # getAbsListViewHeightBasedOnChildren(AbsListView)}
     * @param view
     * @return
     */
    public static void setAbsListViewHeightBasedOnChildren(AbsListView view) {
        setViewHeight(view, getAbsListViewHeightBasedOnChildren(view));
    }
	
	/**
     * 获取AbsListView高度，{@link AbsListView}
     * @param view
     * @return
     */
    public static int getAbsListViewHeightBasedOnChildren(AbsListView view) {
        ListAdapter adapter;
        if (view == null || (adapter = view.getAdapter()) == null) {
            return 0;
        }
        int height = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, view);
            if (item instanceof ViewGroup) {
                item.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            item.measure(0, 0);
            height += item.getMeasuredHeight();
        }
        height += view.getPaddingTop() + view.getPaddingBottom();
        return height;
    }
    
    /**
	 * 将dip转换为px
	 * @param context 上下文对象
	 * @param dipValue dip
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f * (dipValue >= 0 ? 1 : -1));
	}
	
	/**
	 * 将px转换为dip
	 * @param context 上下文对象
	 * @param pxValue px
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f * (pxValue >= 0 ? 1 : -1));
	}
	
	/**
	 * 以数组的形式返回屏幕分辨率
	 * @param context 上下文对象
	 * @return
	 */
	public static int[] getDisplay(Context context) {
		if (context == null) {
			return null;
		}
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int[] display = new int[2];
		display[0] = metrics.widthPixels;
		display[1] = metrics.heightPixels;
		return display;
	}
	
	/**
	 * 隐藏软键盘
	 * @param context
	 * @param editText
	 */
	public static void hideInputMethod(Context context, EditText editText) {
		InputMethodManager inputMethodManager = null;
		try {
			inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (inputMethodManager != null) {
			inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}
	
	/**
	 * 显示软键盘
	 * @param context
	 * @param editText
	 */
	public static void showInputMethod(Context context, EditText editText) {
		InputMethodManager inputMethodManager = null;
		try {
			inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (inputMethodManager != null) {
			boolean isInputOpen = inputMethodManager.isActive();
			if (!isInputOpen) {
				inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
			}
		}
	}
	
	/**
	 * 设置输入框光标停留在文字后面
	 * @param context
	 * @param editText
	 */
	public static void setTextCursor(Context context, EditText editText) {
		CharSequence text = editText.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}
	
	/**
	 * 对ScrollView截图
	 * @param scrollView
	 * @return
	 */
	public static Bitmap getBitmap(ScrollView scrollView) {
		int height = 0;
		Bitmap bitmap = null;
		for (int i = 0; i < scrollView.getChildCount(); i++) {
			height += scrollView.getChildAt(i).getHeight();
			scrollView.getChildAt(i).setBackgroundColor(Color.WHITE);
		}
		bitmap = Bitmap.createBitmap(scrollView.getWidth(), height, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		scrollView.draw(canvas);
		return bitmap;
	}
	
	/**
	 * 对WebView截图
	 * @param webView
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap getBitmap(WebView webView) {
		float scale = webView.getScale();
		int height = (int) (webView.getContentHeight() * scale);
		final Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), height, Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);
		webView.draw(canvas);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		final byte[] bytes = stream.toByteArray();
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	/**
	 * 设置文本样式
	 * @param textView
	 */
	public static void setTextUnderline(TextView textView) {
		// 中间加横线
		textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		// 底部加横线：
//		textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	}
	
}