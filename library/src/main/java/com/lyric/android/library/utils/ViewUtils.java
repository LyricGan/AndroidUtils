package com.lyric.android.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
     * @param gridView GridView
     * @return int
     */
    public static int getGridViewVerticalSpacing(GridView gridView) {
		Class<?> cls;
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
	 * @param view View
	 */
	public static void setViewEnabled(View view) {
		setViewEnabled(view, true);
	}
	
	/**
	 * 设置视图及其子视图不可用
	 * @param view View
	 */
	public static void setViewUnabled(View view) {
		setViewEnabled(view, false);
	}
	
	/**
	 * 设置视图及其子视图是否可用
	 * @param view 视图对象
	 * @param enabled boolean
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
	 * @param view View
	 * @param listener OnClickListener
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
	 * @param view View
	 * @param height height
	 * @see {@link View}
	 */
	public static void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
    }
	
	/**
     * 设置ListView高度，{@link # getListViewHeightBasedOnChildren(ListView)}
     * @param view ListView
     */
    public static void setListViewHeightBasedOnChildren(ListView view) {
        setViewHeight(view, getListViewHeightBasedOnChildren(view));
    }

	/**
	 * 获取ListView的高度，通过计算 {@link ListView}
	 * @param view ListView
	 * @return int
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
     * @param view AbsListView
     */
    public static void setAbsListViewHeightBasedOnChildren(AbsListView view) {
        setViewHeight(view, getAbsListViewHeightBasedOnChildren(view));
    }
	
	/**
     * 获取AbsListView高度，{@link AbsListView}
     * @param view AbsListView
     * @return int
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
	 * 隐藏软键盘
	 * @param context Context
	 * @param editText EditText
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
	 * @param context Context
     * @param editText EditText
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
	 * @param context Context
     * @param editText EditText
	 */
	public static void setTextCursor(Context context, EditText editText) {
		CharSequence text = editText.getText();
		if (text != null) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}
	
	/**
	 * 对ScrollView截图
	 * @param scrollView ScrollView
	 * @return Bitmap
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
	 * @param webView WebView
	 * @return Bitmap
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
		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

    private static long sLastClickTime;
    private static final long MAX_DELAY_TIME = 500L;

    /**
     * 判断是否对视图快速点击
     * @return boolean
     */
    public static boolean isFastClicked() {
        long time = System.currentTimeMillis();
        long dis = time - sLastClickTime;
        if (0 < dis && dis < MAX_DELAY_TIME) {
            return true;
        }
        sLastClickTime = time;
        return false;
    }

    /**
     * 解决ScrollView嵌套导致的高度计算问题
     * @return the measure specification based on size and mode
     */
    public static int getExpandSpec() {
        return View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
    }

    /**
     * 获取状态栏高度
     * @param context Context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int height = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    public static void setStatusBarColor(Activity activity, int color) {
        setStatusBarColor(activity, color, false);
    }

    /**
     * 设置状态栏颜色，需要在style文件加上<item name="android:fitsSystemWindows">true</item>
     * @param activity Activity
     * @param color 状态栏颜色
     * @param isInjectNavigation 是否影响虚拟导航栏
     */
    public static void setStatusBarColor(Activity activity, int color, boolean isInjectNavigation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (isInjectNavigation) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintColor(color);
            tintManager.setStatusBarTintEnabled(true);
            if (isInjectNavigation) {
                tintManager.setNavigationBarTintColor(color);
                tintManager.setNavigationBarTintEnabled(true);
            }
        }
    }

    public static void setStatusBarOverlay(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    public static void hideSystemUI(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public static void showSystemUI(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public static void setVisibility(View view, boolean visibility) {
        if (view == null) {
            return;
        }
        if (visibility) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            }
        }
    }

    public static void setVisibility(View view, int visibility) {
        if (view == null || (visibility != View.VISIBLE && visibility != View.INVISIBLE && visibility != View.GONE)) {
            return;
        }
        if (visibility != view.getVisibility()) {
            view.setVisibility(visibility);
        }
    }

    /**
     * 获取虚拟按键栏高度
     * @param context Context
     * @return 虚拟按键栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        int height = 0;
        if (isMeizu()) {
            height = getMeizuBarHeight(context);
        } else if (hasNavigationBar(context)) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                height = resources.getDimensionPixelSize(resourceId);
            }
        }
        return height;
    }

    public static boolean hasNavigationBar(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean flag = resources.getBoolean(resourceId);
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                flag = false;
            } else if ("0".equals(sNavBarOverride)) {
                flag = true;
            }
            return flag;
        } else {
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return sNavBarOverride;
    }

    private static boolean isMeizu() {
        return Build.BRAND.equals("Meizu");
    }

    private static int getMeizuBarHeight(Context context) {
        try {
            Class cls = Class.forName("com.android.internal.R$dimen");
            Object obj = cls.newInstance();
            Field field = cls.getField("mz_action_button_min_height");
            int height = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
