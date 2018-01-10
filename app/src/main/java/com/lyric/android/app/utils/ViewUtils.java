package com.lyric.android.app.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.Selection;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * 视图工具类
 * 
 * @author lyricgan
 * @created 2015-5-28
 */
public class ViewUtils {
    /** 上一次操作时间 */
    private static long sLastOperateTime;

    public static boolean isFastOperated() {
        return isFastOperated(500L);
    }

    public static boolean isFastOperated(long maxDelayTimes) {
        long time = System.currentTimeMillis();
        long dis = time - sLastOperateTime;
        if (0 < dis && dis < maxDelayTimes) {
            return true;
        }
        sLastOperateTime = time;
        return false;
    }

    public <T extends View> T findViewById(View view, int id) {
        if (view != null) {
            return (T) view.findViewById(id);
        }
        return null;
    }

    public static Resources getResources(Context context) {
        return context.getResources();
    }

    public static void setLanguage(Context context, Locale locale) {
        Locale.setDefault(locale);
        Resources resources = getResources(context);
        Configuration config = resources.getConfiguration();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        config.locale = locale;
        resources.updateConfiguration(config, metrics);
    }

    public static Locale getLanguage(Context context) {
        Configuration config = getResources(context).getConfiguration();
        return config.locale;
    }

    /**
     * 设置视图高度
     * @param view View
     * @param height height
     */
    private static void setViewHeight(View view, int height) {
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
	public static void hideSoftKeyboard(Context context, EditText editText) {
		InputMethodManager imm = null;
		try {
			imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (imm != null) {
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}
	
	/**
	 * 显示软键盘
	 * @param context Context
     * @param editText EditText
	 */
	public static void showSoftKeyboard(Context context, EditText editText) {
		InputMethodManager imm = null;
		try {
			imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (imm != null) {
			boolean isInputOpen = imm.isActive();
			if (!isInputOpen) {
				imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
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
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
            if (statusBarHeight <= 0) {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object obj = clazz.newInstance();
                Field field = clazz.getField("status_bar_height");
                int height = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(height);
            }
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

    /**
     * 执行视图触摸反馈震动效果
     * @param view 视图
     * @return true or false
     */
    public static boolean performHapticFeedback(View view) {
        return view != null && (view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING));
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} to a fullscreen opaque
     * Activity.
     * <p>
     * Call this whenever the background of a translucent Activity has changed
     * to become opaque. Doing so will allow the {@link android.view.Surface} of
     * the Activity behind to be released.
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            if (method != null) {
                method.setAccessible(true);
                method.invoke(activity);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} back from opaque to
     * translucent following a call to
     * {@link #convertActivityFromTranslucent(android.app.Activity)} .
     * <p>
     * Calling this allows the Activity behind this one to be seen again. Once
     * all such Activities have been redrawn
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityToTranslucent(Activity activity) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerCls = null;
            for (Class<?> cls : classes) {
                if (cls.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerCls = cls;
                }
            }
            if (Build.VERSION.SDK_INT >= 21) {
                Method method = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerCls, ActivityOptions.class);
                if (method != null) {
                    method.setAccessible(true);
                    method.invoke(activity, new Object[] { null, null });
                }
            } else {
                Method method = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerCls);
                if (method != null) {
                    method.setAccessible(true);
                    method.invoke(activity, new Object[] { null });
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 获取背景Drawable，用于设置背景
     * @param color 填充颜色
     * @param cornerRadius 圆角半径
     * @param strokeWidth 圆边宽度
     * @param strokeColor 圆边颜色
     * @return 背景Drawable
     */
    public static GradientDrawable getDrawable(int color, int cornerRadius, int strokeWidth, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawable.setColor(color);
        drawable.setCornerRadius(cornerRadius);
        drawable.setStroke(strokeWidth, strokeColor);
        return drawable;
    }

    /**
     * 获取背景Drawable，区分默认和按下
     * @param normalDrawable 默认背景
     * @param pressedDrawable 按下背景
     * @return 背景Drawable，区分默认和按下
     */
    public static StateListDrawable getSelector(Drawable normalDrawable, Drawable pressedDrawable) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressedDrawable);
        drawable.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        drawable.addState(new int[]{}, normalDrawable);
        return drawable;
    }

    public static int[] getLocationOnScreen(View view) {
        int[] outLocation = new int[2];
        if (view != null) {
            view.getLocationOnScreen(outLocation);
        }
        return outLocation;
    }

    public static int[] getLocationInWindow(View view) {
        int[] outLocation = new int[2];
        if (view != null) {
            view.getLocationInWindow(outLocation);
        }
        return outLocation;
    }
}
