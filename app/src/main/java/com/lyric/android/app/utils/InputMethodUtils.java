package com.lyric.android.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘工具类
 *
 * @author Lyric Gan
 * @since 2016/4/6 11:14
 */
public class InputMethodUtils {

    private InputMethodUtils() {
    }

    private static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static void showSoftInput(View view) {
        InputMethodManager imm = getInputMethodManager(view.getContext());
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void showSoftInput(View view, int flags) {
        InputMethodManager imm = getInputMethodManager(view.getContext());
        if (imm != null) {
            imm.showSoftInput(view, flags);
        }
    }

    public static void hideSoftInput(View view) {
        hideSoftInput(view.getContext(), view.getWindowToken(), 0);
    }

    public static void hideSoftInput(Context context, IBinder windowToken, int flags) {
        InputMethodManager imm = getInputMethodManager(context);
        if (imm != null) {
            imm.hideSoftInputFromWindow(windowToken, flags);
        }
    }

    public static void toggle(Context context) {
        InputMethodManager imm = getInputMethodManager(context);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean isActive(Context context) {
        InputMethodManager imm = getInputMethodManager(context);
        return imm != null && imm.isActive();
    }

    public static void observeSoftKeyboard(Activity activity, final OnSoftKeyboardChangedListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = decorView.getHeight();
                int keyboardHeight = height - displayHeight;
                if (previousKeyboardHeight != keyboardHeight) {
                    boolean hide = (double) displayHeight / height > 0.8;
                    listener.onSoftKeyBoardChange(keyboardHeight, !hide);
                }
                previousKeyboardHeight = height;
            }
        });
    }

    /**
     * 软键盘变化监听事件
     */
    public interface OnSoftKeyboardChangedListener {

        void onSoftKeyBoardChange(int height, boolean visible);
    }
}
