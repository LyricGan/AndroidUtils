package com.lyric.android.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author lyric
 * @description
 * @time 2016/4/6 11:14
 */
public class InputMethodUtils {
    public static final int SHOW_IMPLICIT = InputMethodManager.SHOW_IMPLICIT;
    public static final int SHOW_FORCED = InputMethodManager.SHOW_FORCED;
    public static final int HIDE_IMPLICIT_ONLY = InputMethodManager.HIDE_IMPLICIT_ONLY;
    public static final int HIDE_NOT_ALWAYS = InputMethodManager.HIDE_NOT_ALWAYS;

    InputMethodUtils() {
    }

    private static InputMethodManager from(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            throw new AssertionError("InputMethodManager is null.");
        }
        return imm;
    }

    public static void show(View view) {
        InputMethodManager imm = from(view.getContext());
        imm.showSoftInput(view, SHOW_IMPLICIT);
    }

    public static void show(View view, int flags) {
        InputMethodManager imm = from(view.getContext());
        imm.showSoftInput(view, flags);
    }

    public static void hide(EditText editText) {
        InputMethodManager imm = from(editText.getContext());
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void hide(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            return;
        }
        InputMethodManager imm = from(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void toggle(Context context) {
        InputMethodManager imm = from(context);
        imm.toggleSoftInput(0, HIDE_NOT_ALWAYS);
    }

    public static boolean isActive(EditText editText) {
        InputMethodManager imm = from(editText.getContext());
        return imm.isActive();
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

    public interface OnSoftKeyboardChangedListener {
        void onSoftKeyBoardChange(int height, boolean visible);
    }
}
